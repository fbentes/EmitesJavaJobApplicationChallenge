/**
 * 
 */
package com.imdb.query.client;

/**
 * @author Fábio Bentes
 *
 */
public interface ImdbSocketClient {
	
	void connectToServer(String ipServer);
	
	void sendMovieTitleToSearchInServer(String title);
}
