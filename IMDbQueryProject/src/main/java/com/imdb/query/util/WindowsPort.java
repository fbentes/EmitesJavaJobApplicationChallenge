package com.imdb.query.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class WindowsPort {

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
			
		} while(!isPortOpened(port) && ++attempts < 30);
		
		if(attempts > 30) {
			return null;
		}
		
		return port;
	}
}
