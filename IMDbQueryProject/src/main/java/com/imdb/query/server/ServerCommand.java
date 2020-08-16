package com.imdb.query.server;

/**
 * Padr�o Command para iniciar o servidor socket.
 *
 * @author F�bio Bentes
 * @version 1.0.0.0
 * @since 10/08/2020
 * 
 */
public interface ServerCommand {

	void execute(IMDbServerSocket imdbSocketServer);
}
