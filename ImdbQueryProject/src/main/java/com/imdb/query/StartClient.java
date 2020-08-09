/**
 * 
 */
package com.imdb.query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.imdb.query.client.ImdbSocketClient;
import javax.inject.Inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
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
		
		String ipServerDefault = "127.0.0.1";
		
		if(args != null && args.length == 1) {
			ipServerDefault = args[0];
		}
		
		StartClient startClient = new StartClient();
		
		Module module = new ImdbQueryModule();
        Injector injector = Guice.createInjector(module);
        injector.injectMembers(startClient);
       		
		startClient.execute(ipServerDefault);
	}
	
	private void execute(String ipServer) {
		
		imdbSocketClient.connectToServer(ipServer);
        
		System.out.println("");
		System.out.print("Digite o título do filme para pesquisa no IMDB: ");

        BufferedReader reader =  
                new BufferedReader(new InputStreamReader(System.in)); 
      
        String movieTitle = null;
        
		while(!movieTitle.trim().toLowerCase().equals("sair")) {
		
			try {
				movieTitle = reader.readLine();
			} catch (IOException e) {
				
				System.out.println("Problema na leitura do título do filme: " + e.getMessage());
			} 
			
			imdbSocketClient.sendMovieTitleToSearchInServer(movieTitle);
		}
	}

}
