/**
 * 
 */
package com.imdb.query.server.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.imdb.query.server.IMDbUrlConnection;
import com.imdb.query.util.Constants;

/**
 * @author Fábio Bentes
 * 
 * Classe responsável pela busca dos filmes do site IMDb e 
 * armazenamento numa lista para uso pelo servidor Socket.
 *
 */
public class IMDbUrlConnectionImpl implements IMDbUrlConnection {

	private List<Object> movieList = new ArrayList<Object>();
	
	public String getMoviesFound(String movieTitle) {
		
		String result = "";
		
		for (Object title : movieList) {
			
			if(title.toString().trim().toLowerCase().startsWith(movieTitle.trim().toLowerCase())) {
				
				result += title + "\n";
			}
		}
		
		return (!result.equals("") ? result : "Nenhum filme foi encontrado como '" + movieTitle + "'");
	}
	
	public int loadMovieLlistFromImdbUrl() {
		
        URL urlIMDb;
        URLConnection urlConnection;
        BufferedReader inputUrlBufferedReader;
        
        StringBuilder strHtml = new StringBuilder();
        
		try {
			urlIMDb = new URL(Constants.IMDb_MOVIES_URL);

			urlConnection = urlIMDb.openConnection();

	        inputUrlBufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

	        String inputLine;
	        
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
        	
        	if(element.attributes().html().contains("href=\"/title/") && !element.text().trim().equals("")) {
        		
        		movieList.add(element.text());
        	}
        }
        
        movieList = movieList.stream().sorted().collect(Collectors.toList());
        
        return movieList.size();
    }
}
