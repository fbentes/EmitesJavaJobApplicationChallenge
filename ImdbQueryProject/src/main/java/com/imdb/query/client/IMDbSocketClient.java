/**
 * 
 */
package com.imdb.query.client;

/**
 * Cliente socket para solicitações de filmes ao servidor.
 * 
 * @author Fábio Bentes
 * @version 1.0.0.0
 * @since 09/08/2020
 * 
 */
public interface IMDbSocketClient {
	
	/**
	 * Conecta no ip do servidor na porta especificada. A porta padrão é 2021.
	 * 
	 * @param ipServer
	 * @param port
	 * @return
	 */
	boolean connectToServer(String ipServer, int port);
	
	/**
	 * Busca de filmes no servidor.
	 * 
	 * @param movieTitle título do filme com o protocolo aplicado
	 * @return 	Todos os filmes encontrados.
	 */
	String sendMovieTitleToSearchInServer(String movieTitle);
	
	/**
	 * Libera os recursos alocados pelo cliente.
	 * 
	 * @return Verdadeiro caso todos os recursos sejam liberados.
	 */
	boolean stopConnection();
}
