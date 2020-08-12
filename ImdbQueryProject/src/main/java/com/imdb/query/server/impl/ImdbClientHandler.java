package com.imdb.query.server.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.imdb.query.server.IMDbUrlConnection;
import com.imdb.query.util.Constants;

public class ImdbClientHandler extends Thread {
	
	private Socket clientSocket;
	private IMDbUrlConnection iMDbUrlConnection;
	
    private PrintWriter writeToClientPrintWriter;
    private BufferedReader readFromClientBufferedReader;
    
    public ImdbClientHandler(Socket clientSocket, IMDbUrlConnection iMDbUrlConnection) {
        this.clientSocket = clientSocket;
        this.iMDbUrlConnection = iMDbUrlConnection;
    }
    
    public void run() {
    	
		try {
			
	        writeToClientPrintWriter = new PrintWriter(clientSocket.getOutputStream(), true);
	        
			readFromClientBufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
	        String movieTitle = readFromClientBufferedReader.readLine();

            String moviesFound = iMDbUrlConnection.getMoviesFound(movieTitle);

            writeToClientPrintWriter.println(moviesFound);
			
		} catch (IOException e) {
			
			System.out.println("Problema ao conectar na porta " + Constants.PORT + ": " + e.getMessage());
		} finally {
			
			close();
		}
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
