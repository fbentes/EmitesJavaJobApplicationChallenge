/**
 * 
 */
package com.imdb.query.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.imdb.query.client.ImdbSocketClient;
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
public class StartClient {

	@Inject
	private ImdbSocketClient imdbSocketClient;
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		String ipServer = "127.0.0.1";
		
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
		
		imdbSocketClient.connectToServer(ipServer, Constants.PORT);
	}
}
