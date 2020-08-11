/**
 * 
 */
package com.imdb.query.server.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javax.inject.Inject;

import org.apache.commons.math3.exception.NullArgumentException;

import com.imdb.query.server.IMDbUrlConnection;
import com.imdb.query.server.ImdbSocketServer;
import com.imdb.query.util.Constants;

/**
 * @author Fábio Bentes
 *
 */
public class ImdbSocketServerImpl implements ImdbSocketServer {
		
	//final static Log logger = LogFactory.getLog(ImdbSocketServer.class);
	
	private ServerSocket serverSocket;
	private PrintWriter out;
	
	@Inject
	private IMDbUrlConnection iMDbUrlConnection;
	
	@Override
	public void connect(int port) {
		
		try {
			
			System.out.println("Conectando no servidor na porta " + port);
			serverSocket = new ServerSocket(port);
			
			System.out.println("Carregando a lista de filmes ...");
			loadMovieLlistFromImdb();
			
			System.out.println("Esperando requisição do cliente ...");
			waitingForClientRequests();
			
		} catch (IOException e) {
			
			System.out.println("Problema ao conectar na porta " + Constants.PORT + ": " + e.getMessage());
		}
	}
	
	private void waitingForClientRequests() throws IOException {
		
		 while (true) {
			 
			 Socket clientSocket = serverSocket.accept();
			 
			 ImdbClientHandler imdbClientHandler = new ImdbClientHandler(clientSocket, iMDbUrlConnection);
			 
			 imdbClientHandler.start();
		 }
	}
	
	public void loadMovieLlistFromImdb() {
		
		if(serverSocket == null) {
			throw new RuntimeException("O método connect() deve ser chamado !");
		}
		
		iMDbUrlConnection.loadMovieLlistFromImdb();
	}
	
	private static class ImdbClientHandler extends Thread {
	
		private Socket clientSocket;
		private IMDbUrlConnection iMDbUrlConnection;
		
        private PrintWriter out;

	    public ImdbClientHandler(Socket clientSocket, IMDbUrlConnection iMDbUrlConnection) {
	        this.clientSocket = clientSocket;
	        this.iMDbUrlConnection = iMDbUrlConnection;
	    }
	    
	    public void run() {
	    	
			try {
				
				Scanner input = null;
				
				while(true) {
					
					String ipClient = clientSocket.getInetAddress().getHostAddress();
					
					System.out.println("Cliente conectado do IP "+ipClient);

					input = new Scanner(clientSocket.getInputStream());
		            
					while(input.hasNextLine()){
						
						String movieTitle = input.nextLine();
						
		                System.out.println("Buscando filmes contendo "+ movieTitle + " ...");
		                
		                String moviesFound = iMDbUrlConnection.getMovieData(movieTitle);
		                
		                out = new PrintWriter(clientSocket.getOutputStream(), true);

		                out.println("Filmes encontrados = " + moviesFound);
		            }	

					input.close();
				}
				
			} catch (IOException e) {
				
				System.out.println("Problema ao conectar na porta " + Constants.PORT + ": " + e.getMessage());
			} 

	    }
	}

	/*
	@Override
	public void waitingForClientRequests() {
		
		Socket clientSocket;
		
		try {
			
			Scanner input = null;
			
			while(true) {
				
				clientSocket = serverSocket.accept();
				
				String ipClient = clientSocket.getInetAddress().getHostAddress();
				
				System.out.println("Cliente conectado do IP "+ipClient);

				input = new Scanner(clientSocket.getInputStream());
	            
				while(input.hasNextLine()){
					
					String movieTitle = input.nextLine();
					
	                System.out.println("Buscando filmes contendo "+ movieTitle + " ...");
	                
	                String moviesFound = iMDbUrlConnection.getMovieData(movieTitle);
	                
	                out = new PrintWriter(clientSocket.getOutputStream(), true);

	                out.println("Filmes encontrados = " + moviesFound);
	            }	

				input.close();
			}
			
		} catch (IOException e) {
			
			System.out.println("Problema ao conectar na porta " + Constants.PORT + ": " + e.getMessage());
		} 
	}
	*/
}
