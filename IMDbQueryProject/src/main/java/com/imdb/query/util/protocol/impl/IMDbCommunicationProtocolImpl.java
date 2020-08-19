package com.imdb.query.util.protocol.impl;

import java.util.Optional;
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
	
	@Override
	public void setMovieTitleWithPatternProtocol(String movieTitle) {
		this.movieTitle = movieTitle;
	}
	
	@Override
	public String getMovieTitleWithPatternProtocol() {
		return movieTitle;
	}
	
	@Override
	public String getMovieTitleWithOutPatternProtocol() {
		
		return movieTitle.replace(
						Constants.PREFIX_PROTOCOL, 
						Constants.STRING_EMPTY).replace(
								Constants.SUFIX_PROTOCOL, 
								Constants.STRING_EMPTY);
	}
	
	@Override
	public boolean isMatchPatternProtocol() {
		
		Optional<String> optionalMovieTitle = Optional.ofNullable(movieTitle);
		
		if(!optionalMovieTitle.isPresent() || optionalMovieTitle.get().trim().equals(Constants.STRING_EMPTY)) {
			return false;
		}
		
		final Pattern pattern = Pattern.compile(Constants.REGEX_PATTERN_PROTOCOL, Pattern.DOTALL);
		
		final Matcher matcher = pattern.matcher(optionalMovieTitle.get());
		
		return matcher.find();
	}
}
