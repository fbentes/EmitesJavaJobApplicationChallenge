/**
 * 
 */
package com.imdb.query.server;

/**
 * @author F�bio Bentes
 *
 */
public interface IMDbUrlConnection {

	String getMoviesFound(String movieTitle);
	
	int loadMovieLlistFromImdbUrl();
}
