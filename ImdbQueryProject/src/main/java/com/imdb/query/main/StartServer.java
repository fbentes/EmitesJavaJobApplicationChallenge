/**
 * 
 */
package com.imdb.query.main;

import javax.inject.Inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.imdb.query.server.ImdbSocketServer;
import com.imdb.query.server.ServerCommand;
import com.imdb.query.server.impl.ServerCommandImpl;
import com.imdb.query.util.ImdbQueryModule;
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
		
		Module[] module = new ImdbQueryModule[] { new ImdbQueryModule()};
        Injector injector = Guice.createInjector(module);
        injector.injectMembers(startServer);
       
        ServerCommand serverCommand = new ServerCommandImpl();
        serverCommand.setImdbSocketServer(startServer.imdbSocketServer);
        serverCommand.execute();
	}
}
