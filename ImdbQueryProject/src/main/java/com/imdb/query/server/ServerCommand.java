/**
 * 
 */
package com.imdb.query.server;

/**
 * @author Fábio Bentes
 * 
 * Padrão Command para iniciar o servidor socket.
 *
 */
public interface ServerCommand {

	void setIMDbSocketServer(IMDbSocketServer imdbSocketServer);
	void execute();
}
