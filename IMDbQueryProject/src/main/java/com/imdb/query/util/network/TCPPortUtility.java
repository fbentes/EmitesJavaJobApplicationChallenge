package com.imdb.query.util.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import com.imdb.query.util.Constants;

/**
 * Responsável por fornecer apis para uso com a porta TCP.
 * 
 * @author Fábio Bentes
 * @version 1.0.0.0
 * @since 16/08/2020
 *
 */
public class TCPPortUtility {


	/**
	 * Valida se a porta está dentro da faixa numérica permitida.
	 * 
	 * @param port Porta a ser validada.
	 * @return Verdadeiro se for válida. Falso caso contrário.
	 */
	public boolean isPortValid(Object port) {
		
		if( port == null) {
			
			throw new NullPointerException("A porta não pode ser nula !");
		}
		
	    try {
	    
	    	int portNumber = Integer.parseInt(port.toString());
	        
	        return portNumber >= 1 && portNumber <= Constants.PORT_TCP_MAX;

	    } catch (NumberFormatException e) {
	        return false;
	    }
	}
		
	/**
	 * Checa via comando windows netstat se a porta está ocupada ou livre para uso.
	 * 
	 * @param port Porta a ser checada.
	 * @return Verdadeiro se a porta estiver livre. Falso caso contrário.
	 */
	public boolean isPortOpened(int port) {
		
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "netstat -na | find \""+port+"\"");
            
        builder.redirectErrorStream(true);
            
        Process p = null;
		
        try {
			
        	p = builder.start();
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
        
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        
    	try {
	        if(r.readLine() != null) {
	        	return false;
	        }
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}           
        
        return true;
	}
	
	/**
	 * Tenta de forma iterativa, 30 tentativas obter a próxima porta 
	 * disponível para uso pelo servidor socket.
	 * 
	 * @return Número da porta disponível.
	 */
	public Integer getNextPortOpenedToUse() {
		
		int attempts = 1;
		
		Integer port = null;
		
		do {
		
			Random random = new Random();
			
			port = random.nextInt(Constants.PORT_TCP_MAX);
			
		} while(!isPortOpened(port) && ++attempts <= 30);
		
		if(attempts > 30) {
			return null;
		}
		
		return port;
	}
}
