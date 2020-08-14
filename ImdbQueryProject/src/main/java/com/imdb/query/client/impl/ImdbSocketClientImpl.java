/**
 * 
 */
package com.imdb.query.client.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.imdb.query.client.ImdbSocketClient;
import com.imdb.query.util.Constants;
import com.imdb.query.util.protocol.IMDbCommunicationProtocol;
import com.imdb.query.util.protocol.impl.IMDbCommunicationProtocolImpl;

/**
 * @author Fábio Bentes
 *
 */
public class ImdbSocketClientImpl implements ImdbSocketClient {
	
	private Socket clientSocket;
    private BufferedReader readFromServerBufferedReader;
    private PrintWriter writeToServerPrintWriter;
    
    private IMDbCommunicationProtocol iMDbCommunicationProtocol;
    
	public boolean connectToServer(String ipServer, int port) {
		
		try {
			
			clientSocket = new Socket(ipServer,port);
			
			iMDbCommunicationProtocol = new IMDbCommunicationProtocolImpl();
			
			return true;
			
		} catch (IOException e) {
			
			System.out.println("");
			
			if(e.getMessage().trim().toLowerCase().equals("connection refused: connect")) {
				
				System.out.println("A conexão do cliente foi recusada porque o servidor não responde !");
			} else {
				
				System.out.println(e.getMessage());
			}
			
			return false;
		}
	}
	
	private boolean isMatchPatternProtocol(String movieTitle) {
						
		iMDbCommunicationProtocol.setMovieTitleWithPatternProtocol(Constants.PREFIX_PROTOCOL + movieTitle + Constants.SUFIX_PROTOCOL);
		
		return iMDbCommunicationProtocol.isMatchPatternProtocol();	
	}
	
	public String sendMovieTitleToSearchInServer(String movieTitle) {

		try {

			readFromServerBufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			writeToServerPrintWriter = new PrintWriter(clientSocket.getOutputStream(), true);

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		try {

			if(!isMatchPatternProtocol(movieTitle)) {
				
				return Constants.IVALID_MESSAGE_PROTOCOL;
			}
			
			writeToServerPrintWriter.println(iMDbCommunicationProtocol.getMovieTitle());	
			
			String responseOfServerWithMovieTitles = "";
			
			String responseReadLine;
			
            while ((responseReadLine = readFromServerBufferedReader.readLine()) != null) {
            	
            	responseOfServerWithMovieTitles += responseReadLine + "\n";
 	        }

	        return responseOfServerWithMovieTitles;

		} catch (IOException e) {

			return e.getMessage();
		}
   }
	
	public boolean stopConnection() {
		
        try {
			
        	readFromServerBufferedReader.close();
			
		} catch (IOException e) {
			
			System.out.println(e.getMessage());
			return false;
		}
        
        writeToServerPrintWriter.close();
        
        try {
			
        	clientSocket.close();
        	
		} catch (IOException e) {
			
			System.out.println(e.getMessage());
			return false;
		}
        
        return true;
    }	
}
