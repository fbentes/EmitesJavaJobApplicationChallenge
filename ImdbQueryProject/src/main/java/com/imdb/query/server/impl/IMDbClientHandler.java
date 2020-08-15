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

/**
 * Handler responsável pelos atendimentos das requisições dos clientes.
 *
 * @author Fábio Bentes
 * @version 1.0.0.0
 * @since 09/08/2020
 * 
 */
public class IMDbClientHandler extends Thread {
	
	private Socket clientSocket;
	private IMDbUrlConnection iMDbUrlConnection;
	
    private PrintWriter writeToClientPrintWriter;
    private BufferedReader readFromClientBufferedReader;
    
    private IMDbCommunicationProtocol iMDbCommunicationProtocol;
    
    public IMDbClientHandler(Socket clientSocket, IMDbUrlConnection iMDbUrlConnection) {
    	
        this.clientSocket = clientSocket;
        this.iMDbUrlConnection = iMDbUrlConnection;

		iMDbCommunicationProtocol = new IMDbCommunicationProtocolImpl();
    }
    
    public void run() {
    	
		try {
			
	        writeToClientPrintWriter = new PrintWriter(clientSocket.getOutputStream(), true);
	        
			readFromClientBufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			// Recebe o nome, ou parte inicial do nome, do filme digitado pelo usuário.
			
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

    /**
     * Checa se o protocolo de comunicação está sendo atendido.
     *  
     * @param movieTitle Nome, ou parte inicial do nome, do filme digitado pelo usuário.
     * @return Verdadeiro caso o cliente tenha aplicado corretamente o protocolo exigido. Falso caso contrário.
     */
	private boolean isMatchPatternProtocol(String movieTitle) {
		
		iMDbCommunicationProtocol.setMovieTitleWithPatternProtocol(movieTitle);
		
		return iMDbCommunicationProtocol.isMatchPatternProtocol();	
	}
	
    public void close() {
    	
        try {
			readFromClientBufferedReader.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
        
        writeToClientPrintWriter.close();

        try {
			clientSocket.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
    }
}
