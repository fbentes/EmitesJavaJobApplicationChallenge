/**
 * 
 */
package com.imdb.query.util;

import com.google.inject.AbstractModule;
import com.imdb.query.client.IMDbSocketClient;
import com.imdb.query.client.impl.IMDbSocketClientImpl;
import com.imdb.query.server.IMDbUrlConnection;
import com.imdb.query.server.IMDbSocketServer;
import com.imdb.query.server.impl.IMDbUrlConnectionImpl;
import com.imdb.query.server.impl.IMDbSocketServerImpl;
import com.imdb.query.util.protocol.IMDbCommunicationProtocol;
import com.imdb.query.util.protocol.impl.IMDbCommunicationProtocolImpl;

/**
 * @author Fábio Bentes
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
