/**
 * 
 */
package com.imdb.query.util.network;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Responsável pela validação de IP.
 * 
 * @author Fábio Bentes
 * 
 * @version 1.0.0.0
 * @since 16/08/2020
 *
 */
public class AddressNetworkValidator {
	
    private static final Logger logger = LogManager.getLogger(AddressNetworkValidator.class);

    private Pattern pattern;
    private Matcher matcher;
    
	private String IPADDRESS_PATTERN = 
				        "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	
	/**
	 * Valida o IP informado se é ou não válido.
	 * 
	 * @param ipAddress IP informado pelo cliente.
	 * @return Verdadeiro caso seja válido. Falso caso conrário.
	 */
	public boolean isIpValid(String ipAddress) {
		
		pattern = Pattern.compile(IPADDRESS_PATTERN);
		matcher = pattern.matcher(ipAddress);
	    
		boolean isMatch = matcher.matches();
		
		if(!isMatch) {
			logger.info(String.format("O IP %s é inválido !", ipAddress));
		}
		
		return isMatch;   
	}
}
