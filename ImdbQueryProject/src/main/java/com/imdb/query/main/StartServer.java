/**
 * 
 */
package com.imdb.query.main;

import com.imdb.query.server.ImdbSocketServer;
import com.imdb.query.util.Constants;
import com.imdb.query.util.ImdbQueryModule;

import javax.inject.Inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
/**
 * @author Fábio Bentes
 *
 */
public class StartServer {

	@Inject
	private ImdbSocketServer imdbSocketServer;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		StartServer startServer = new StartServer();
		
		Module module = new ImdbQueryModule();
        Injector injector = Guice.createInjector(module);
        injector.injectMembers(startServer);
       
        startServer.execute();
	}
	
	private void execute() {
		
		System.out.println("Conectando no servidor na porta " + Constants.PORT);
		
		boolean connected = imdbSocketServer.connect(Constants.PORT);
		
		if(connected) {

			System.out.println("Servidor conectado.");

			System.out.println("Carregando a lista de filmes ...");
			
			int totalMovies = imdbSocketServer.loadMovieLlistFromImdb();
			
			System.out.println("Total de filmes carregados = " + totalMovies);
			
			System.out.println("");
			System.out.println("Esperando requisição do cliente ...");
			
			imdbSocketServer.waitingForClientRequests();			
		
		} else {
			System.out.println("Servidor não conectado !!!");			
		}
		
		System.out.println("");
		
	}
}
