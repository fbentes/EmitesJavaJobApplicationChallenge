/**
 * 
 */
package com.imdb.query.client;

/**
 * @author F�bio Bentes
 *
 */
public interface ImdbSocketClient {
	
	void connectToServer(String ipServer);
	
	void sendMovieTitleToSearchInServer(String title);
}
