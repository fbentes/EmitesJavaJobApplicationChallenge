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
	 * Inicia a conexão do servidor socket. 
	 * 
	 * @param port Porta de conexão. 
	 * @return Verdadeiro caso seja estabelecida a conexão. Falso caso contrário.
	 * @see ServerCommand#execute()
	 */
	boolean connect(int port);

	/**
	 * Se connect() disparar exceção pela porta ocupada, uma outra porta disponível 
	 * será encontrada para conexão.
	 * 
	 * @return Porta aberta para conexão socket.
	 */
	Integer getAlternativePort();
	
	/**
	 * Fica espera de solicitações dos clientes enquanto não for parado pelo usuário.
	 */
	void waitingForClientRequests();
	
	/**
	 * Carrega em cache a lista de filmes buscadas do site IMDb.
	 * 
	 * @return Quantidade de itens da lista.
	 * @see IMDbUrlConnection#fillMovieListFromImdbUrl()
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
