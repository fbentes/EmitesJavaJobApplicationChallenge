/**
 * 
 */
package com.imdb.query.server;

/**
 * @author Fábio Bentes
 *
 */
public interface ImdbSocketServer {
		
	void connect();
	
	void waitingForClientRequests();
}
