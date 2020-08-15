/**
 * 
 */
package com.imdb.query.server.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.inject.Inject;

import com.imdb.query.server.IMDbUrlConnection;
import com.imdb.query.server.IMDbSocketServer;
import com.imdb.query.util.Constants;

/**
 * Respons�vel pelo atendimento das requisi��es dos clientes.
 * Fica em loop at� o usu�rio do servidor socket solicitar a parada do mesmo.
 * Enquanto em loop, a cada solicita��o uma Thread (ImdbClientHandler) � instanciada 
 * para o atendimento.
 *
 * @author F�bio Bentes
 * @version 1.0.0.0
 * @since 09/08/2020
 * 
 */
public class IMDbSocketServerImpl implements IMDbSocketServer {
		
	private ServerSocket serverSocket;
	
	private boolean isExecuting;
	
	public boolean isStoped() {
		
		return !isExecuting;
	}
	
	@Inject
	private IMDbUrlConnection iMDbUrlConnection;
	
	@Override
	public boolean connect(int port) {
		
		try {
			
			serverSocket = new ServerSocket(port);
			
			isExecuting = true;
			
		} catch (IOException e) {
			
			System.out.println("Problema ao conectar na porta " + Constants.PORT + ": " + e.getMessage());
			
			isExecuting = false;
		}
		
		return isExecuting;
	}
	
	@Override
	public int loadMovieLlistFromImdb() {
		
		if(!isExecuting) {
			System.out.println("O m�todo connect() deve ser chamado !");
			return -1;
		}
		
		return iMDbUrlConnection.fillMovieListFromImdbUrl();
	}
	
	@Override
	public void waitingForClientRequests() {
		
		 while (isExecuting) {
			 
			 Socket clientSocket = null;

			 try {
				
				 clientSocket = serverSocket.accept();
				
			} catch (IOException e) {
				
				System.out.println("Problema ao conectar no servidor: " + e.getMessage());
			}
			 
			 IMDbClientHandler imdbClientHandler = new IMDbClientHandler(clientSocket, iMDbUrlConnection);
			 
			 imdbClientHandler.start();
		 }

		 close();
	}
	
	private boolean close() {
		
		 try {
				
			 serverSocket.close();
			 
			 return true;
			
		} catch (IOException e) {
			
			System.out.println(e.getMessage());
		}
		 
		 return false;
	}
	
	@Override
	public void stop() {
		
		isExecuting = false;
    }
}
