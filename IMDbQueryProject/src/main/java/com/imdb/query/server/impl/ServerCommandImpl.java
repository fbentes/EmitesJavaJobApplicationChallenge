package com.imdb.query.server.impl;

import com.imdb.query.server.IMDbServerSocket;
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

	@Override
	public void execute(IMDbServerSocket imdbServerSocket) {
		
		System.out.println("Conectando no servidor na porta " + imdbServerSocket.getPort());
		
		boolean connected = imdbServerSocket.connect();
		
		if(connected) {

			if(imdbServerSocket.getPort() == imdbServerSocket.getAlternativePort()) {
				
				System.out.println("Servidor conectado na porta " + imdbServerSocket.getPort());
			}
			else {
				System.out.println("****************************************");
				System.out.println("Porta " + imdbServerSocket.getPort() + " está ocupada !");
				System.out.println("Servidor conectado na porta " + imdbServerSocket.getAlternativePort());
				System.out.println("****************************************");
			}

			System.out.println("Carregando a lista de filmes ...");
			
			int totalMovies = imdbServerSocket.loadMovieLlistFromImdb();
			
			System.out.println("Total de filmes carregados = " + totalMovies);
			
			System.out.println(Constants.STRING_EMPTY);
			System.out.println("Esperando requisição do cliente ...");
			System.out.println(Constants.STRING_EMPTY);
			
			imdbServerSocket.waitingForClientRequests();			
		
		} else {
			System.out.println("Servidor não conectado !!!");			
		}
		
		System.out.println(Constants.STRING_EMPTY);		
	}
}
