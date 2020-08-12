/**
 * 
 */
package com.imdb.query.server.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.imdb.query.server.IMDbUrlConnection;

/**
 * @author Fábio Bentes
 *
 */
public class IMDbUrlConnectionImpl implements IMDbUrlConnection {

	private List<String> movieList = new ArrayList<String>();
	TreeSet<String> set = new TreeSet<String>();
	
	@Override
	public String getMoviesFound(String movieTitle) {
		
		String result = "";
		
		for (String title : movieList) {
			
			if(title.trim().toLowerCase().startsWith(movieTitle.trim().toLowerCase())) {
				
				result += title + "\n";
			}
		}
		
		return (!result.equals("") ? result : "Nenhum filme foi encontrado como '" + movieTitle + "'");
	}
	
	@Override
	public int loadMovieLlistFromImdb() {
		
		String strUrl = "https://www.imdb.com/chart/top/?ref_=wl_expl_1";
		
        URL urlIMDb;
        URLConnection urlConnection;
        BufferedReader in = null;
        
        StringBuilder strHtml = new StringBuilder();
        
		try {
			urlIMDb = new URL(strUrl);

			urlConnection = urlIMDb.openConnection();

	        in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

	        String inputLine;
	        
	        while ((inputLine = in.readLine()) != null) {
	        	
	        	strHtml.append(inputLine);
	        	strHtml.append(System.getProperty("line.separator"));
	        }
	       
	        in.close();
        
		} catch (Exception e) {
			System.out.println("Problema ao conectar na url " + strUrl + ": " + e.getMessage());
		}
        
        Document document = Jsoup.parse(strHtml.toString());
        
        Elements elements = document.getElementsByTag("a");
        
        for (Element element : elements) {
        	
        	if(element.attributes().html().contains("href=\"/title/") && !element.text().trim().equals("")) {
        		
        		movieList.add(element.text());
        	}
        }
        
        movieList = movieList.stream().sorted().collect(Collectors.toList());
        
        System.out.println(movieList.toArray());
        
        return movieList.size();
    }
}
