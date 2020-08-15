/**
 * 
 */
package com.imdb.query.server;

/**
 * @author F�bio Bentes
 * 
 * Servidor socket para atendimento aos clientes.
 *
 */
public interface IMDbSocketServer {
	
	/**
	 * Conecta na porta espec�fica. O padr�o � 2021.
	 * 
	 * @param port
	 * @return
	 */
	boolean connect(int port);
	
	/**
	 * Fica espera de solicita��es dos clientes enquanto n�o for parado pelo usu�rio.
	 */
	void waitingForClientRequests();
	
	/**
	 * Carrega em cache a lista de filmes buscadas do site IMDb.
	 * 
	 * @return Quantidade de itens da lista.
	 */
	int loadMovieLlistFromImdb();
	
	/**
	 * For�a a parada do servidor e encerramento de sua thread.
	 */
	void stop();
	
	/**
	 * @return Verdadeiro caso o servidor foi solicitado para parar.
	 */
	boolean isStoped();
}
