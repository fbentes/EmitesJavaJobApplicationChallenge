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
    
    public void run() {
    	
		try {
			
			IMDbQueryModuleInjector.initialize(this);
			
	        writeToClientPrintWriter = new PrintWriter(clientSocket.getOutputStream(), true);
	        
			readFromClientBufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			// Recebe o nome, ou parte inicial do nome, do filme digitado pelo usu�rio.
			
			Optional<String> movieTitle = Optional.ofNullable(readFromClientBufferedReader.readLine());
			
			if(!movieTitle.isPresent() ) {
				
	            writeToClientPrintWriter.println(String.format(Constants.IVALID_MESSAGE_CLIENT_TO_SERVER, ""));

	            logger.error(String.format(Constants.IVALID_MESSAGE_CLIENT_TO_SERVER, ""));
	            
	            return;
			}
			
	        if(!isMatchPatternProtocol(movieTitle.get())) {
	        	
	            writeToClientPrintWriter.println(Constants.IVALID_MESSAGE_PROTOCOL);
	        }

            String moviesFound = iMDbUrlConnection.getMoviesFound(iMDbCommunicationProtocol.getMovieTitleWithOutPatternProtocol());

            writeToClientPrintWriter.println(moviesFound);
			
		} catch (IOException e) {
			
			logger.error("Problema na resposta para o cliente: " + e.getMessage());
			
		} finally {
			
			clearAllocatedResources();
		}
    }

    /**
     * Checa se o protocolo de comunica��o est� sendo atendido.
     *  
     * @param movieTitle Nome, ou parte inicial do nome, do filme digitado pelo usu�rio.
     * @return Verdadeiro caso o cliente tenha aplicado corretamente o protocolo exigido. Falso caso contr�rio.
     */
	private boolean isMatchPatternProtocol(String movieTitle) {
		
		iMDbCommunicationProtocol.setMovieTitleWithPatternProtocol(movieTitle);
		
		return iMDbCommunicationProtocol.isMatchPatternProtocol();	
	}
	
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
