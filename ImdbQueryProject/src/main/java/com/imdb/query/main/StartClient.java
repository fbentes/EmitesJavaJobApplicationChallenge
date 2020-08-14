/**
 * 
 */
package com.imdb.query.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.inject.Inject;

import com.imdb.query.client.ImdbSocketClient;
import com.imdb.query.util.Constants;
import com.imdb.query.util.IMDbQueryModuleInjector;
/**
 * @author Fábio Bentes
 *
 *	Classe responsável para iniciar o Client Socket para solicitações de nomes de filmes com o servidor Socket.
 *
 *  Podem ser pesquisados nomes completos, mas também iniciais de nomes de filmes para o retorno
 *  dos nomes concatenados com "\n". Nesse caso a pesquisa no funcionará como um like no SQL.
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
		
		String ipServer = Constants.IP_SERVER;
		
		if(args != null && args.length == 1) {
			ipServer = args[0];
		}
		
		StartClient startClient = new StartClient();
		
		IMDbQueryModuleInjector.initialize(startClient);
       		
		startClient.execute(ipServer);
	}
	
	private void execute(String ipServer) {
		
		do {
			
			System.out.println("Conectando com o servidor...\n");
			
			boolean connected = imdbSocketClient.connectToServer(ipServer, Constants.PORT);
			
			if(!connected) {
				
				System.out.println("");
				break;
			}

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
		
		System.out.println("*********************");
		System.out.println("Conexão do cliente encerrada !");
		
		imdbSocketClient.stopConnection();
	}
}
