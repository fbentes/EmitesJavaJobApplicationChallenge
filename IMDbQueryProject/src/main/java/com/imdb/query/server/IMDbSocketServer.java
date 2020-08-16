package com.imdb.query.server;

/**
 * Servidor socket para atendimento aos clientes.
 *
 * @author F�bio Bentes
 * @version 1.0.0.0
 * @since 09/08/2020
 * 
 */
public interface IMDbSocketServer {
	
	/**
	 * Inicia a conex�o do servidor socket. 
	 * 
	 * @param port Porta de conex�o. 
	 * @return Verdadeiro caso seja estabelecida a conex�o. Falso caso contr�rio.
	 * @see ServerCommand#execute()
	 */
	boolean connect(int port);

	/**
	 * Se connect() disparar exce��o pela porta ocupada, uma outra porta dispon�vel 
	 * ser� encontrada para conex�o.
	 * 
	 * @return Porta aberta para conex�o socket.
	 */
	Integer getAlternativePort();
	
	/**
	 * Fica espera de solicita��es dos clientes enquanto n�o for parado pelo usu�rio.
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
	 * For�a a parada do servidor e encerramento de sua thread.
	 */
	void stop();
	
	/**
	 * @return Verdadeiro caso o servidor foi solicitado para parar.
	 */
	boolean isStoped();
}
