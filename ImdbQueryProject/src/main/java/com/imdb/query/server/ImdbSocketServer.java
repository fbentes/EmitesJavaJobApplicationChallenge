/**
 * 
 */
package com.imdb.query.server;

/**
 * @author Fábio Bentes
 *
 */
public interface ImdbSocketServer {
		
	boolean connect(int port);
	
	void waitingForClientRequests();
	
	int loadMovieLlistFromImdb();
	
	void stop();
}
