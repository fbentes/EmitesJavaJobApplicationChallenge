/**
 * 
 */
package com.imdb.query.main;

import java.io.IOException;


import javax.inject.Inject;

import com.imdb.query.server.IMDbSocketServer;
import com.imdb.query.server.ServerCommand;
import com.imdb.query.server.impl.ServerCommandImpl;
import com.imdb.query.util.IMDbQueryModuleInjector;

/**
 * @author F�bio Bentes
 *
 *  Classe respons�vel para iniciar o servidor Socket no prompt de comando.
 *  
 */
public class StartServer {

	@Inject
	private IMDbSocketServer imdbSocketServer;
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws InterruptedException, IOException {
			
		StartServer startServer = new StartServer();
		
		// Infe��o de depend�ncia para a inst�ncia de StartServer
		
		IMDbQueryModuleInjector.initialize(startServer);
		
        // Iniciando o servidor TCP Socket para esperar solicita��es do(s) cliente(s)
        
        Thread thread = new Thread(new Runnable() {
		    public void run() {

		    	ServerCommand serverCommand = new ServerCommandImpl();
		    	serverCommand.setIMDbSocketServer(startServer.imdbSocketServer);
		    	serverCommand.execute();
		    }
		  });
		
        thread.start();
        
        // Espera 4 segundos para dar tempo de carregar a lista de filmes do site IMDb antes de prosseguir a execu��o principal.
	    
        thread.join(3000); 

	    System.out.println("");
		System.out.println("Digite kill para parar o servidor: ");	
	    System.out.println("");

	    // S� para o servidor se for digitado, literalmente, "kill" !
	    
	    do {
			
		    int read = System.in.read();

		    if(read == 107) {
		    	
				startServer.imdbSocketServer.stop();
				
				break;
		    }

	    } while (true);
		
	    if(startServer.imdbSocketServer.isStoped()) {
			
			System.out.println("*********************");
			System.out.println("Servidor parado !");
		}	
	}
}
