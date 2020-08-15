/**
 * 
 */
package com.imdb.query.server;

/**
 * Servidor socket para atendimento aos clientes.
 *
 * @author Fábio Bentes
 * @version 1.0.0.0
 * @since 09/08/2020
 * 
 */
public interface IMDbSocketServer {
	
	/**
	 * Conecta na porta específica. O padrão é 2021.
	 * 
	 * @param port
	 * @return
	 */
	boolean connect(int port);
	
	/**
	 * Fica espera de solicitações dos clientes enquanto não for parado pelo usuário.
	 */
	void waitingForClientRequests();
	
	/**
	 * Carrega em cache a lista de filmes buscadas do site IMDb.
	 * 
	 * @return Quantidade de itens da lista.
	 */
	int loadMovieLlistFromImdb();
	
	/**
	 * Força a parada do servidor e encerramento de sua thread.
	 */
	void stop();
	
	/**
	 * @return Verdadeiro caso o servidor foi solicitado para parar.
	 */
	boolean isStoped();
}
