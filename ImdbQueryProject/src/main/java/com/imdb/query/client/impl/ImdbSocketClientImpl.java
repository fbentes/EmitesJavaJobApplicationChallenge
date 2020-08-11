/**
 * 
 */
package com.imdb.query.client.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.imdb.query.client.ImdbSocketClient;

/**
 * @author Fábio Bentes
 *
 */
public class ImdbSocketClientImpl implements ImdbSocketClient {
	
	private Socket clientSocket;
    private BufferedReader in;
    
	@Override
	public void connectToServer(String ipServer, int port) {
		
		try {

			clientSocket = new Socket(ipServer,port);

			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			inputMovieTitle();

		} catch (UnknownHostException e) {
			
			System.out.println(e.getMessage());
			
		} catch (IOException e) {

			System.out.println(e.getMessage());
		}
	}
	
	private void inputMovieTitle() {
		
		System.out.println("");
		System.out.print("Digite o título do filme para pesquisa no IMDB: ");

        BufferedReader reader =  
                new BufferedReader(new InputStreamReader(System.in)); 
      
        String movieTitle = " ";
        
		while(!movieTitle.trim().toLowerCase().equals("sair")) {
		
			try {
				movieTitle = reader.readLine();
			} catch (IOException e) {
				
				System.out.println("Problema na leitura do título do filme: " + e.getMessage());
			} 
			
			String response = sendMovieTitleToSearchInServer(movieTitle);
			
			System.out.println("");
			System.out.println("Resposta: \n" + response);
		}
	}
	
	private String sendMovieTitleToSearchInServer(String title) {
		
		PrintStream output;
		
		try {

			output = new PrintStream(clientSocket.getOutputStream());
	        
			output.println(title);	
			
			String response = in.readLine();
			
			return response;

		} catch (IOException e) {

			return e.getMessage();
		}
   }
}
