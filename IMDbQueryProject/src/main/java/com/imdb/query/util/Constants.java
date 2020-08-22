package com.imdb.query.util;

/**
 * Respons�vel por todas as constantes da solu��o.
 *
 * @author F�bio Bentes
 * @version 1.0.0.0
 * @since 09/08/2020
 * 
 */
public class Constants {
	
	/**
	 * Porta default = 20222.
	 */
	public static final int PORT_DEFAULT = 20222;
	
	/**
	 * Porta m�xima dispon�vel = 65535
	 */
	public static final int PORT_TCP_MAX = 65535;
	
	/**
	 * IP default do servidor = 127.0.0.1.
	 * 
	 */
	public static final String IP_SERVER_DEFAULT = "127.0.0.1";
	
	/**
	 * Site IMDb para listagem de filmes: https://www.imdb.com/chart/top/?ref_=wl_expl_1
	 * 
	 */
	public static final String IMDb_MOVIES_URL = "https://www.imdb.com/chart/top/?ref_=wl_expl_1";
	
	public static final String PREFIX_PROTOCOL = "IMDb_";
	
	public static final String SUFIX_PROTOCOL = "_IMDb";

	public static final String REGEX_PATTERN_PROTOCOL = PREFIX_PROTOCOL + "(.+?)" + SUFIX_PROTOCOL;
	
	public static final String IVALID_MESSAGE_PROTOCOL = "O protocolo de comunica��o est� inv�lido !";

	public static final String RESPONSE_OTHER_SERVER = "O servidor respondeu com protocolo de comunica��o inv�lido !";

	public static final String IVALID_MESSAGE_CLIENT_TO_SERVER = "T�tulo de filme recebido vazio. Tente novamente !";

	public static final String STRING_EMPTY = "";
	
	public static final String INFORME_TITULO_FILME = "Informe o t�tulo de filme ou parte inicial. Ex: Bat para buscar filmes que comecem com Bat.";
	
	public static final String MOVIE_TITLE_TO_SERVER_SOCKET_COMMUNICATION_ERROR = "Problema na comunica��o do t�tulo do filme para o servidor.";
}