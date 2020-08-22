package com.imdb.query.client.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.imdb.query.client.IMDbClientSocket;
import com.imdb.query.util.Constants;
import com.imdb.query.util.protocol.IMDbCommunicationProtocol;

/**
 * Responsável pela comunicação com o servidor Socket.
 *
 * @author Fábio Bentes
 * @version 1.0.0.0
 * @since 09/08/2020
 * 
 */
public class IMDbClientSocketImpl implements IMDbClientSocket {
	
    private static final Logger logger = LogManager.getLogger(IMDbClientSocketImpl.class);

	private Socket clientSocket;
	
    @Inject
    private IMDbCommunicationProtocol iMDbCommunicationProtocol;
    
    @Override
	public boolean connectToServer(String ipServer, int port) {
		
		try {
			
			clientSocket = new Socket(ipServer,port);
			
			return true;			
			
		} catch (Exception e) {
			
			logger.error(Constants.STRING_EMPTY);
			
			if(e.getMessage().trim().toLowerCase().equals("connection refused: connect")) {
				
				logger.error("A conexão do cliente foi recusada porque o servidor não responde !");
			} else {
				
				logger.error(e.getMessage());
			}
			
			return false;
		}
	}

    /**
     * Valida se foi preenchido um título de filme.
     * 
     * @param movieTitle Título do filme.
     * @return Verdadeiro se for válido. Falso caso contrário.
     */
    private boolean isValidMovieTitle(String movieTitle) {
    	
		Optional<String> optionalMovieTitle = Optional.ofNullable(movieTitle);
		
		return optionalMovieTitle.isPresent();
    }
	
    /**
     * Aplica protocolo de comunicação nos filmes encontrados.
     * 
     * @param movieTitle Título do filme.
     * @return Título do filme com protocolo aplicado.
     */
    private String applyCommunicationProtocol(String movieTitle) {
    	
    	return iMDbCommunicationProtocol.getMessageWithPatternProtocolApplied(movieTitle);
    }
       
    /**
     * Captura a resposta do servidor socket (filmes encontrados separados por \n).
     * 
     * @return Filme ou filmes respondidos pelo servidor socket.
     */
    private String readResponseFromServerSocket() {
    	
		StringBuilder responseOfServerWithMovieTitles = new StringBuilder();
		
		// Lê a resposta do servidor socket iterativamente 
		
		try(BufferedReader readFromServerBufferedReader = 
				new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
			
			readFromServerBufferedReader.lines().forEach(responseReadLine ->{
	        	
				if(!responseReadLine.trim().equals(Constants.STRING_EMPTY)) {
	        		
	            	responseOfServerWithMovieTitles.append(responseReadLine + "\n");
	        	}
			});
			
	        return responseOfServerWithMovieTitles.toString();

		} catch (IOException e) {
			
			logger.error(e.getMessage());
			
			return e.getMessage();
		}
    }
        
    @Override
	public String requestMovieTitleToSearchInServer(String movieTitle) {
		
    	try {
    		
			if(!isValidMovieTitle(movieTitle)) {
				
				return Constants.INFORME_TITULO_FILME;
			}
	
			String responseFromServerSocket;
			
	    	try (PrintWriter writeToServerPrintWriter = 
	    			new PrintWriter(clientSocket.getOutputStream(), true)){
	    		
				// Envia para o servidor socket o título do filme com o protocolo de comunicação aplicado.
				
	    		writeToServerPrintWriter.println(applyCommunicationProtocol(movieTitle));
	    			    				
				// Recebe a resposta do servidor socket com o(s) filme(s) encontrado(s) com o protocolo de comunicação aplicado.
				
				responseFromServerSocket = readResponseFromServerSocket();

	    	} catch (Exception e) {
				
				logger.error(e.getMessage());
				
				return Constants.MOVIE_TITLE_TO_SERVER_SOCKET_COMMUNICATION_ERROR;
			}
	    	
    		// Se a resposta do servidor não obedecer o protocolo especificado, significa que outro
	        // servidor respondeu ao cliente. Então será registrado no log a resposta original
	        // e apresentada ao cliente apenas que a resposta desobedeceu o protocolo.
	        
	        if(!iMDbCommunicationProtocol.isMatchPatternProtocol(responseFromServerSocket)) {
	        
	        	return Constants.IVALID_MESSAGE_PROTOCOL;
	        }
	        
	        // Retira o protocolo aplicado no servidor socket para exibição puramente dos dados retornados para o cliente.
	        
	        return iMDbCommunicationProtocol.
	        		getMessageWithOutPatternProtocolApplied(responseFromServerSocket);
	    }
    	finally {
    		
    		closeClientSocket();    		
    	}
   }
    
	private void closeClientSocket() {
		
        try {

        	clientSocket.close();
        	
		} catch (IOException e) {
			
			logger.error(e.getMessage());
		}
    }	
}
