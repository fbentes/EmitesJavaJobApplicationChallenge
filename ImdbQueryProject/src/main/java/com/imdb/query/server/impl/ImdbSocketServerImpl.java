/**
 * 
 */
package com.imdb.query.server.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.inject.Inject;

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
	
	private boolean isExecuting;
	
	public boolean isStoped() {
		
		return !isExecuting;
	}
	
	@Inject
	private IMDbUrlConnection iMDbUrlConnection;
	
	public boolean connect(int port) {
		
		try {
			
			serverSocket = new ServerSocket(port);
			
			isExecuting = true;
			
		} catch (IOException e) {
			
			System.out.println("Problema ao conectar na porta " + Constants.PORT + ": " + e.getMessage());
			
			isExecuting = false;
		}
		
		return isExecuting;
	}
	
	public int loadMovieLlistFromImdb() {
		
		if(!isExecuting) {
			System.out.println("O método connect() deve ser chamado !");
			return -1;
		}
		
		return iMDbUrlConnection.loadMovieLlistFromImdb();
	}
	
	public void waitingForClientRequests() {
		
		 while (isExecuting) {
			 
			 Socket clientSocket = null;

			 try {
				clientSocket = serverSocket.accept();
			} catch (IOException e) {
				
				System.out.println("Problema ao conectar no servidor: " + e.getMessage());
			}
			 
			 ImdbClientHandler imdbClientHandler = new ImdbClientHandler(clientSocket, iMDbUrlConnection);
			 
			 imdbClientHandler.start();
		 }

		 close();
	}
	
	private boolean close() {
		
		 try {
				
			 serverSocket.close();
			 
			 return true;
			
		} catch (IOException e) {
			
			System.out.println(e.getMessage());
		}
		 
		 return false;
	}
	
	public void stop() {
		
		isExecuting = false;
    }
}
