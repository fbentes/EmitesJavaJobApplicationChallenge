/**
 * 
 */
package com.imdb.query.server;

/**
 * @author F�bio Bentes
 *
 */
public interface ImdbSocketServer {
		
	boolean connect(int port);
	
	void waitingForClientRequests();
	
	int loadMovieLlistFromImdb();
	
	void stop();
}
