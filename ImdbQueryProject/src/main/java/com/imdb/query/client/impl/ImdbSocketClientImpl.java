/**
 * 
 */
package com.imdb.query.client.impl;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.imdb.query.client.ImdbSocketClient;
import com.imdb.query.util.Constants;

/**
 * @author Fábio Bentes
 *
 */
public class ImdbSocketClientImpl implements ImdbSocketClient {
	
	private Socket client;
	
	@Override
	public void connectToServer(String ipServer) {
		
		try {
			
			client = new Socket(ipServer,Constants.PORT);
			
		} catch (UnknownHostException e) {
			
			System.out.println(e.getMessage());
			
		} catch (IOException e) {

			System.out.println(e.getMessage());
		}
	}
	
	@Override
	public void sendMovieTitleToSearchInServer(String title) {
		
		PrintStream output;
		
		try {

			output = new PrintStream(client.getOutputStream());
	        
			output.println(title);		

		} catch (IOException e) {

			System.out.println(e.getMessage());
		}
   }
}
