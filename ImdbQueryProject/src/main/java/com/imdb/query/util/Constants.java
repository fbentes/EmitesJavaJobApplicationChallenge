/**
 * 
 */
package com.imdb.query.util;

/**
 * @author Fábio Bentes
 * 
 * Classe responsável por todas as constantes da solução.
 *
 */
public class Constants {
	
	public static final int PORT = 2021;
	
	public static final String IP_SERVER = "127.0.0.1";
	
	public static final String IMDb_MOVIES_URL = "https://www.imdb.com/chart/top/?ref_=wl_expl_1";
	
	public static final String PREFIX_PROTOCOL = "IMDb_";
	
	public static final String SUFIX_PROTOCOL = "_IMDb";

	public static final String REGEX_PATTERN_PROTOCOL = PREFIX_PROTOCOL + "(.+?)" + SUFIX_PROTOCOL;
	
	public static final String IVALID_MESSAGE_PROTOCOL = "Protocolo de comunicação inválido";
}