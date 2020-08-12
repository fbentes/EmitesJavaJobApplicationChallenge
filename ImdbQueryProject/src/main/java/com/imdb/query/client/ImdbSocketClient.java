/**
 * 
 */
package com.imdb.query.client;

/**
 * @author F�bio Bentes
 *
 */
public interface ImdbSocketClient {
	
	boolean connectToServer(String ipServer, int port);
	
	String keyBoardInputMovieTitle();
	
	String sendMovieTitleToSearchInServer(String movieTitle);
	
	boolean stopConnection();
}
