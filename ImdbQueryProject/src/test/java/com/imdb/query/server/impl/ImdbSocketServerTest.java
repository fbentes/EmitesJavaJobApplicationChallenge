package com.imdb.query.server.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.imdb.query.server.IMDbSocketServer;
import com.imdb.query.util.Constants;
import com.imdb.query.util.IMDbQueryModule;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ImdbSocketServerTest {

	@Inject
	private IMDbSocketServer imdbSocketServer;
	
	@BeforeAll
	public void initializeTests() {

		Module module = new IMDbQueryModule();
        Injector injector = Guice.createInjector(module);
        injector.injectMembers(this);
	}
	
	@Test
	@Order(1)
	public void connectTest() {
		
		boolean connected = imdbSocketServer.connect(Constants.PORT);
		
		assertTrue(connected);
		
		System.out.println("Servidor conectado !");
	}
	
	@Test
	@Order(2)
	public void loadMovieLlistFromImdbTest() {
		
		int movieQuantity = imdbSocketServer.loadMovieLlistFromImdb();
		
		assertTrue(movieQuantity > 0);
		
		System.out.println("Quantidade de filmes = " + movieQuantity);
	}
	
	@Test
	@Order(3)
	public void waitingForClientRequests() throws InterruptedException {
			
		Thread thread = new Thread(new Runnable() {
		    public void run() {
		    	imdbSocketServer.waitingForClientRequests();
		    }
		  });
		
		  thread.start();
		 
		  thread.join(5000);
		  
		  assertTrue(thread.isAlive());
		  
		  thread.interrupt();

		  System.out.println("Espera pelo cliente bem sucedida !");
	}

	@AfterAll
	public void isStoped() {
		
		imdbSocketServer.stop();
		
		boolean isStopped = imdbSocketServer.isStoped();
		
		assertTrue(isStopped);
		
		System.out.println("Servidor finalizado !");
	}
}
