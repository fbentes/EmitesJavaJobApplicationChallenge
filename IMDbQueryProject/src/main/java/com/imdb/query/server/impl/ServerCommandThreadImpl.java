package com.imdb.query.server.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.imdb.query.server.IMDbServerSocket;
import com.imdb.query.server.ServerCommandThread;
import com.imdb.query.util.Constants;

/**
 * Classe que implementa o pattern Command respons�vel para iniciar o servidor TCP Socket numa Thread, 
 * esperando as solicita��es do cliente.
 * 
 * @author F�bio Bentes
 * @version 1.0.0.0
 * @since 10/08/2020
 * 
 */
public class ServerCommandThreadImpl extends Thread implements ServerCommandThread {

    private static final Logger logger = LogManager.getLogger(ServerCommandThreadImpl.class);

    private IMDbServerSocket imdbServerSocket;
    
    public ServerCommandThreadImpl(IMDbServerSocket imdbServerSocket) {
    	
    	this.imdbServerSocket = imdbServerSocket;
    }
    
    @Override
	public void run() {
		
    	 synchronized(this) {
    		 
				logger.info("");
    			
    			boolean connected = imdbServerSocket.connectToServerSocket();
    			
    			if(!connected) {
    				logger.info("Servidor n�o conectado !!!");
    				return;
    			}

				if(imdbServerSocket.getPort() == imdbServerSocket.getAlternativePort()) {
					
					logger.info(String.format("Servidor conectado na porta %d.", imdbServerSocket.getPort()));
				}
				else {
					logger.info("****************************************");
					logger.info(String.format("Porta %d est� ocupada !", imdbServerSocket.getPort()));
					logger.info(String.format("Servidor conectado na porta %d.", imdbServerSocket.getAlternativePort()));
					logger.info("****************************************");
				}

				logger.info("");
				logger.info("Carregando a lista de filmes ...");
				
				int totalMovies = imdbServerSocket.loadMovieLlistFromImdb();
				
				logger.info("Total de filmes carregados = " + totalMovies);
				
				logger.info(Constants.STRING_EMPTY);
				logger.info("Esperando requisi��o do cliente ...");
				
				notify();
    	 }

    	 imdbServerSocket.waitingForClientRequests();			
	
		logger.info(Constants.STRING_EMPTY);		
	}
}
