package com.imdb.query.server.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.imdb.query.server.IMDbUrlConnection;
import com.imdb.query.util.Constants;
import com.imdb.query.util.IMDbQueryModuleInjector;
import com.imdb.query.util.protocol.IMDbCommunicationProtocol;

/**
 * Handler respons�vel pelos atendimentos das requisi��es dos clientes.
 *
 * @author F�bio Bentes
 * @version 1.0.0.0
 * @since 09/08/2020
 * 
 */
public class IMDbClientHandler extends Thread {
	
    private static final Logger logger = LogManager.getLogger("IMDbClientHandler");

    private Socket clientSocket;

    @Inject
	private IMDbUrlConnection iMDbUrlConnection;

    @Inject
    private IMDbCommunicationProtocol iMDbCommunicationProtocol;
    
    private PrintWriter writeToClientPrintWriter;
    
    private BufferedReader readFromClientBufferedReader;
    
	public void setClientSocket(Socket clientSocket) {
    	
    	this.clientSocket = clientSocket;
    }
	
	/**
	 * Initicializa recursos e injeta depend�ncias.
	 * 
	 */
	private boolean initializedResources() {
		
		IMDbQueryModuleInjector.initialize(this);
		
        try {
			
        	writeToClientPrintWriter = new PrintWriter(clientSocket.getOutputStream(), true);
			
			readFromClientBufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			return true;

        } catch (IOException e) {
			
			logger.error(e.getMessage());
			
			return false;
		}
	}

	/**
	 * Leitura do t�tulo do filme enviado pelo client socket que foi digitado pelo usu�rio.
	 * 
	 * @return T�tulo do filme digitado pelo usu�rio do cliente. 
	 */
	private Optional<String> readFromClientSocket() {
		
		try {
			
			return Optional.ofNullable(readFromClientBufferedReader.readLine());
			
		} catch (IOException e) {
			
			logger.error("Problema na resposta para o cliente: " + e.getMessage());
		}
			
		return Optional.empty();
	}
	
	/**
	 * Valida se o cliente socket enviou um t�tulo de filme para o servidor.
	 * 
	 * @param movieTitle T�tulo do filme.
	 * @return Verdadeiro se foi digitado um t�tulo. Falso caso contr�rio.
	 */
	private boolean isValidMovieTitle(Optional<String> movieTitle) {
		
		if(!movieTitle.isPresent() ) {
			
            writeToClientPrintWriter.println(Constants.IVALID_MESSAGE_CLIENT_TO_SERVER);

            logger.error(String.format(Constants.IVALID_MESSAGE_CLIENT_TO_SERVER, ""));
            
            return false;
		}

		return true;
	}
    
    public void run() {
    	
    	try {
			
    		if(!initializedResources()) {
    			return;
    		}

    		// Leitura do t�tulo do filme enviado pelo cliente socket.
    		
    		Optional<String> movieTitle = readFromClientSocket();
    		
    		if(!isValidMovieTitle(movieTitle)) {
    			
    			return;
    		}
    		       
    		// Checa se o t�tulo do filme foi enviado pelo cliente socket com o protocolo correto. 
    		
            if(!iMDbCommunicationProtocol.isMatchPatternProtocol(movieTitle.get())) {
            	
                responseToClientSocketWithMessageError(movieTitle.get());
                
                return;
            }
            
            // Remove o protocolo aplicado no t�tulo do filme.
            
            String movieTitleWithOutProtocol = removeMovieTitleProtocolToSearch(movieTitle.get());
            
            // Retorna o(s) filme(s) encontrado(s).
            
            String moviesFound = iMDbUrlConnection.getMoviesFound(movieTitleWithOutProtocol);

            // Responde para o cliente socket o(s) filme(s) encontrados com o protocolo de comunica��o aplicado.
            
            responseToClientSocket(applyCommunicationProtocol(moviesFound));			
		} 
        finally {
			
			clearAllocatedResources();
		}
    }
    
    /**
     * Aplica protocolo de comunica��o nos filmes encontrados.
     * 
     * @param moviesFound Filmes encontrados pelo servidor separados por \n.
     * @return Filmes encontrados com protocolo aplicado.
     */
    private String applyCommunicationProtocol(String moviesFound) {
    	
    	return iMDbCommunicationProtocol.getMessageWithPatternProtocolApplied(moviesFound);
    }
    
    /**
     * Remove protocolo de comunica��o do t�tulo de filme.
     * 
     * @param movieTitle T�tulo do filme.
     * @return T�tulo do filme sem protocolo.
     */
    private String removeMovieTitleProtocolToSearch(String movieTitle) {
    	
        return iMDbCommunicationProtocol.getMessageWithOutPatternProtocolApplied(movieTitle);
    }
    
    
    /**
     * Responde para o cliente socket mensagem inv�lida para protocolo.
     * 
     * @param movieTitle T�tulo do filme.
     */
    private void responseToClientSocketWithMessageError(String movieTitle) {
    	
         writeToClientPrintWriter.println(Constants.IVALID_MESSAGE_PROTOCOL);
    }

    /**
     * Responde para o cliente socket os filmes encontrados pelo servidor.
     * 
     * @param moviesFound Filmes encontrados.
     */
    private void responseToClientSocket(String moviesFound) {
    	
        writeToClientPrintWriter.println(moviesFound);			
    }

    /**
     * Libera todos os recursos alocados.
     * 
     */
    private void clearAllocatedResources() {
    	
        try {
			
        	readFromClientBufferedReader.close();
			
		} catch (IOException e) {
			
			logger.error(e.getMessage());
		}
        
        writeToClientPrintWriter.close();

        try {
			
        	clientSocket.close();
			
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
    }
}
