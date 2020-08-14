package com.imdb.query.util.protocol.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.imdb.query.util.Constants;
import com.imdb.query.util.protocol.IMDbCommunicationProtocol;

/**
 * 
 * @author F�bio Bentes
 * 
 * Classe respons�vel pela aplica��o do protocolo de comunica��o entre o cliente e servidor Sockets.
 * 
 * Ex.: O Cliente deve enviar IMDb_nome do filme_IMDb para ser aceito pelo servidor.
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
	
	public static void main(String[] args) {

		IMDbCommunicationProtocolImpl iMDbCommunicationProtocol =  new IMDbCommunicationProtocolImpl();
		
		String movieTitle = "A Separa��o";
		
		System.out.println("************* SEM PADR�O ******************");

		iMDbCommunicationProtocol.setMovieTitleWithPatternProtocol(movieTitle);
		
		boolean b = iMDbCommunicationProtocol.isMatchPatternProtocol();
		
		System.out.println("isMatch = " +b);
		System.out.println(iMDbCommunicationProtocol.getMovieTitleWithOutPatternProtocol());

		System.out.println("************* COM PADR�O ******************");
		iMDbCommunicationProtocol.setMovieTitleWithPatternProtocol("IMDb_"+movieTitle+"_IMDb");
		
		b = iMDbCommunicationProtocol.isMatchPatternProtocol();
		
		System.out.println("isMatch = " +b);
		System.out.println(iMDbCommunicationProtocol.getMovieTitleWithOutPatternProtocol());
	}
}
