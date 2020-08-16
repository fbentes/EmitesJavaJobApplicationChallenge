package com.imdb.query.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.inject.Inject;

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
 * @author F�bio Bentes
 * @version 1.0.0.0
 * @since 09/08/2020
 * 
 */
public class StartClient {

	private static String ipServer = Constants.IP_SERVER_DEFAULT;
	
	private static int port = Constants.PORT_DEFAULT;
	
	@Inject
	private IMDbClientSocket imdbClientSocket;
	
	/**
	 *  
	 * @param args[0] pode ser o IP do servidor ou a porta usada. Se for a porta, args.length deve ser 1 !
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
		
		if(args == null || args.length == 0) {
			
			return true;
		}
		
		if(args.length > 2) {
			
			System.out.println("N�o pode haver mais de 2 argumentos !");
			
			return false;
		}
		
		AddressNetworkValidator addressNetworkValidator = new AddressNetworkValidator();
		
		TCPPortUtility tcpPortUtility = new TCPPortUtility();
		
		boolean retorno = false;
		
		if(args.length == 1) {  // Ou � um IP ou � uma porta.

			if(addressNetworkValidator.isIpValid(args[0])) {
				
				ipServer = args[0];
				
				retorno = true;
				
			} else {
				
				if(tcpPortUtility.isPortValid(args[0])) {
					
					port = Integer.parseInt(args[0]);
					
					retorno = true;
				}
			}
			
			if(!retorno) {
				System.out.println("O par�metro n�o � um IP v�lido e nem uma porta v�lida !");
			}
			
			return retorno;
		}
		
		// args[0] deve ser um IP e args[1], se houver, deve ser uma porta. 
		// A porta padr�o 20222 ser� usada se a porta n�o for informada pelo usu�rio.
		
		if(args.length == 2) {    
			
			if(addressNetworkValidator.isIpValid(args[0])) {
				
				ipServer = args[0];
				
			} else {
				System.out.println("O IP " + args[0] + " � inv�lido !");
				return false;
			}

			if(tcpPortUtility.isPortValid(args[1])) {
				
				port = Integer.parseInt(args[1]);
				
			} else {
				System.out.println("A porta " + args[1] + " � inv�lida !");
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
		
		try {
			
			do {
				
				String movieTitle = getMovieTitleInputed();
			
				if(movieTitle.trim().toLowerCase().equals("exit") ) {
					break;
				}
	
				if(!canConnectServer(ipServer, port)) {
					continue;
				}
				
				String responseOfServerWithMovieTitles = imdbClientSocket.sendMovieTitleToSearchInServer(movieTitle);
				
				System.out.println("");
				System.out.println("Resposta:");
				System.out.println(responseOfServerWithMovieTitles);
				System.out.println("");
				
			} while(true);
		
		} finally {
			
			System.out.println("*********************");
			System.out.println("Conex�o do cliente encerrada !");
			
			imdbClientSocket.stopConnection();
		}
	}

	private boolean canConnectServer(String ipServer, int port) {
		
		System.out.println("Conectando com o servidor...\n");
		
		boolean connected = imdbClientSocket.connectToServer(ipServer, port);
		
		if(!connected) {
			
			System.out.println("Conex�o n�o estabelecida entre o cliente e servidor !");
		}
		
		return connected;
	}
	
	private String getMovieTitleInputed() {
		
		String movieTitle = "";
		
		System.out.print("Digite o t�tulo do filme para pesquisa no IMDB (exit encerra o cliente): ");

        BufferedReader movieTitleBufferedReader =  
                new BufferedReader(new InputStreamReader(System.in)); 
      
		try {
			
			movieTitle = movieTitleBufferedReader.readLine();
			
		} catch (IOException e) {
			
			System.out.println("Problema na leitura do t�tulo do filme: " + e.getMessage());
		} 
		
		return movieTitle;
	}	
}
