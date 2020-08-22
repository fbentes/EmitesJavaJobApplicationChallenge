package com.imdb.query.server.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.imdb.query.server.IMDbServerSocket;
import com.imdb.query.server.IMDbUrlConnection;
import com.imdb.query.util.network.TCPPortUtility;

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
public class IMDbServerSocketImpl implements IMDbServerSocket {
		
    private static final Logger logger = LogManager.getLogger(IMDbServerSocketImpl.class);

	private ServerSocket serverSocket;
	
	private boolean isExecuting;
	
	private int port;
	
	private Integer alternativePort;
	
	@Inject
	private IMDbUrlConnection iMDbUrlConnection;
	
	@Override
	public boolean connectToServerSocket() {
		
		alternativePort = port;
		
		do {
			
			try {
				
				serverSocket = new ServerSocket(alternativePort);
				
				isExecuting = true;
				
			} catch (IOException e) {
				
				logger.error("Problema ao conectar na porta " + alternativePort + ": " + e.getMessage());
				
				logger.error("Tentando outra porta ...");
				
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
			logger.error("O m�todo connect() deve ser chamado !");
			return -1;
		}
		
		return iMDbUrlConnection.fillMovieListFromImdbUrl();
	}
	
	@Override
	public void waitingForClientRequests() {
		
		int attemptsToAcceptClientRequest = 1;
		
		while (isExecuting) {
			 
			Optional<Socket> clientSocket = Optional.empty();
	
			 try {
				
				 clientSocket = Optional.ofNullable(serverSocket.accept());
				
			} catch (Exception e) {
				
				if(isExecutionRequestedStoped()) {  // Se foi requisitado para parar o servidor socket pela thread chamadora.
					break;
				}
				
				logger.error("Tentiva " + attemptsToAcceptClientRequest + ". Problema na espera de requisi��o do cliente: " + e.getMessage());
				
				// Tenta 30 vezes receber requisi��o do cliente. Se ultrapassar encerra o servidor.
				
				if(++attemptsToAcceptClientRequest > 30) {
					break;
				}
				
				continue;
			}
			 
			 // Inicia uma Thread para atendimento da solicita��o por cliente socket.
			 
			 if(clientSocket.isPresent()) {
				 
				IMDbClientHandler imdbClientHandler = new IMDbClientHandler(); 
				
				imdbClientHandler.setClientSocket(clientSocket.get());
				
              	imdbClientHandler.start();
			 }
		}
	
		closeServerSocket();
	}
	
	@Override
	public void requestStopExecution() {
		
		isExecuting = false;
    }
	
	@Override
	public boolean isExecutionRequestedStoped() {
		
		return !isExecuting;
	}
	
	@Override
	public boolean closeServerSocket() {
		
		try {
				
			 serverSocket.close();
			 
			 return true;
			
		} catch (IOException e) {
			
			logger.error("Problema ao fechar conex�o do servidor: "+ e.getMessage());
		}
		 
		 return false;
	}
}
