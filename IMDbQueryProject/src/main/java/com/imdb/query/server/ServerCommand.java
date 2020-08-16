package com.imdb.query.server;

/**
 * Padrão Command para iniciar o servidor socket.
 *
 * @author Fábio Bentes
 * @version 1.0.0.0
 * @since 10/08/2020
 * 
 */
public interface ServerCommand {

	void execute(IMDbServerSocket imdbSocketServer);
}
