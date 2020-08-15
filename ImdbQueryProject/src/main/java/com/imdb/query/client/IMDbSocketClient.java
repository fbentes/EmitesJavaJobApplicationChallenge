/**
 * 
 */
package com.imdb.query.client;

/**
 * @author F�bio Bentes
 *
 * Cliente socket para solicita��es de filmes ao servidor.
 */
public interface IMDbSocketClient {
	
	/**
	 * Conecta no ip do servidor na porta especificada. A porta padr�o � 2021.
	 * 
	 * @param ipServer
	 * @param port
	 * @return
	 */
	boolean connectToServer(String ipServer, int port);
	
	/**
	 * Busca de filmes no servidor.
	 * 
	 * @param movieTitle t�tulo do filme com o protocolo aplicado
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
