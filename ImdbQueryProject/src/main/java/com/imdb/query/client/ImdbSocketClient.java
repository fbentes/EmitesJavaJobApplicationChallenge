/**
 * 
 */
package com.imdb.query.client;

/**
 * @author Fábio Bentes
 *
 */
public interface ImdbSocketClient {
	
	boolean connectToServer(String ipServer, int port);
	
	String sendMovieTitleToSearchInServer(String movieTitle);
	
	boolean stopConnection();
}
