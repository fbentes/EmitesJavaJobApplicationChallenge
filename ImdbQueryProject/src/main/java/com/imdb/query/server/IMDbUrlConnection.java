/**
 * 
 */
package com.imdb.query.server;

/**
 * @author Fábio Bentes
 *
 */
public interface IMDbUrlConnection {

	String getMoviesFound(String movieTitle);
	
	int loadMovieLlistFromImdbUrl();
}
