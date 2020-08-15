package com.imdb.query.server;

/**
 * Responsável pela comunicação com o site IMDb para busca de filmes.
 *
 * @author Fábio Bentes
 * @version 1.0.0.0
 * @since 09/08/2020
 * 
 */
public interface IMDbUrlConnection {

	/**
	 * Pesquisa na lista ordenada alfabeticamente no servidor todas  
	 * as ocorrências encontradas de nomes, a partir do início. 
	 * 
	 * Ex.: Quando o usuário pesquisar "Batman", será retornado: "Batman Begins, Batman - Cavaleiro das Trevas", etc... 
	 * 
	 * @param movieTitle Título do filme para pesquisa na lista em cache.
	 * @return Filmes encontrados.
	 */
	String getMoviesFound(String movieTitle);
	
	/**
	 * Preenche a lista em memória com os nomes retornados do site IMDb.
	 * 
	 * @return Quantidade de filmes inseridos na lista.
	 * @see IMDbSocketServer#loadMovieLlistFromImdb()
	 */
	int fillMovieListFromImdbUrl();
}
