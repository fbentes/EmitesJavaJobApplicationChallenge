/**
 * 
 */
package com.imdb.query.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.inject.Inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.imdb.query.client.ImdbSocketClient;
import com.imdb.query.util.Constants;
import com.imdb.query.util.ImdbQueryModule;
/**
 * @author Fábio Bentes
 *
 */
public class StartClient {

	@Inject
	private ImdbSocketClient imdbSocketClient;
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		String ipServer = Constants.LOCAL_HOST;
		
		if(args != null && args.length == 1) {
			ipServer = args[0];
		}
		
		StartClient startClient = new StartClient();
		
		Module module = new ImdbQueryModule();
        Injector injector = Guice.createInjector(module);
        injector.injectMembers(startClient);
       		
		startClient.execute(ipServer);
	}
	
	private void execute(String ipServer) {
		
		do {
			
			System.out.println("Conectando com o servidor...\n");
			
			imdbSocketClient.connectToServer(ipServer, Constants.PORT);

			String movieTitle = "";
			
			System.out.print("Digite o título do filme para pesquisa no IMDB: ");

	        BufferedReader movieTitleBufferedReader =  
	                new BufferedReader(new InputStreamReader(System.in)); 
	      
			try {
				
				movieTitle = movieTitleBufferedReader.readLine();
				
			} catch (IOException e) {
				
				System.out.println("Problema na leitura do título do filme: " + e.getMessage());
			} 
		
			if(movieTitle.equals("sair") ) {
				break;
			}

			String responseOfServerWithMovieTitles = imdbSocketClient.sendMovieTitleToSearchInServer(movieTitle);
			
			System.out.println("");
			System.out.println("Resposta:");
			System.out.println(responseOfServerWithMovieTitles);
			System.out.println("");
			
		} while(true);
		
		System.out.println("************");
		System.out.println("Finalizado !");
		
		imdbSocketClient.stopConnection();
	}
}
