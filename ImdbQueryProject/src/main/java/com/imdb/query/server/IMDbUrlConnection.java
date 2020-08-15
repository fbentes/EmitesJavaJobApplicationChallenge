/**
 * 
 */
package com.imdb.query.server;

/**
 * @author F�bio Bentes
 * 
 * Respons�vel pela comunica��o com o site IMDb para busca de filmes.
 *
 */
public interface IMDbUrlConnection {

	/**
	 * Pesquisa na lista ordenada alfabeticamente no servidor todas  
	 * as ocorr�ncias encontradas de nomes, a partir do in�cio. 
	 * 
	 * Ex.: Quando o usu�rio pesquisar "Batman", ser� retornado: "Batman Begins, Batman - Cavaleiro das Trevas", etc... 
	 * 
	 * @param movieTitle 
	 * @return
	 */
	String getMoviesFound(String movieTitle);
	
	/**
	 * Preenche a lista em mem�ria com os nomes retornados do site IMDb.
	 * 
	 * @return
	 */
	int fillMovieListFromImdbUrl();
}
