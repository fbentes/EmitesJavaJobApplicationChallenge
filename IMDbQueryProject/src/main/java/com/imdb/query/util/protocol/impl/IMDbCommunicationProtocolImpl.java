package com.imdb.query.util.protocol.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.imdb.query.util.Constants;
import com.imdb.query.util.protocol.IMDbCommunicationProtocol;

/**
 * Responsável pela aplicação do protocolo de comunicação entre o cliente e servidor Sockets.
 * 
 * Ex.: O Cliente deve enviar "IMDb_" + movieTitle + "_IMDb" para ser aceito pelo servidor.
 *
 * @author Fábio Bentes
 * @version 1.0.0.0
 * @since 14/08/2020
 * 
 */
public class IMDbCommunicationProtocolImpl implements IMDbCommunicationProtocol {

	private String movieTitle;
	
	public void setMovieTitleWithPatternProtocol(String movieTitle) {
		this.movieTitle = movieTitle;
	}
	
	public String getMovieTitle() {
		return movieTitle;
	}
	
	public String getMovieTitleWithOutPatternProtocol() {
		return movieTitle.replace(Constants.PREFIX_PROTOCOL, "").replace(Constants.SUFIX_PROTOCOL, "");
	}
	
	public boolean isMatchPatternProtocol() {
		
		if(movieTitle == null || movieTitle.trim().equals("")) {
			return false;
		}
		
		final Pattern pattern = Pattern.compile(Constants.REGEX_PATTERN_PROTOCOL, Pattern.DOTALL);
		
		final Matcher matcher = pattern.matcher(movieTitle);
		
		return matcher.find();
	}
}
