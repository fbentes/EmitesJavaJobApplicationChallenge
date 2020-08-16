package com.imdb.query.util.protocol;

/**
 * Responsável pela aplicação do protocolo de comunicação entre o servidor e o cliente.
 * 
 * @author Fábio Bentes
 * @version 1.0.0.0
 * @since 14/08/2020
 * 
 */
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
	String getMovieTitleWithPatternProtocol();
	
	/**
	 * @return Apenas o título do filme sem o protocolo para pesquisa no site IMDb.
	 */
	String getMovieTitleWithOutPatternProtocol();
	
	/**
	 * @return Verdadeiro se o protocolo correto foi aplicado no nome do filme
	 */
	boolean isMatchPatternProtocol();
}