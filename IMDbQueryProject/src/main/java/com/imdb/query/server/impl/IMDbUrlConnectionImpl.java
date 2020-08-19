package com.imdb.query.server.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.inject.Singleton;
import com.imdb.query.server.IMDbUrlConnection;
import com.imdb.query.util.Constants;

/**
 * Respons�vel pela busca dos filmes do site IMDb e armazenamento numa lista  
 * em cache usada pelo servidor Socket para o atendimento das requisi��es dos clientes.
 *
 * @author F�bio Bentes
 * @version 1.0.0.0
 * @since 09/08/2020
 * 
 */
@Singleton
public class IMDbUrlConnectionImpl implements IMDbUrlConnection {

	private List<Object> movieList = new ArrayList<Object>();
	
	@Override
	public String getMoviesFound(String movieTitle) {
		
		Optional<String> optionalMovieTitle = Optional.ofNullable(movieTitle);
		
		if(!optionalMovieTitle.isPresent()) {
			return Constants.STRING_EMPTY;
		}
		
		StringBuffer result = new StringBuffer();

		for (Object title : movieList) {
			
			if(title.toString().trim().toLowerCase().startsWith(optionalMovieTitle.get().trim().toLowerCase())) {
				
				result.append(title + "\n");
			}
		}
		
		return (result.length() > 0 ? 
				result.toString() : 
					"Nenhum filme foi encontrado com o t�tulo '" + optionalMovieTitle.get() + "' !");
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
	        
			// PS: N�o usado Optional para n�o afetar o desempenho na itera��o.

	        while ((inputLine = inputUrlBufferedReader.readLine()) != null) {
	        	
	        	strHtml.append(inputLine);
	        	strHtml.append(System.getProperty("line.separator"));
	        }
	       
	        inputUrlBufferedReader.close();
        
		} catch (Exception e) {
			System.out.println("Problema ao conectar na url " + Constants.IMDb_MOVIES_URL + ". Erro: " + e.getMessage());
		}
        
        Document document = Jsoup.parse(strHtml.toString());
        
        Elements elements = document.getElementsByTag("a");
        
        for (Element element : elements) {
        	
        	if(element.attributes().html().contains("href=\"/title/") && !element.text().trim().equals(Constants.STRING_EMPTY)) {
        		
        		movieList.add(element.text());
        	}
        }
        
        // Ordena��o para tornar os retornos das pesquisas mais r�pidos para os clientes.
        
        movieList = movieList.stream().sorted().collect(Collectors.toList());
        
        return movieList.size();
    }
}