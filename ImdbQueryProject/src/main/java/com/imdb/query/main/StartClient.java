/**
 * 
 */
package com.imdb.query.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.inject.Inject;

import com.imdb.query.client.IMDbSocketClient;
import com.imdb.query.util.Constants;
import com.imdb.query.util.IMDbQueryModuleInjector;

/**
 * @author F�bio Bentes
 *
 *	Classe respons�vel para iniciar o Client Socket para solicita��es de nomes de filmes para o servidor Socket.
 *
 *  Podem ser pesquisados nomes completos, mas tamb�m iniciais de nomes de filmes para o retorno
 *  dos nomes concatenados com "\n". Nesse caso a pesquisa funcionar� como um like no SQL !
 *  
 */
public class StartClient {

	@Inject
	private IMDbSocketClient imdbSocketClient;
	
	/**
	 * @param args IP do servidor socket caso n�o seja o mesmo host do cliente. Por default � 127.0.0.1. 
	 * 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		String ipServer = Constants.IP_SERVER_DEFAULT;
		
		if(args != null && args.length == 1) {
			ipServer = args[0];
		}
		
		StartClient startClient = new StartClient();
		
		IMDbQueryModuleInjector.initialize(startClient);
       		
		startClient.execute(ipServer);
	}
	
	private void execute(String ipServer) {
		
		try {
			
			do {
				
				System.out.println("Conectando com o servidor...\n");
				
				boolean connected = imdbSocketClient.connectToServer(ipServer, Constants.PORT);
				
				if(!connected) {
					
					System.out.println("");
					break;
				}
	
				String movieTitle = "";
				
				System.out.print("Digite o t�tulo do filme para pesquisa no IMDB: ");
	
		        BufferedReader movieTitleBufferedReader =  
		                new BufferedReader(new InputStreamReader(System.in)); 
		      
				try {
					
					movieTitle = movieTitleBufferedReader.readLine();
					
				} catch (IOException e) {
					
					System.out.println("Problema na leitura do t�tulo do filme: " + e.getMessage());
				} 
			
				if(movieTitle.trim().toLowerCase().equals("exit") ) {
					break;
				}
	
				String responseOfServerWithMovieTitles = imdbSocketClient.sendMovieTitleToSearchInServer(movieTitle);
				
				System.out.println("");
				System.out.println("Resposta:");
				System.out.println(responseOfServerWithMovieTitles);
				System.out.println("");
				
			} while(true);
		
		} finally {
			
			System.out.println("*********************");
			System.out.println("Conex�o do cliente encerrada !");
			
			imdbSocketClient.stopConnection();
		}
	}
}
