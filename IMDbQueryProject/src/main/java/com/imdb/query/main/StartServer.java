package com.imdb.query.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.inject.Inject;

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
        
        // Espera 4 segundos para dar tempo de carregar a lista de filmes do site IMDb antes de prosseguir a thread principal.
	    
        thread.join(3000); 

	    do {
			
		    System.out.println("");
			System.out.println("Digite kill para parar o servidor: ");	
		    System.out.println("");

		    // S� para o servidor se for digitado, literalmente, "kill" !
		    
	        BufferedReader movieTitleBufferedReader =  
	                new BufferedReader(new InputStreamReader(System.in)); 
	      
	        String read = null;
	        
			try {
				
				read = movieTitleBufferedReader.readLine();
				
			} catch (IOException e) {
				
				System.out.println("Problema na leitura do teclado: " + e.getMessage());
				read = "kill";
			} 

		    if(read.equals("kill")) {
		    	
				startServer.imdbServerSocket.stop();
				
				break;
		    }

	    } while (true);
		
	    if(startServer.imdbServerSocket.isStoped()) {
			
			System.out.println("*********************");
			System.out.println("Servidor parado !");
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
		
		if(args == null || args.length == 0) {
			
			return true;
		}
		
		if(args.length > 1) {
			
			System.out.println("N�o pode haver mais de um argumento !");
			
			return false;
		}
		
		TCPPortUtility tcpPortUtility = new TCPPortUtility();
		
		if(tcpPortUtility.isPortValid(args[0])) {
			
			port = Integer.parseInt(args[0]);
			
			return true;
			
		} 
		
		System.out.println("A porta informada � inv�lida !");
		
		return false;
	}
}
