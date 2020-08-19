package com.imdb.query.client.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import com.imdb.query.client.IMDbClientSocket;
import com.imdb.query.server.IMDbServerSocket;
import com.imdb.query.server.ServerCommand;
import com.imdb.query.server.impl.ServerCommandImpl;
import com.imdb.query.util.Constants;
import com.imdb.query.util.IMDbQueryModuleInjector;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ImdbClientSocketImplTest {
    
	@Inject
	private IMDbClientSocket imdbSocketClient;
	
	@Inject
	private IMDbServerSocket imdbSocketServer;

	@BeforeAll
	public void initializeTests() throws InterruptedException {

		// Injetando dependências ...
		
		IMDbQueryModuleInjector.initialize(this);
        
		System.out.println("************ PREPARA SERVIDOR PARA CLIENTE ************");
		System.out.println(Constants.STRING_EMPTY);
		
		Thread thread = new Thread(new Runnable() {
		    public void run() {

		    	ServerCommand serverCommand = new ServerCommandImpl();
		    	
		    	imdbSocketServer.setPort(Constants.PORT_DEFAULT);
		    	
		    	serverCommand.execute(imdbSocketServer);
		    }
		  });
		
        thread.start();

        // Espera 4 segundos para dar tempo de carregar a lista de filmes do site IMDb antes de prosseguir a execução principal.
	    thread.join(4000);  

		System.out.println(Constants.STRING_EMPTY);			
		System.out.println("************ INICIA CLIENTE PARA SOLICITAÇÃO NO SERVIDOR ************");
		System.out.println(Constants.STRING_EMPTY);			
	}
	
	@Test
    @Order(1)
	public void connectToServerTest() {
		
		boolean connected = imdbSocketClient.connectToServer(Constants.IP_SERVER_DEFAULT, imdbSocketServer.getAlternativePort());
		
		assertTrue(connected);
		
		if(connected) {
			System.out.println("Cliente conectado com o servidor !");
		} else {
			System.out.println("Cliente não conectado com o servidor !");			
		}
	}
	
	@Test
    @Order(2)
	public void sendMovieTitleToSearchInServer() {

		Random random = new Random();
		
		String[] movieArrayTest = 
			{"...E o Vento Levou, ",
	         "12 Anos", 
	         "12 Homens", 
	         "1917, 2001: Uma Odisséia no Espaço", 
	         "3 ", 
	         "A Batalha ", 
	         "A Caça", 
	         "A Chantagem", 
	         "A Criada", 
	         "A Felicidade", 
	         "A General", 
	         "A Lista ", 
	         "A Malvada", 
	         "A Mulher Faz o Homem", 
	         "A Origem", 
	         "A Separação", 
	         "Alien", 
	         "Antes" , 
	         "Batman", 
	         "Um sonho"};
		
		String movieTitleExample = movieArrayTest[random.nextInt(movieArrayTest.length)];

		System.out.println("Filme pesquisado: " + movieTitleExample);
		
		String sent = imdbSocketClient.sendMovieTitleToSearchInServer(movieTitleExample);

		boolean movieTitleFound = !sent.trim().equals(Constants.STRING_EMPTY);

		assertTrue(movieTitleFound);
		
		if(movieTitleFound) {			
			System.out.println("Filme(s) encontrado(s): " + sent);
		} else {
			System.out.println("Nenhum filme foi encontrado !");			
		}
	}
	
	@AfterAll
	public void stopConnectionTest() {
		
		boolean isClientStopped = imdbSocketClient.stopConnection();
		
		imdbSocketServer.requestStop();
		
		boolean isServerStopped = imdbSocketServer.isRequestedStoped();
		
		assertTrue(isClientStopped && isServerStopped);
		
		if(isClientStopped) {
			System.out.println("Conexão do cliente encerrada !");
		} else {
			System.out.println("Conexão do cliente não encerrada !!!");
		}
		
		if(isServerStopped) {		
			System.out.println("Servidor parado !");	
		} else {
			System.out.println("Servidor não parado !");
		}
	}
}
