package com.imdb.query.util.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.Random;

import com.imdb.query.util.Constants;

/**
 * Respons�vel por fornecer apis para uso com a porta TCP.
 * 
 * @author F�bio Bentes
 * @version 1.0.0.0
 * @since 16/08/2020
 *
 */
public class TCPPortUtility {


	/**
	 * Valida se a porta est� dentro da faixa num�rica permitida.
	 * 
	 * @param port Porta a ser validada. O tipo � Object para facilitar a chamada pelo cliente.
	 * @return Verdadeiro se for v�lida. Falso caso contr�rio.
	 */
	public boolean isPortValid(Object port) {
		
		Optional<Object> optionalPort =  Optional.ofNullable(port);
				
		if(!optionalPort.isPresent()) {
			
			throw new IllegalArgumentException("A porta n�o pode ser nula !");
		}
		
	    try {
	    
	    	int portNumber = Integer.parseInt(port.toString());
	        
	        return portNumber >= 1 && portNumber <= Constants.PORT_TCP_MAX;

	    } catch (NumberFormatException e) {
	        return false;
	    }
	}
		
	/**
	 * Checa via comando windows netstat se a porta est� ocupada ou livre para uso.
	 * 
	 * @param port Porta a ser checada.
	 * @return Verdadeiro se a porta estiver livre. Falso caso contr�rio.
	 */
	public boolean isPortOpened(int port) {
		
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "netstat -na | find \""+port+"\"");
            
        builder.redirectErrorStream(true);
            
        Optional<Process> process = Optional.empty();
		
        try {
			
        	process = Optional.ofNullable(builder.start());
			
		} catch (IOException e) {
			
			System.out.println(e.getMessage());
			
			return false;
		}
        
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.get().getInputStream()));
        
    	try {
    		
	        return bufferedReader.readLine() == null;
	        		        
		} catch (IOException e) {
			
			System.out.println(e.getMessage());
		}
        
        return false;
	}
	
	/**
	 * Tenta de forma iterativa, 30 tentativas obter a pr�xima porta 
	 * dispon�vel para uso pelo servidor socket.
	 * 
	 * @return N�mero da porta dispon�vel.
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
