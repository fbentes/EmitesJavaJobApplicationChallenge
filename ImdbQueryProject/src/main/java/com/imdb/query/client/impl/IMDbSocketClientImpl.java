/**
 * 
 */
package com.imdb.query.client.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.imdb.query.client.IMDbSocketClient;
import com.imdb.query.util.Constants;
import com.imdb.query.util.protocol.IMDbCommunicationProtocol;
import com.imdb.query.util.protocol.impl.IMDbCommunicationProtocolImpl;

/**
 * Respons�vel pela comunica��o com o servidor Socket.
 *
 * @author F�bio Bentes
 * @version 1.0.0.0
 * @since 09/08/2020
 * 
 */
public class IMDbSocketClientImpl implements IMDbSocketClient {
	
	private Socket clientSocket;
    private BufferedReader readFromServerBufferedReader;
    private PrintWriter writeToServerPrintWriter;
    
    private IMDbCommunicationProtocol iMDbCommunicationProtocol;
    
    @Override
	public boolean connectToServer(String ipServer, int port) {
		
		try {
			
			clientSocket = new Socket(ipServer,port);
			
			iMDbCommunicationProtocol = new IMDbCommunicationProtocolImpl();
			
			return true;
			
		} catch (IOException e) {
			
			System.out.println("");
			
			if(e.getMessage().trim().toLowerCase().equals("connection refused: connect")) {
				
				System.out.println("A conex�o do cliente foi recusada porque o servidor n�o responde !");
			} else {
				
				System.out.println(e.getMessage());
			}
			
			return false;
		}
	}
	
	/**
	 * Aplica o protocolo de comunica��o entre o cliente e servidor.
	 * 
	 * @param movieTitle O t�tulo de um filme a ser pesquisado pelo usu�rio.
	 * @return Verdadeiro se o padr�o de protocolo aplicado for v�lido.
	 */
	private boolean isAppliedProtocol(String movieTitle) {
						
		iMDbCommunicationProtocol.setMovieTitleWithPatternProtocol(Constants.PREFIX_PROTOCOL + movieTitle + Constants.SUFIX_PROTOCOL);
		
		return iMDbCommunicationProtocol.isMatchPatternProtocol();	
	}
	
    @Override
	public String sendMovieTitleToSearchInServer(String movieTitle) {

		try {

			readFromServerBufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			writeToServerPrintWriter = new PrintWriter(clientSocket.getOutputStream(), true);

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		try {

			if(!isAppliedProtocol(movieTitle)) {
				
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
	
    @Override
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
