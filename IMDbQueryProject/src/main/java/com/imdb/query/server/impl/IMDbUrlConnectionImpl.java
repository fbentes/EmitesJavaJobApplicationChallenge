package com.imdb.query.server.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.inject.Singleton;
import com.imdb.query.server.IMDbUrlConnection;
import com.imdb.query.util.Constants;

/**
 * Responsável pela busca dos filmes do site IMDb e armazenamento numa lista  
 * em cache usada pelo servidor Socket para o atendimento das requisições dos clientes.
 *
 * @author Fábio Bentes
 * @version 1.0.0.0
 * @since 09/08/2020
 * 
 */
@Singleton
public class IMDbUrlConnectionImpl implements IMDbUrlConnection {

    private static final Logger logger = LogManager.getLogger(IMDbUrlConnectionImpl.class);

	private List<Object> movieList = new ArrayList<>();
	
	@Override
	public String getMoviesFound(String movieTitle) {
		
		Optional<String> optionalMovieTitle = Optional.ofNullable(movieTitle);
		
		if(!optionalMovieTitle.isPresent()) {
			return Constants.STRING_EMPTY;
		}
		
		StringBuilder result = new StringBuilder();

		// Preenche o StringBuilder result apenas com os filmes encontrados (já ordenados na origem) !
		
		movieList.
		stream().
		filter(title -> 
		title.toString().trim().toLowerCase().startsWith(optionalMovieTitle.get().trim().toLowerCase())).
		forEach(title -> result.append(title + "\n"));
		
		return (result.length() > 0 ? 
				result.toString() : 
					"Nenhum filme foi encontrado com o título '" + optionalMovieTitle.get().trim() + "' !");
	}
	
	@Override
	public int fillMovieListFromImdbUrl() {
		
        URL urlIMDb;
        URLConnection urlConnection;
        BufferedReader inputUrlBufferedReader;
        
        StringBuilder strHtml = new StringBuilder();
        
		try {
			urlIMDb = new URL(Constants.IMDb_MOVIES_URL);

			urlConnection = urlIMDb.openConnection();

	        inputUrlBufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

	        String inputLine;
	        
			// PS: Não usado Optional para não afetar o desempenho na iteração.

	        while ((inputLine = inputUrlBufferedReader.readLine()) != null) {
	        	
	        	strHtml.append(inputLine);
	        	strHtml.append(System.getProperty("line.separator"));
	        }
	       
	        inputUrlBufferedReader.close();
        
		} catch (Exception e) {
			logger.error("Problema ao conectar na url " + Constants.IMDb_MOVIES_URL + ". Erro: " + e.getMessage());
		}
        
        Document document = Jsoup.parse(strHtml.toString());
        
        Elements elements = document.getElementsByTag("a");
        
        for (Element element : elements) {
        	
        	if(element.attributes().html().contains("href=\"/title/") && !element.text().trim().equals(Constants.STRING_EMPTY)) {
        		
        		movieList.add(element.text());
        	}
        }
        
        // Ordenação para tornar os retornos das pesquisas mais rápidas para os clientes.
        
        movieList = 
        		movieList.
        		stream().
        		sorted().
        		collect(Collectors.toList());
        
        return movieList.size();
    }
}
