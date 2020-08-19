package com.imdb.query.server.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

import com.google.inject.Inject;
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
		
		int attemptsNumberToAcceptClientRequest = 1;
		
		while (isExecuting) {
			 
			Optional<Socket> clientSocket = Optional.empty();
	
			 try {
				
				 clientSocket = Optional.ofNullable(serverSocket.accept());
				
			} catch (Exception e) {
				
				if(isRequestedStoped()) {  // Se foi requisitado para parar o servidor socket pela thread chamadora.
					break;
				}
				
				System.out.println("Tentiva " + attemptsNumberToAcceptClientRequest + ". Problema na espera de requisição do cliente: " + e.getMessage());
				
				// Tenta 30 vezes receber requisição do cliente. Se ultrapassar encerra o servidor.
				
				if(++attemptsNumberToAcceptClientRequest > 30) {
					break;
				}
				
				continue;
			}
			 
			 // Inicia uma Thread para atendimento da solicitação por cliente socket.
			 
			 if(clientSocket.isPresent()) {
				 
				IMDbClientHandler imdbClientHandler = new IMDbClientHandler(); 
				
				imdbClientHandler.setClientSocket(clientSocket.get());
				
              	imdbClientHandler.start();
			 }
		}
	
		close();
	}
	
	@Override
	public void requestStop() {
		
		isExecuting = false;
    }
	
	@Override
	public boolean isRequestedStoped() {
		
		return !isExecuting;
	}
	
	@Override
	public boolean close() {
		
		try {
				
			 serverSocket.close();
			 
			 return true;
			
		} catch (IOException e) {
			
			System.out.println("Problema ao fechar conexão do servidor: "+ e.getMessage());
		}
		 
		 return false;
	}
}
