package com.imdb.query.server.impl;

import com.imdb.query.server.IMDbServerSocket;
import com.imdb.query.server.ServerCommand;

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

	@Override
	public void execute(IMDbServerSocket imdbSocketServer) {
		
		System.out.println("Conectando no servidor na porta " + imdbSocketServer.getPort());
		
		boolean connected = imdbSocketServer.connect();
		
		if(connected) {

			if(imdbSocketServer.getPort() == imdbSocketServer.getAlternativePort()) {
				
				System.out.println("Servidor conectado na porta " + imdbSocketServer.getPort());
			}
			else {
				System.out.println("****************************************");
				System.out.println("Porta " + imdbSocketServer.getPort() + " está ocupada !");
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
