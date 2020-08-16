package com.imdb.query.server.impl;

import com.imdb.query.server.IMDbSocketServer;
import com.imdb.query.server.ServerCommand;
import com.imdb.query.util.Constants;

/**
 * Classe que implementa o pattern Command responsável para iniciar o servidor TCP Socket, 
 * esperando as solicitações do cliente.
 * 
 * @author Fábio Bentes
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
		
		System.out.println("Conectando no servidor na porta " + Constants.PORT_DEFAULT);
		
		boolean connected = imdbSocketServer.connect(Constants.PORT_DEFAULT);
		
		if(connected) {

			if(Constants.PORT_DEFAULT == imdbSocketServer.getAlternativePort()) {
				
				System.out.println("Servidor conectado na porta " + imdbSocketServer.getAlternativePort());
			}
			else {
				System.out.println("****************************************");
				System.out.println("Porta "+Constants.PORT_DEFAULT+" fechada.");
				System.out.println("Servidor conectado na porta " + imdbSocketServer.getAlternativePort());
				System.out.println("****************************************");
			}

			System.out.println("Carregando a lista de filmes ...");
			
			int totalMovies = imdbSocketServer.loadMovieLlistFromImdb();
			
			System.out.println("Total de filmes carregados = " + totalMovies);
			
			System.out.println("");
			System.out.println("Esperando requisição do cliente ...");
			System.out.println("");
			
			imdbSocketServer.waitingForClientRequests();			
		
		} else {
			System.out.println("Servidor não conectado !!!");			
		}
		
		System.out.println("");		
	}
}
