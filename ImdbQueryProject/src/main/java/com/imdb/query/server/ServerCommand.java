/**
 * 
 */
package com.imdb.query.server;

/**
 * @author fbent
 *
 */
public interface ServerCommand {

	void setImdbSocketServer(ImdbSocketServer imdbSocketServer);
	void execute();
}
