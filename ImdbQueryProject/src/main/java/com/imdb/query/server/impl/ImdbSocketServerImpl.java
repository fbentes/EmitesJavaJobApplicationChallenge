/**
 * 
 */
package com.imdb.query.server.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import com.imdb.query.server.ImdbSocketServer;
import com.imdb.query.util.Constants;

/**
 * @author Fábio Bentes
 *
 */
public class ImdbSocketServerImpl implements ImdbSocketServer {
		
	//final static Log logger = LogFactory.getLog(ImdbSocketServer.class);
	
	private ServerSocket server;
	
	@Override
	public void connect() {
		
		try {
			
			server = new ServerSocket(Constants.PORT);
			
		} catch (IOException e) {
			
			System.out.println("Problema ao conectar na porta " + Constants.PORT + ": " + e.getMessage());
		}
	}
	
	@Override
	public void waitingForClientRequests() {
		
		Socket client;
		
		try {
			
			Scanner input = null;
			
			while(true) {
				
				client = server.accept();
				
				String ipClient = client.getInetAddress().getHostAddress();
				
				System.out.println("Cliente conectado do IP "+ipClient);

				input = new Scanner(client.getInputStream());
	            
				while(input.hasNextLine()){
					
	                System.out.println("Mensagem do cliente " + ipClient + ": "+ input.nextLine());
	            }

				input.close();
			}
			
		} catch (IOException e) {
			
			System.out.println("Problema ao conectar na porta " + Constants.PORT + ": " + e.getMessage());
		
		} 
	}
}
