package com.imdb.query.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.imdb.query.client.IMDbClientSocket;
import com.imdb.query.util.Constants;
import com.imdb.query.util.IMDbQueryModuleInjector;
import com.imdb.query.util.network.AddressNetworkValidator;
import com.imdb.query.util.network.TCPPortUtility;

/**
 *	Classe respons�vel para iniciar o Client Socket para solicita��es de nomes de filmes para o servidor Socket.
 *
 *  Podem ser pesquisados nomes completos, mas tamb�m iniciais de nomes de filmes para o retorno
 *  dos nomes concatenados com "\n". Nesse caso a pesquisa funcionar� como um like no SQL !
 *  
 * Tutorial para execu��o:
 * 
 * C:\Temp\java -jar IMDbClientSocket.jar [ipServidor | porta]
 * 
 * Os argumentos s�o opcionais. Se forem omititos o ipServidor ser� localhost e a porta padr�o ser� 20222. Ex.:
 *
 * C:\Temp\java -jar IMDbClientSocket.jar  (conecta no servidor em localhost e porta padr�o 20222).
 *
 * C:\Temp\java -jar IMDbClientSocket.jar 34985 (conecta no servidor em localhost e porta 34985).
 *
 * C:\Temp\java -jar IMDbClientSocket.jar 192.168.0.16 (conecta no servidor em 192.168.0.16 e porta padr�o 20222).
 *
 * C:\Temp\java -jar IMDbClientSocket.jar 192.168.0.16 33845 (conecta no servidor em 192.168.0.16 e porta 33845).
 * 
 * - Cada cliente deve ser executado em sua inst�ncia de prompt de comando para simular chamadas simult�neas.
 * 
 * - Se o cliente tentar se conectar numa porta alocada por outro processo que n�o seja o servidor socket da solu��o, poder� haver travamento no caso da porta 135 (RPC) ou rejei��o no caso da porta 6969 (Acmsoda - cliente bittorrent) com a mensagem personalizada ('O protocolo de comunica��o est� inv�lido') para resposta de Bad Request desse Acmsoda ou outros servi�os afins.
 *
 * - As pesquisas por t�tulos de filmes podem ser feitas pelo nome completo ou pelo in�cio do nome do t�tulo (Ex.: 'Batman' para retornar todos os filmes que comecem por essa palavra).
 *
 * @author F�bio Bentes
 * @version 1.0.0.0
 * @since 09/08/2020
 * 
 */
public class StartClient extends StartBase  {

    private static final Logger logger;
    
    static {
    	
    	disableAccessWarnings();
    	
    	System.out.println("");
    	System.out.println("Iniciando mecanismo de log, aguarde ...");
    	
    	logger = LogManager.getLogger(StartClient.class);
    }

	private static String ipServer = Constants.IP_SERVER_DEFAULT;
	
	private static int port = Constants.PORT_DEFAULT;
	
	@Inject
	private IMDbClientSocket imdbClientSocket;
	
	/**
	 *  
	 * @param args[0] pode ser o IP do servidor ou a porta usada.
	 *  
	 * Ex.: c:\java -jar IMDbClientSocket.jar 192.168.0.12
	 *  ou  c:\java -jar IMDbClientSocket.jar 20233
	 *     
	 * @param args[1] Se informado, deve ser a porta, pois args[0] deve ser o IP do servidor !
	 * 
	 * Ex.: c:\java -jar IMDbClientSocket.jar 192.168.0.12 20233
	 */
	public static void main(String[] args) {

		if(!readArguments(args)) {
			return;
		}
		
		StartClient startClient = new StartClient();

		IMDbQueryModuleInjector.initialize(startClient);

		startClient.execute(ipServer, port);
	}

