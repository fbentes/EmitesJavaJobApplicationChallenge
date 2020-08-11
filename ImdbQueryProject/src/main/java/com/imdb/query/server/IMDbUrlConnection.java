/**
 * 
 */
package com.imdb.query.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Fábio Bentes
 *
 */
public interface IMDbUrlConnection {

	String getMovieData(String movieTitle);
	
	void loadMovieLlistFromImdb();
}
