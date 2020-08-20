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
	
    @Override
	public String sendMovieTitleToSearchInServer(String movieTitle) {

		try {

			readFromServerBufferedReader = new BufferedReader(
					new InputStreamReader(clientSocket.getInputStream()));

			writeToServerPrintWriter = new PrintWriter(clientSocket.getOutputStream(), true);

		} catch (IOException e) {
			
			logger.error(e.getMessage());
			
			return Constants.STRING_EMPTY;
		}

		try {
			Optional<String> optionalMovieTitle = Optional.ofNullable(movieTitle);
			
			if(!optionalMovieTitle.isPresent()) {
				
				return Constants.DIGITE_TITULO_FILME;
			}
			
			String movieTitleWithProtocol = iMDbCommunicationProtocol.getMessageWithPatternProtocolApplied(movieTitle);
			
			writeToServerPrintWriter.println(movieTitleWithProtocol);	
			
			StringBuffer responseOfServerWithMovieTitles = new StringBuffer();
			
			String responseReadLine;
			
			// PS: N�o usado Optional e nem Constants.STRING_EMPTY para n�o afetar no 
			// desempenho na itera��o.
			
            while ((responseReadLine = readFromServerBufferedReader.readLine()) != null) {
            	
            	if(!responseReadLine.trim().equals("")) {
            		
                	responseOfServerWithMovieTitles.append(responseReadLine + "\n");
            	}
 	        }
            
            // Se a resposta do servidor n�o obedecer o protocolo especificado, significa que outro
            // servidor respondeu ao cliente. Ent�o ser� registrado no log a resposta origiral
            // e apresentada ao cliente apenas que a resposta desobedeceu o protocolo.
            
            if(!iMDbCommunicationProtocol.isMatchPatternProtocol(responseOfServerWithMovieTitles.toString())) {
            
            	return Constants.IVALID_MESSAGE_PROTOCOL;
            }
            
            // Retira o protocolo aplicado no servidor para exibi��o puramente dos dados no cliente.
            
            return iMDbCommunicationProtocol.
            		getMessageWithOutPatternProtocolApplied(responseOfServerWithMovieTitles.toString());

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
