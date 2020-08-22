package com.imdb.query.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.imdb.query.server.IMDbServerSocket;
import com.imdb.query.server.ServerCommandThread;
import com.imdb.query.server.impl.ServerCommandThreadImpl;
import com.imdb.query.util.Constants;
import com.imdb.query.util.IMDbQueryModuleInjector;
import com.imdb.query.util.network.TCPPortUtility;

/**
 *  Classe respons�vel para iniciar o servidor Socket no prompt de comando.
 *  
 * Tutorial para execu��o:
 * 
 * C:\Temp\java -jar IMDbServerSocket.jar [porta]
 * 
 * O argumento � opcional. Se for omitido o argumento, a porta padr�o ser� 20222.  
 * 
 * C:\Temp\java -jar IMDbServerSocket.jar  (executa na porta padr�o 20222).
 * 
 * C:\Temp\java -jar IMDbServerSocket.jar 32987 (executa na porta 32987).
 * 
 * - V�rios servidores podem ser instanciados, cada um no seu prompt e na sua porta, para futuras conex�es de clientes.
 *
 * - Um servidor socket � instanciado numa Thread filha para que a Thread principal possa gerenci�-lo. 
 *   E o servidor aloca uma Thread para cada atendimento de solicita��o de cliente. Assim m�ltiplas conex�es podem ser estabelecidas.
 *
 * - Se a porta estiver ocupada por outro processo, ser� feita tentativas de aloca��o pelo servidor socket at� encontrar uma porta aberta. 
 *   Essa porta aberta rec�m alocada pelo servidor socket ser� impressa no console para que o cliente saiba qual porta se conectar.
 * 
 * @author F�bio Bentes
 * @version 1.0.0.0
 * @since 09/08/2020
 * 
 */
public class StartServer extends StartBase {

    private static final Logger logger;
    
    static {
    	
    	disableAccessWarnings();
    	
    	System.out.println("");
    	System.out.println("Iniciando mecanismo de log, aguarde ...");
    	
    	logger = LogManager.getLogger(StartServer.class);
    }
    
    private static int port = Constants.PORT_DEFAULT;
    
	@Inject
	private IMDbServerSocket imdbServerSocket;
	
	/**
	 * Tenta conectar na porta padr�o 20222. Caso j� tenha sido alocada por conex�o desse 
	 * servidor socket ou algum outro processo, h� a tentativa para conex�o em outra porta dispon�vel.
	 * Sendo assim, torna-se poss�vel o atendimento de solicita��es dos clientes por diferentes portas
	 * conectadas.
	 * 
	 * @param args Porta para conex�o socket. Se n�o for informada, a porta dar�o 20222 ser� usada.
	 * @throws InterruptedException Disparado caso haja algum problema na chamada de thread.join(3000).
	 * @throws IOException Disparado caso haja algum problema na chamada de System.in.read().
	 */
	public static void main(String[] args) throws InterruptedException, IOException {
			
		if(!readArguments(args)) {
			return;
		}

		StartServer startServer = new StartServer();
		
		// Infe��o de depend�ncia para a inst�ncia de StartServer
		
		IMDbQueryModuleInjector.initialize(startServer);
		
        // Iniciando o servidor TCP Socket para esperar solicita��es do(s) cliente(s)
		// Cada inst�ncia do servidor socket iniciar� numa thread �nica, podendo assim,
		// ser parada pelo usu�rio na thread principal a qualquer momento.
        
    	ServerCommandThread serverCommandThread = new ServerCommandThreadImpl(startServer.imdbServerSocket);
    	
    	startServer.imdbServerSocket.setPort(port);
    	
    	serverCommandThread.start();

    	synchronized(serverCommandThread){
    		
       		serverCommandThread.wait();
    	}

    	do {
			
			System.out.print("(Digite kill para parar o servidor)");	
		    logger.info(Constants.STRING_EMPTY);

		    // S� para o servidor se for digitado, literalmente, "quit" !
		    
	        BufferedReader movieTitleBufferedReader =  
	                new BufferedReader(new InputStreamReader(System.in)); 
	      
	        Optional<String> readKeyboard = Optional.empty();
	        
			try {
				
				readKeyboard = Optional.ofNullable(movieTitleBufferedReader.readLine());
				
			} catch (IOException e) {
				
				logger.info("Problema na leitura do teclado: " + e.getMessage());
				
				readKeyboard = Optional.of("kill");
			} 

		    if(readKeyboard.isPresent() && readKeyboard.get().toLowerCase().trim().equals("kill")) {
		    	
				startServer.imdbServerSocket.requestStopExecution();
				
				break;
		    }

	    } while (true);
			    
	    if(startServer.imdbServerSocket.closeServerSocket()) {
			
	    	serverCommandThread.interrupt();
	    	
			logger.info(Constants.STRING_EMPTY);
			logger.info("Servidor parado pelo usu�rio !!!");
		}	
	}
	
	/**
	 * Captura a porta informada na linha de comando. Caso n�o seja  
	 * informado argumento, a porta padr�o 20222 ser� usada.
	 * 
	 * Ex.: C:\java -jar IMDbServerSocket.jar 23908
	 * 
	 * @param args Porta para conex�o socket.
	 * @return Verdadeiro se n�o foi informado argumento ou se uma porta v�lida foi informada. Falso caso contr�rio.
	 */
	private static boolean readArguments(String[] args) {
		
		Optional<String[]> optionalArgs = Optional.ofNullable(args); 

		if(!optionalArgs.isPresent() || optionalArgs.get().length == 0) { // Se args == null ou args.length == 0.
			
			return true;  // continua com port = Constants.PORT_DEFAULT;
		}
		
		if(optionalArgs.get().length > 1) {  // Usu�rio informou a porta de conex�o pela linha de comando.
			
			logger.info("N�o pode haver mais de um argumento !");
			
			return false;
		}
		
		Optional<String> optionalFirstArg = Optional.of(args[0]);

		TCPPortUtility tcpPortUtility = new TCPPortUtility();
		
		if(tcpPortUtility.isPortValid(optionalFirstArg.get())) {
			
			port = Integer.parseInt(optionalFirstArg.get());
			
			return true;
		} 
		
		logger.info(String.format("A porta %s � inv�lida. O n�mero m�ximo permitido � %d !", optionalFirstArg.get(), Constants.PORT_TCP_MAX));
		
		return false;
	}
	
}
