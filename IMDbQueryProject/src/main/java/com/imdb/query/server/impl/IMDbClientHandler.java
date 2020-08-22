package com.imdb.query.server.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.imdb.query.server.IMDbUrlConnection;
import com.imdb.query.util.Constants;
import com.imdb.query.util.IMDbQueryModuleInjector;
import com.imdb.query.util.protocol.IMDbCommunicationProtocol;

/**
 * Handler responsável pelos atendimentos das requisições dos clientes.
 * Cada solicitação do cliente é atendida por essa Thread do servidor socket.
 * 
 * @author Fábio Bentes
 * @version 1.0.0.0
 * @since 09/08/2020
 * 
 */
public class IMDbClientHandler extends Thread {
	
    private static final Logger logger = LogManager.getLogger(IMDbClientHandler.class);

    private Socket clientSocket;

    @Inject
	private IMDbUrlConnection iMDbUrlConnection;

    @Inject
    private IMDbCommunicationProtocol iMDbCommunicationProtocol;
    
	public void setClientSocket(Socket clientSocket) {
    	
    	this.clientSocket = clientSocket;
    }
	
    public void run() {

    	IMDbQueryModuleInjector.initialize(this);  // Injeta dependências

    	// Aloca recursos para leitura e escrita de/para o cliente socket.
    	
		try (BufferedReader readFromClientBufferedReader = new 
				BufferedReader(
						new InputStreamReader(clientSocket.getInputStream()));
			 PrintWriter writeToClientPrintWriter = 
						new PrintWriter(
								clientSocket.getOutputStream(), true)) {
			
    		// Leitura do título do filme enviado pelo cliente.
    		
    		Optional<String> movieTitle = Optional.ofNullable(readFromClientBufferedReader.readLine());    		
    		
    		if(!movieTitle.isPresent()) {
    			
                writeToClientPrintWriter.println(Constants.IVALID_MESSAGE_CLIENT_TO_SERVER);

                return;
    		}
    		       
    		// Checa se o título do filme foi recebido do cliente com o protocolo correto. 
    		
            if(!iMDbCommunicationProtocol.isMatchPatternProtocol(movieTitle.get())) {
            	
            	writeToClientPrintWriter.println(Constants.IVALID_MESSAGE_PROTOCOL);
                
                return;
            }
            
            // Remove o protocolo aplicado pelo cliente no título do filme.
            
            String movieTitleWithOutProtocol = removeMovieTitleProtocolToSearch(movieTitle.get());
            
            // Retorna o(s) filme(s) encontrado(s) pelo servidor.
            
            String moviesFound = iMDbUrlConnection.getMoviesFound(movieTitleWithOutProtocol);

            // Responde para o cliente o(s) filme(s) encontrados com o mesmo protocolo de comunicação aplicado pelo cliente.
            
            writeToClientPrintWriter.println(applyCommunicationProtocol(moviesFound));	
		} 
		catch (IOException e) {
			
			logger.error("Problema na resposta para o cliente: " + e.getMessage());
		}
        finally {
			
			closeClientSocket();
		}
    }
    
    /**
     * Aplica protocolo de comunicação nos filmes encontrados.
     * 
     * @param moviesFound Filmes encontrados pelo servidor separados por \n.
     * @return Filmes encontrados com protocolo aplicado.
     */
    private String applyCommunicationProtocol(String moviesFound) {
    	
    	return iMDbCommunicationProtocol.getMessageWithPatternProtocolApplied(moviesFound);
    }
    
    /**
     * Remove protocolo de comunicação do título de filme.
     * 
     * @param movieTitle Título do filme.
     * @return Título do filme sem protocolo.
     */
    private String removeMovieTitleProtocolToSearch(String movieTitle) {
    	
        return iMDbCommunicationProtocol.getMessageWithOutPatternProtocolApplied(movieTitle);
    }
    
    private void closeClientSocket() {
    	
        try {
			
        	clientSocket.close();
			
		} catch (IOException e) {
			
			logger.error(e.getMessage());
		}
    }
}
