/**
 * 
 */
package com.imdb.query.server;

/**
 * @author F�bio Bentes
 *
 */
public interface ImdbSocketServer {
		
	void connect();
	
	void waitingForClientRequests();
}
