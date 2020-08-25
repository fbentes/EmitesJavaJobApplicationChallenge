package com.imdb.query.util.protocol;

/**
 * Respons�vel pela aplica��o do protocolo de comunica��o entre o servidor e o cliente.
 * 
 * @author F�bio Bentes
 * @version 1.0.0.0
 * @since 14/08/2020
 * 
 */
public interface IMDbCommunicationProtocol {
	
	/**
	 * Padr�o de protocolo retirado na mensagem entre o cliente e servidor.
	 * 
	 * @param message Mensagem a ser retirada o protocolo. 
	 * @return Mensagem sem o protocolo aplicado.
	 */
	String getMessageWithOutPatternProtocolApplied(String message);
	
	/**
	 * Padr�o de protocolo aplicado na mensagem entre o cliente e servidor.
	 * 
	 * @param message Mensagem a ser aplicada o protocolo. 
	 * @return Mensagem com o protocolo aplicado.
	 */
	
	String getMessageWithPatternProtocolApplied(String message);
	
    /**
     * Checa se o protocolo de comunica��o entre o cliente e servidor socket
     * foi corretamente aplicado.
     * 
     * @param message Mensagem entre o cliente e servidor.
     * @return Verdadeiro se o protocolo correto foi aplicado na mensagem. Falso caso contr�rio.
     */
	boolean isMatchPatternProtocol(String message);
}