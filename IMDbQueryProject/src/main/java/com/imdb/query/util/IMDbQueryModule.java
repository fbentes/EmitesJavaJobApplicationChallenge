package com.imdb.query.util;

import com.google.inject.AbstractModule;
import com.imdb.query.client.IMDbClientSocket;
import com.imdb.query.client.impl.IMDbClientSocketImpl;
import com.imdb.query.server.IMDbServerSocket;
import com.imdb.query.server.IMDbUrlConnection;
import com.imdb.query.server.impl.IMDbServerSocketImpl;
import com.imdb.query.server.impl.IMDbUrlConnectionImpl;

/**
 * Injeta as depend�ncias necess�rias para a solu��o.
 * 
 * @author F�bio Bentes
 * @version 1.0.0.0
 * @since 11/08/2020
 * 
 */
public class IMDbQueryModule extends AbstractModule {

	@Override
	protected void configure() {
		
		bind(IMDbClientSocket.class).to(IMDbClientSocketImpl.class);
		bind(IMDbServerSocket.class).to(IMDbServerSocketImpl.class);
		bind(IMDbUrlConnection.class).to(IMDbUrlConnectionImpl.class);		
	}
}
