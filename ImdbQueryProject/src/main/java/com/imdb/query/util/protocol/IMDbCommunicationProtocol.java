package com.imdb.query.util.protocol;

public interface IMDbCommunicationProtocol {

	/**
	 * Seta o títuto do filme com o protocolo aplicado. 
	 * Usado pelo Client Socket. 
	 * 
	 * @param movieTitle Título do filme com o protocolo. Ex.: "IMDb_Batman_IMDb"
	 */
	void setMovieTitleWithPatternProtocol(String movieTitle);

	/**
	 * Título do filme com o protocolo aplicado.
	 * Usado pelo client socket para envio para o servidor socket.
	 * 
	 * @return . Ex.: "IMDb_Batman_IMDb"
	 */
	String getMovieTitle();
	
	/**
	 * @return Apenas o título do filme sem o protocolo para pesquisa no site IMDb.
	 */
	String getMovieTitleWithOutPatternProtocol();
	
	/**
	 * @return Verdadeiro se o protocolo correto foi aplicado no nome do filme
	 */
	boolean isMatchPatternProtocol();
}