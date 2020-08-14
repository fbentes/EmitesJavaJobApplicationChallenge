package com.imdb.query.util.protocol;

public interface IMDbCommunicationProtocol {

	void setMovieTitleWithPatternProtocol(String movieTitle);

	String getMovieTitle();
	
	String getMovieTitleWithOutPatternProtocol();
	
	boolean isMatchPatternProtocol();
}