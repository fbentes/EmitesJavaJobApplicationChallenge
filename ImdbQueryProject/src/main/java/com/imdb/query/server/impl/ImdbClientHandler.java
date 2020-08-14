package com.imdb.query.server.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.imdb.query.server.IMDbUrlConnection;
import com.imdb.query.util.Constants;
import com.imdb.query.util.protocol.IMDbCommunicationProtocol;
import com.imdb.query.util.protocol.impl.IMDbCommunicationProtocolImpl;

public class ImdbClientHandler extends Thread {
	
	private Socket clientSocket;
	private IMDbUrlConnection iMDbUrlConnection;
	
    private PrintWriter writeToClientPrintWriter;
    private BufferedReader readFromClientBufferedReader;
    
    private IMDbCommunicationProtocol iMDbCommunicationProtocol;
    
    public ImdbClientHandler(Socket clientSocket, IMDbUrlConnection iMDbUrlConnection) {
    	
        this.clientSocket = clientSocket;
        this.iMDbUrlConnection = iMDbUrlConnection;

		iMDbCommunicationProtocol = new IMDbCommunicationProtocolImpl();
    }
    
    public void run() {
    	
		try {
			
	        writeToClientPrintWriter = new PrintWriter(clientSocket.getOutputStream(), true);
	        
			readFromClientBufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
	        String movieTitle = readFromClientBufferedReader.readLine();
	        
	        if(!isMatchPatternProtocol(movieTitle)) {
	        	
	            writeToClientPrintWriter.println(Constants.IVALID_MESSAGE_PROTOCOL);
	        }

            String moviesFound = iMDbUrlConnection.getMoviesFound(iMDbCommunicationProtocol.getMovieTitleWithOutPatternProtocol());

            writeToClientPrintWriter.println(moviesFound);
			
		} catch (IOException e) {
			
			System.out.println("Problema ao conectar na porta " + Constants.PORT + ": " + e.getMessage());
		} finally {
			
			close();
		}
    }

	private boolean isMatchPatternProtocol(String movieTitle) {
		
		iMDbCommunicationProtocol.setMovieTitleWithPatternProtocol(movieTitle);
		
		return iMDbCommunicationProtocol.isMatchPatternProtocol();	
	}
	
    public void close() {
        try {
			readFromClientBufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        writeToClientPrintWriter.close();

        try {
			clientSocket.close();
		} catch (IOException e) {
		}
    }
}
