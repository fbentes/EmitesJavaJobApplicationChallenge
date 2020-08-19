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
 * Respons�vel pela comunica��o com o servidor Socket.
 *
 * @author F�bio Bentes
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
				
				logger.error("A conex�o do cliente foi recusada porque o servidor n�o responde !");
			} else {
				
				logger.error(e.getMessage());
			}
			
			isClientSocketConnected = false;
		}

		return isClientSocketConnected;
	}
	
	/**
	 * Aplica o protocolo de comunica��o entre o cliente e servidor.
	 * 
	 * @param movieTitle O t�tulo de um filme a ser pesquisado pelo usu�rio.
	 * @return Verdadeiro se o padr�o de protocolo aplicado for v�lido.
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
			
			// PS: N�o usado Optional e nem Constants.STRING_EMPTY para n�o afetar no desempenho na itera��o.
			
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
