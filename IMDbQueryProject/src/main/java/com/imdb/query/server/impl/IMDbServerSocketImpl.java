package com.imdb.query.server.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.inject.Inject;

import com.imdb.query.server.IMDbServerSocket;
import com.imdb.query.server.IMDbUrlConnection;
import com.imdb.query.util.network.TCPPortUtility;

/**
 * Responsável pelo atendimento das requisições dos clientes.
 * Fica em loop até o usuário do servidor socket solicitar a parada do mesmo.
 * Enquanto em loop, a cada solicitação uma Thread (ImdbClientHandler) é instanciada 
 * para o atendimento.
 *
 * @author Fábio Bentes
 * @version 1.0.0.0
 * @since 09/08/2020
 * 
 */
public class IMDbServerSocketImpl implements IMDbServerSocket {
		
	private ServerSocket serverSocket;
	
	private boolean isExecuting;
	
	private int port;
	
	private Integer alternativePort;
	
	@Inject
	private IMDbUrlConnection iMDbUrlConnection;
	
	@Override
	public boolean connect() {
		
		alternativePort = port;
		
		do {
			
			try {
				
				serverSocket = new ServerSocket(alternativePort);
				
				isExecuting = true;
				
			} catch (IOException e) {
				
				System.out.println("Problema ao conectar na porta " + alternativePort + ": " + e.getMessage());
				
				System.out.println("Tentando outra porta ...");
				
				TCPPortUtility tcpPortUtility = new TCPPortUtility();
				
				alternativePort = tcpPortUtility.getNextPortOpenedToUse();
				
				isExecuting = false;				
			}

		} while(!isExecuting);
		
		return isExecuting;
	}
	
	@Override
	public void setPort(int port) {
		
		this.port = port;
	}
	
	@Override
	public int getPort() {
		return this.port;
	}

	@Override
	public Integer getAlternativePort() {
		return alternativePort;
	}
	
	@Override
	public int loadMovieLlistFromImdb() {
		
		if(!isExecuting) {
			System.out.println("O método connect() deve ser chamado !");
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
	
	@Override
	public boolean isStoped() {
		
		return !isExecuting;
	}
}
