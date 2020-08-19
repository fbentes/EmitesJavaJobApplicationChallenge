package com.imdb.query.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

import com.google.inject.Inject;

import com.imdb.query.server.IMDbServerSocket;
import com.imdb.query.server.ServerCommand;
import com.imdb.query.server.impl.ServerCommandImpl;
import com.imdb.query.util.Constants;
import com.imdb.query.util.IMDbQueryModuleInjector;
import com.imdb.query.util.network.TCPPortUtility;

/**
 *  Classe respons�vel para iniciar o servidor Socket no prompt de comando.
 *  
 * @author F�bio Bentes
 * @version 1.0.0.0
 * @since 09/08/2020
 * 
 */
public class StartServer {

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
        
        Thread thread = new Thread(new Runnable() {
		    public void run() {

		    	ServerCommand serverCommand = new ServerCommandImpl();
		    	
		    	startServer.imdbServerSocket.setPort(port);
		    	
		    	serverCommand.execute(startServer.imdbServerSocket);
		    }
		  });
		
        thread.start();
        
        // Espera 3 segundos para dar tempo de carregar a lista de filmes do site IMDb antes de prosseguir a thread principal.
	    
        thread.join(3000); 

	    do {
			
		    System.out.println(Constants.STRING_EMPTY);
			System.out.print("Digite kill para parar o servidor: ");	
		    System.out.println(Constants.STRING_EMPTY);

		    // S� para o servidor se for digitado, literalmente, "kill" !
		    
	        BufferedReader movieTitleBufferedReader =  
	                new BufferedReader(new InputStreamReader(System.in)); 
	      
	        Optional<String> readKeyboard = Optional.empty();
	        
			try {
				
				readKeyboard = Optional.ofNullable(movieTitleBufferedReader.readLine());
				
			} catch (IOException e) {
				
				System.out.println("Problema na leitura do teclado: " + e.getMessage());
				
				readKeyboard = Optional.of("kill");
			} 

		    if(readKeyboard.isPresent() && readKeyboard.get().toLowerCase().trim().equals("kill")) {
		    	
				startServer.imdbServerSocket.requestStop();
				
				break;
		    }

	    } while (true);
			    
	    if(startServer.imdbServerSocket.close()) {
			
	    	thread.interrupt();
	    	
			System.out.println(Constants.STRING_EMPTY);
			System.out.println("Servidor parado pelo usu�rio !!!");
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
			
			System.out.println("N�o pode haver mais de um argumento !");
			
			return false;
		}
		
		Optional<String> optionalFirstArg = Optional.of(args[0]);

		TCPPortUtility tcpPortUtility = new TCPPortUtility();
		
		if(tcpPortUtility.isPortValid(optionalFirstArg.get())) {
			
			port = Integer.parseInt(optionalFirstArg.get());
			
			return true;
		} 
		
		System.out.println("A porta " + optionalFirstArg.get() + " � inv�lida. O n�mero m�ximo permitido � " + Constants.PORT_TCP_MAX);
		
		return false;
	}
}
