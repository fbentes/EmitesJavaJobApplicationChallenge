package com.imdb.query.client;

/**
 * Cliente socket para solicita��es de filmes ao servidor.
 * 
 * @author F�bio Bentes
 * @version 1.0.0.0
 * @since 09/08/2020
 * 
 */
public interface IMDbClientSocket {
	
	/**
	 * Conecta no ip do servidor na porta especificada. A porta padr�o � 20222.
	 * 
	 * @param ipServer IP do servidor iniciado.
	 * @param port Porta usada para conex�o. O padr�o � 20222.
	 * @return Verdadeiro caso seja estabelecida a conex�o com o servidor. Falso caso contr�rio.
	 */
	boolean connectToServer(String ipServer, int port);
	
	/**
	 * Busca de filmes no servidor.
	 * 
	 * @param movieTitle t�tulo do filme com o protocolo aplicado
	 * @return 	Todos os filmes encontrados.
	 */
	String requestMovieTitleToSearchInServer(String movieTitle);	
}
