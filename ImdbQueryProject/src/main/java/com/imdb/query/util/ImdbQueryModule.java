/**
 * 
 */
package com.imdb.query.util;

import com.google.inject.AbstractModule;
import com.imdb.query.client.ImdbSocketClient;
import com.imdb.query.client.impl.ImdbSocketClientImpl;
import com.imdb.query.server.IMDbUrlConnection;
import com.imdb.query.server.ImdbSocketServer;
import com.imdb.query.server.impl.IMDbUrlConnectionImpl;
import com.imdb.query.server.impl.ImdbSocketServerImpl;

/**
 * @author Fábio Bentes
 *
 */
public class ImdbQueryModule extends AbstractModule {

	@Override
	protected void configure() {
		
		bind(ImdbSocketClient.class).to(ImdbSocketClientImpl.class);
		bind(ImdbSocketServer.class).to(ImdbSocketServerImpl.class);
		bind(IMDbUrlConnection.class).to(IMDbUrlConnectionImpl.class);		
	}
}
