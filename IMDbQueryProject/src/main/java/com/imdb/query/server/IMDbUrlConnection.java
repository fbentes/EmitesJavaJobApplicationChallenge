package com.imdb.query.server;

/**
 * Respons�vel pela comunica��o com o site IMDb para busca de filmes.
 *
 * @author F�bio Bentes
 * @version 1.0.0.0
 * @since 09/08/2020
 * 
 */
public interface IMDbUrlConnection {

	/**
	 * Pesquisa na lista ordenada alfabeticamente no servidor todas  
	 * as ocorr�ncias encontradas de nomes, a partir do in�cio. 
	 * 
	 * Ex.: Quando o usu�rio pesquisar "Batman", ser� retornado: "Batman Begins, Batman - Cavaleiro das Trevas", etc... 
	 * 
	 * @param movieTitle T�tulo do filme para pesquisa na lista em cache.
	 * @return Filmes encontrados.
	 */
	String getMoviesFound(String movieTitle);
	
	/**
	 * Preenche a lista em mem�ria com os nomes retornados do site IMDb.
	 * 
	 * @return Quantidade de filmes inseridos na lista.
	 * @see IMDbSocketServer#loadMovieLlistFromImdb()
	 */
	int fillMovieListFromImdbUrl();
}
