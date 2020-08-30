package com.imdb.query.util.protocol.impl;

import com.google.inject.Singleton;
import com.imdb.query.util.Constants;
import com.imdb.query.util.protocol.IMDbCommunicationProtocol;

@Singleton
public class IMDbCommunicationProtocolWithLengthDataTransferImpl implements IMDbCommunicationProtocol {

	private static final String SEPARATOR_PROTOCOL = ":";
	
	@Override
	public String getMessageWithOutPatternProtocolApplied(String message) {
		
		int beginIndex = message.indexOf(SEPARATOR_PROTOCOL);
		
		return message.substring(beginIndex + 1, message.length());
	}

	@Override
	public String getMessageWithPatternProtocolApplied(String message) {
		
		return message.length() + SEPARATOR_PROTOCOL + message;
	}

	/* Solicitação do cliente socket = 6:batman
	 * Resposta do servidor socket = 33:3 Idiotas\nO 3 Homem\nToy Story 3
	 */	
	
	@Override
	public boolean isMatchPatternProtocol(String message) {
		
		String[] messageSeparatedProtocol = message.split(SEPARATOR_PROTOCOL);

		String query = Constants.STRING_EMPTY;
		
		if(messageSeparatedProtocol.length < 2) {   // Deve ter o query length e a query. 
			return false;
		}
		else 
			if(messageSeparatedProtocol.length > 2) {  // Se for encontrao o separador : no nome do filme. Ex.: "2001: Uma Odisséia no Espaço".
				
				for (int i = 1; i < messageSeparatedProtocol.length; i++) {
					query += messageSeparatedProtocol[i] + (i < messageSeparatedProtocol.length - 1 ? ":" : Constants.STRING_EMPTY);
				}
			}
		
		int queryLength = -1;
		
		try {
			
			queryLength = Integer.parseInt(messageSeparatedProtocol[0]);
			
		} catch (NumberFormatException e) {
			
			return false;
		}
		
		if(query.equals(Constants.STRING_EMPTY)) {
			
			query = messageSeparatedProtocol[1];
		}
		
		return queryLength == query.length();
	}
}
