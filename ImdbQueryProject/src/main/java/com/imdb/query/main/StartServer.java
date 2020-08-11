/**
 * 
 */
package com.imdb.query.main;

import com.imdb.query.server.ImdbSocketServer;
import com.imdb.query.util.Constants;
import com.imdb.query.util.ImdbQueryModule;

import javax.inject.Inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
/**
 * @author Fábio Bentes
 *
 */
public class StartServer {

	@Inject
	private ImdbSocketServer imdbSocketServer;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		StartServer startServer = new StartServer();
		
		Module module = new ImdbQueryModule();
        Injector injector = Guice.createInjector(module);
        injector.injectMembers(startServer);
       
        startServer.execute();
	}
	
	private void execute() {
		
		imdbSocketServer.connect(Constants.PORT);
		
	}
}
