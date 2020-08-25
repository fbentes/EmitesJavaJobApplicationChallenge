package com.imdb.query.util.protocol;

/**
 * Responsável pela aplicação do protocolo de comunicação entre o servidor e o cliente.
 * 
 * @author Fábio Bentes
 * @version 1.0.0.0
 * @since 14/08/2020
 * 
 */
public interface IMDbCommunicationProtocol {
	
	/**
	 * Padrão de protocolo retirado na mensagem entre o cliente e servidor.
	 * 
	 * @param message Mensagem a ser retirada o protocolo. 
	 * @return Mensagem sem o protocolo aplicado.
	 */
	String getMessageWithOutPatternProtocolApplied(String message);
	
	/**
	 * Padrão de protocolo aplicado na mensagem entre o cliente e servidor.
	 * 
	 * @param message Mensagem a ser aplicada o protocolo. 
	 * @return Mensagem com o protocolo aplicado.
	 */
	
	String getMessageWithPatternProtocolApplied(String message);
	
    /**
     * Checa se o protocolo de comunicação entre o cliente e servidor socket
     * foi corretamente aplicado.
     * 
     * @param message Mensagem entre o cliente e servidor.
     * @return Verdadeiro se o protocolo correto foi aplicado na mensagem. Falso caso contrário.
     */
	boolean isMatchPatternProtocol(String message);
}