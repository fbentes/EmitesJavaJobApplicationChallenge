package com.imdb.query.client.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
	
    private static final Logger logger = LogManager.getLogger("IMDbClientSocketImpl");

    private boolean isClientSocketConnected;
	
	private Socket clientSocket;
	
    private BufferedReader readFromServerBufferedReader;
    
    private PrintWriter writeToServerPrintWriter;
    
    @Inject
    private IMDbCommunicationProtocol iMDbCommunicationProtocol;
    
    @Override
	public boolean connectToServer(String ipServer, int port) {
		
		try {
			
			clientSocket = new Socket(ipServer,port);
			
			isClientSocketConnected = true;			
			
		} catch (Exception e) {
			
			logger.error(Constants.STRING_EMPTY);
			
			if(e.getMessage().trim().toLowerCase().equals("connection refused: connect")) {
				
				logger.error("A conexão do cliente foi recusada porque o servidor não responde !");
			} else {
				
				logger.error(e.getMessage());
			}
			
			isClientSocketConnected = false;
		}

		return isClientSocketConnected;
	}
	
	/**
	 * Aplica o protocolo de comunicação entre o cliente e servidor.
	 * 
	 * @param movieTitle O título de um filme a ser pesquisado pelo usuário.
	 * @return Verdadeiro se o padrão de protocolo aplicado for válido.
	 */
	private boolean isAppliedProtocol(String movieTitle) {

		iMDbCommunicationProtocol.setMovieTitleWithPatternProtocol(
				Constants.PREFIX_PROTOCOL + movieTitle + Constants.SUFIX_PROTOCOL);

		return iMDbCommunicationProtocol.isMatchPatternProtocol();	
	}
	
    @Override
	public String sendMovieTitleToSearchInServer(String movieTitle) {

		if(!isAppliedProtocol(movieTitle)) {
			
			return String.format(Constants.IVALID_MESSAGE_PROTOCOL, movieTitle);
		}
		
		try {

			readFromServerBufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			writeToServerPrintWriter = new PrintWriter(clientSocket.getOutputStream(), true);

		} catch (IOException e) {
			
			logger.error(e.getMessage());
			
			return Constants.STRING_EMPTY;
		}

		try {

			writeToServerPrintWriter.println(iMDbCommunicationProtocol.getMovieTitleWithPatternProtocol());	
			
			StringBuffer responseOfServerWithMovieTitles = new StringBuffer();
			
			String responseReadLine;
			
			// PS: Não usado Optional e nem Constants.STRING_EMPTY para não afetar no desempenho na iteração.
			
            while ((responseReadLine = readFromServerBufferedReader.readLine()) != null) {
            	
            	if(!responseReadLine.trim().equals("")) {
            		
                	responseOfServerWithMovieTitles.append(responseReadLine + "\n");
            	}
 	        }

	        return responseOfServerWithMovieTitles.toString();

		} catch (IOException e) {

			logger.error(e.getMessage());
			
			return e.getMessage();
		}
   }
	
    @Override
	public boolean stopConnection() {
		
    	if(!isClientSocketConnected) {
    		return false;
    	}
    	
        try {
			
        	readFromServerBufferedReader.close();
			
		} catch (IOException e) {
			
			logger.error(e.getMessage());
			
			return false;
		}
        
        writeToServerPrintWriter.close();
        
        try {
			
        	clientSocket.close();
        	
		} catch (IOException e) {
			
			logger.error(e.getMessage());
			
			return false;
		}
        
        return true;
    }	
}