	/**
	 * Respons�vel pela leitura dos par�metros na linha de comando.
	 * Sendo v�lido apenas IP e/ou porta para conex�o.
	 * 
	 * @param args IP e/ou porta para conex�o.
	 * @return Verdadeiro se a leitura foi bem sucessida ou se nenhum argumento foi informado. Falso caso contr�rio.
	 */
	private static boolean readArguments(String[] args) {
		
		Optional<String[]> optionalArgs = Optional.ofNullable(args); 
		
		if(!optionalArgs.isPresent() || optionalArgs.get().length == 0) { // Se args == null ou args.length == 0.
			
			return true;
		}
		
		if(optionalArgs.get().length > 2) {
			
			logger.info("N�o pode haver mais de 2 argumentos !");
			
			return false;
		}
		
		Optional<String> optionalFirstArg = Optional.of(args[0]);

		AddressNetworkValidator addressNetworkValidator = new AddressNetworkValidator();
		
		TCPPortUtility tcpPortUtility = new TCPPortUtility();
		
		boolean isIpValid = false;
		
		boolean isPortValid = false;
		
		boolean retorno = false;
		
		if(optionalArgs.get().length == 1) {  // Ou � um IP ou � uma porta.

			isIpValid = addressNetworkValidator.isIpValid(optionalFirstArg.get());
			
			if(isIpValid) {
				
				ipServer = optionalFirstArg.get();
				
				retorno = true;
				
			} else {
				
				isPortValid = tcpPortUtility.isPortValid(optionalFirstArg.get());
				
				if(isPortValid) {
					
					port = Integer.parseInt(optionalFirstArg.get());
					
					logger.info(String.format("O argumento %s � uma porta v�lida !", optionalFirstArg.get()));

					retorno = true;				
				}
			}

			return retorno;
		}
		
		// args[0] deve ser um IP e args[1], se houver, deve ser uma porta. 
		// A porta padr�o 20222 ser� usada se a porta n�o for informada pelo usu�rio.
		
		Optional<String> optionalSecondArg = Optional.of(args[1]);
		
		if(optionalArgs.get().length == 2) {    
			
			isIpValid = addressNetworkValidator.isIpValid(optionalFirstArg.get());
			
			if(isIpValid) {
				
				ipServer = optionalFirstArg.get();
			}

			isPortValid = tcpPortUtility.isPortValid(optionalSecondArg.get());
			
			if(isPortValid) {
				
				port = Integer.parseInt(optionalSecondArg.get());
			}
			
			if(!isIpValid || !isPortValid ) {
				return false;
			}
		}
		
    	return true;
	}

	/**
	 * Inicia o cliente socket para solicita��es para o servidor.
	 * 
	 * @param ipServer IP do servidor socket.
	 * @param port Porta usada pelo servidor socket.
	 */
	private void execute(String ipServer, int port) {
		
		do {
			
			Optional<String> movieTitle = getMovieTitleInputed();
		
			if(movieTitle.get().trim().toLowerCase().equals("quit") ) {
				break;
			}

			if(!canConnectServer(ipServer, port)) {
				continue;
			}
			
			String responseOfServerWithMovieTitles = imdbClientSocket.requestMovieTitleToSearchInServer(movieTitle.get());
			
			logger.info(Constants.STRING_EMPTY);
			logger.info("Resposta:");
			logger.info(responseOfServerWithMovieTitles);
			logger.info("Conex�o com o servidor fechada !");
			logger.info("******************************************************");
			
		} while(true);
		
		logger.info(Constants.STRING_EMPTY);
		logger.info("Cliente finalizado pelo usu�rio !!!");
	}

	private boolean canConnectServer(String ipServer, int port) {
		
		boolean connected = imdbClientSocket.connectToServer(ipServer, port);
		
		if(connected) {
			
			logger.info(String.format("Conectado no servidor em %s:%s.", ipServer, port));
		}
		else {
			logger.info("Conex�o n�o estabelecida entre o cliente e servidor !");
			logger.info("******************************************************");
		}
		
		return connected;
	}
	
	private Optional<String> getMovieTitleInputed() {
		
		Optional<String> movieTitle = Optional.empty();
		
		logger.info(Constants.STRING_EMPTY);		
		logger.info("Digite o t�tulo do filme para pesquisa no IMDb (p/ sair digite quit): ");

        BufferedReader movieTitleBufferedReader =  
                new BufferedReader(new InputStreamReader(System.in)); 
      
		try {
			
			movieTitle = Optional.ofNullable(movieTitleBufferedReader.readLine());
			
		} catch (IOException e) {
			
			logger.error("Problema na leitura do t�tulo do filme: " + e.getMessage());
		} 
		
		return movieTitle;
	}	
}
