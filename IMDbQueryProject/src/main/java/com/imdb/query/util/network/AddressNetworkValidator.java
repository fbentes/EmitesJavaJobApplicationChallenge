/**
 * 
 */
package com.imdb.query.util.network;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Respons�vel pela valida��o de IP.
 * 
 * @author F�bio Bentes
 * 
 * @version 1.0.0.0
 * @since 16/08/2020
 *
 */
public class AddressNetworkValidator {
	
    private Pattern pattern;
    private Matcher matcher;
    
	private String IPADDRESS_PATTERN = 
				        "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	
	/**
	 * Valida o IP informado se � ou n�o v�lido.
	 * 
	 * @param ipAddress IP informado pelo cliente.
	 * @return Verdadeiro caso seja v�lido. Falso caso conr�rio.
	 */
	public boolean isIpValid(String ipAddress) {
		
		pattern = Pattern.compile(IPADDRESS_PATTERN);
		matcher = pattern.matcher(ipAddress);
	    
		return  matcher.matches();
	}
}
