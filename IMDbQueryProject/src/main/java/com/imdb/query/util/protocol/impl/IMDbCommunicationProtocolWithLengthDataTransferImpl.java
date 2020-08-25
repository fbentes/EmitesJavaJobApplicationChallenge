package com.imdb.query.util.protocol.impl;

import com.imdb.query.util.Constants;
import com.imdb.query.util.protocol.IMDbCommunicationProtocol;

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

	public static void main(String[] args) {
		
		IMDbCommunicationProtocolWithLengthDataTransferImpl iMDbCommunication = new IMDbCommunicationProtocolWithLengthDataTransferImpl();
		
		String s = "6:batman";
		String s1 = "33:3 Idiotas\\nO 3 Homem\\nToy Story 3";
		
		String s2 = "7:batman";
		String s3 = "35:3 Idiotas\\nO 3 Homem\\nToy Story 3";

		String s4 = "28:2001: Uma Odisséia no Espaço";
		String s5 = "2001: Uma Odisséia no Espaço";
		
		System.out.println(iMDbCommunication.isMatchPatternProtocol(s));
		System.out.println(iMDbCommunication.isMatchPatternProtocol(s1));

		System.out.println(!iMDbCommunication.isMatchPatternProtocol(s2));
		System.out.println(!iMDbCommunication.isMatchPatternProtocol(s3));

		System.out.println(iMDbCommunication.isMatchPatternProtocol(s4));
		System.out.println(!iMDbCommunication.isMatchPatternProtocol(s5));
		
		String s6 = iMDbCommunication.getMessageWithOutPatternProtocolApplied(s4);
		
		System.out.println(s6);
	}
}
