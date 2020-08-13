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

/**
 * @author Fábio Bentes
 *
 */
public class ImdbSocketClientImpl implements ImdbSocketClient {
	
	private Socket clientSocket;
    private BufferedReader readFromServerBufferedReader;
    private PrintWriter writeToServerPrintWriter;
    
	public boolean connectToServer(String ipServer, int port) {
		
		try {
			
			clientSocket = new Socket(ipServer,port);
			
			return true;
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
			
			return false;
		}
	}
	
	public String sendMovieTitleToSearchInServer(String movieTitle) {
				
		try {
		
			readFromServerBufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	        
			writeToServerPrintWriter = new PrintWriter(clientSocket.getOutputStream(), true);

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		try {

			writeToServerPrintWriter.println(movieTitle);	
			
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
