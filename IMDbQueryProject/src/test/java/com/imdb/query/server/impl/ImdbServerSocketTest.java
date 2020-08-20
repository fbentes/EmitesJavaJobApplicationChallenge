package com.imdb.query.server.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.imdb.query.server.IMDbServerSocket;
import com.imdb.query.util.Constants;
import com.imdb.query.util.IMDbQueryModule;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ImdbServerSocketTest {

    private static final Logger logger = LogManager.getLogger("ImdbServerSocketTest");

	@Inject
	private IMDbServerSocket imdbSocketServer;
	
	@BeforeAll
	public void initializeTests() {

		Module module = new IMDbQueryModule();
        Injector injector = Guice.createInjector(module);
        injector.injectMembers(this);
	}
	
	@Test
	@Order(1)
	public void connectTest() {
		
		imdbSocketServer.setPort(Constants.PORT_DEFAULT);
		
		boolean connected = imdbSocketServer.connectToServerSocket();
		
		assertTrue(connected);
		
		logger.info("Servidor conectado !");
	}
	
	@Test
	@Order(2)
	public void connectToPortBusyTest() {
		
		imdbSocketServer.setPort(135);  // Port RPC
		
		boolean connected = imdbSocketServer.connectToServerSocket();
		
		boolean isConnectedDifferentPortOrigin = 
				imdbSocketServer.getPort() != imdbSocketServer.getAlternativePort();
		
		assertTrue(connected && isConnectedDifferentPortOrigin);
		
		logger.info("Servidor conectado !");
	}
	
	@Test
	@Order(3)
	public void loadMovieLlistFromImdbTest() {
		
		int movieQuantity = imdbSocketServer.loadMovieLlistFromImdb();
		
		assertTrue(movieQuantity > 0);
		
		logger.info("Quantidade de filmes = " + movieQuantity);
	}
	
	@Test
	@Order(4)
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

		  logger.info("Espera pelo cliente bem sucedida !");
	}

	@AfterAll
	public void isStoped() {
		
		imdbSocketServer.requestStopExecution();
		
		boolean isStopped = imdbSocketServer.isExecutionRequestedStoped();
		
		assertTrue(isStopped);
		
		logger.info("Servidor finalizado !");
	}
}
