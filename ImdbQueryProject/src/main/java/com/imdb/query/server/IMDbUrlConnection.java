/**
 * 
 */
package com.imdb.query.server;

/**
 * @author Fábio Bentes
 * 
 * Responsável pela comunicação com o site IMDb para busca de filmes.
 *
 */
public interface IMDbUrlConnection {

	/**
	 * Pesquisa na lista ordenada alfabeticamente no servidor todas  
	 * as ocorrências encontradas de nomes, a partir do início. 
	 * 
	 * Ex.: Quando o usuário pesquisar "Batman", será retornado: "Batman Begins, Batman - Cavaleiro das Trevas", etc... 
	 * 
	 * @param movieTitle 
	 * @return
	 */
	String getMoviesFound(String movieTitle);
	
	/**
	 * Preenche a lista em memória com os nomes retornados do site IMDb.
	 * 
	 * @return
	 */
	int fillMovieListFromImdbUrl();
}
