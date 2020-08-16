package com.imdb.query.util.protocol;

/**
 * Respons�vel pela aplica��o do protocolo de comunica��o entre o servidor e o cliente.
 * 
 * @author F�bio Bentes
 * @version 1.0.0.0
 * @since 14/08/2020
 * 
 */
public interface IMDbCommunicationProtocol {

	/**
	 * Seta o t�tuto do filme com o protocolo aplicado. 
	 * Usado pelo Client Socket. 
	 * 
	 * @param movieTitle T�tulo do filme com o protocolo. Ex.: "IMDb_Batman_IMDb"
	 */
	void setMovieTitleWithPatternProtocol(String movieTitle);

	/**
	 * T�tulo do filme com o protocolo aplicado.
	 * Usado pelo client socket para envio para o servidor socket.
	 * 
	 * @return . Ex.: "IMDb_Batman_IMDb"
	 */
	String getMovieTitleWithPatternProtocol();
	
	/**
	 * @return Apenas o t�tulo do filme sem o protocolo para pesquisa no site IMDb.
	 */
	String getMovieTitleWithOutPatternProtocol();
	
	/**
	 * @return Verdadeiro se o protocolo correto foi aplicado no nome do filme
	 */
	boolean isMatchPatternProtocol();
}