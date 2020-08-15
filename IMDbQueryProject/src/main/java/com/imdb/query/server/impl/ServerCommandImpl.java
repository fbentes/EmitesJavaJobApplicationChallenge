package com.imdb.query.server.impl;

import com.imdb.query.server.IMDbSocketServer;
import com.imdb.query.server.ServerCommand;
import com.imdb.query.util.Constants;

/**
 * Classe que implementa o pattern Command respons�vel para iniciar o servidor TCP Socket, 
 * esperando as solicita��es do cliente.
 * 
 * @author F�bio Bentes
 * @version 1.0.0.0
 * @since 10/08/2020
 * 
 */
public class ServerCommandImpl implements ServerCommand {

	private IMDbSocketServer imdbSocketServer;
	
	public void setIMDbSocketServer(IMDbSocketServer imdbSocketServer) {
		this.imdbSocketServer = imdbSocketServer;
	}
	
	@Override
	public void execute() {
		
		System.out.println("Conectando no servidor na porta " + Constants.PORT);
		
		boolean connected = imdbSocketServer.connect(Constants.PORT);
		
		if(connected) {

			System.out.println("Servidor conectado.");

			System.out.println("Carregando a lista de filmes ...");
			
			int totalMovies = imdbSocketServer.loadMovieLlistFromImdb();
			
			System.out.println("Total de filmes carregados = " + totalMovies);
			
			System.out.println("");
			System.out.println("Esperando requisi��o do cliente ...");
			System.out.println("");
			
			imdbSocketServer.waitingForClientRequests();			
		
		} else {
			System.out.println("Servidor n�o conectado !!!");			
		}
		
		System.out.println("");		
	}
}
