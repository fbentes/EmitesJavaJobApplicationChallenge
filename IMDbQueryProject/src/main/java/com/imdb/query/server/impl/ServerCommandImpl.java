package com.imdb.query.server.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    private static final Logger logger = LogManager.getLogger("ServerCommandImpl");

    @Override
	public void execute(IMDbServerSocket imdbServerSocket) {
		
		logger.info(String.format("Conectando no servidor na porta %d ...", imdbServerSocket.getPort()));
		
		boolean connected = imdbServerSocket.connect();
		
		if(connected) {

			if(imdbServerSocket.getPort() == imdbServerSocket.getAlternativePort()) {
				
				logger.info(String.format("Servidor conectado na porta %d.", imdbServerSocket.getPort()));
			}
			else {
				logger.info("****************************************");
				logger.info(String.format("Porta %d está ocupada !", imdbServerSocket.getPort()));
				logger.info(String.format("Servidor conectado na porta %d.", imdbServerSocket.getAlternativePort()));
				logger.info("****************************************");
			}

			logger.info("Carregando a lista de filmes ...");
			
			int totalMovies = imdbServerSocket.loadMovieLlistFromImdb();
			
			logger.info("Total de filmes carregados = " + totalMovies);
			
			logger.info(Constants.STRING_EMPTY);
			logger.info("Esperando requisição do cliente ...");
			logger.info(Constants.STRING_EMPTY);
			
			imdbServerSocket.waitingForClientRequests();			
		
		} else {
			logger.info("Servidor não conectado !!!");			
		}
		
		logger.info(Constants.STRING_EMPTY);		
	}
}
