/**
 * 
 */
package com.imdb.query.server;

/**
 * @author F�bio Bentes
 * 
 * Padr�o Command para iniciar o servidor socket.
 *
 */
public interface ServerCommand {

	void setIMDbSocketServer(IMDbSocketServer imdbSocketServer);
	void execute();
}
