package com.imdb.query.util;

import com.google.inject.AbstractModule;
import com.imdb.query.client.IMDbSocketClient;
import com.imdb.query.client.impl.IMDbSocketClientImpl;
import com.imdb.query.server.IMDbSocketServer;
import com.imdb.query.server.IMDbUrlConnection;
import com.imdb.query.server.impl.IMDbSocketServerImpl;
import com.imdb.query.server.impl.IMDbUrlConnectionImpl;

/**
 * Injeta as dependências necessárias para a solução.
 * 
 * @author Fábio Bentes
 * @version 1.0.0.0
 * @since 11/08/2020
 * 
 */
public class IMDbQueryModule extends AbstractModule {

	@Override
	protected void configure() {
		
		bind(IMDbSocketClient.class).to(IMDbSocketClientImpl.class);
		bind(IMDbSocketServer.class).to(IMDbSocketServerImpl.class);
		bind(IMDbUrlConnection.class).to(IMDbUrlConnectionImpl.class);		
	}
}
