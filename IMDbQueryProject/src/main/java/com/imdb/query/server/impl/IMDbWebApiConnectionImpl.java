package com.imdb.query.server.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.inject.Singleton;
import com.imdb.query.entity.Movie;
import com.imdb.query.entity.ResultFromJsonIMDbWebApi;
import com.imdb.query.server.IMDbServerSocket;
import com.imdb.query.server.IMDbUrlConnection;
import com.imdb.query.util.Constants;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Singleton
public class IMDbWebApiConnectionImpl implements IMDbUrlConnection {

    private static final Logger logger = LogManager.getLogger(IMDbWebApiConnectionImpl.class);
   
	private List<Movie> movieCacheList = new ArrayList<>();
	
	@Override
	public String getMoviesFound(String movieTitle) {

		if(movieTitle == null || movieTitle.trim().equals("")) {
			
			return Constants.STRING_EMPTY;
		}
		
		StringBuilder result = new StringBuilder();

		// Preenche o StringBuilder result apenas com os filmes encontrados !
		
		movieCacheList.
		stream().
		filter(movie -> 
				movie.toString().trim().toLowerCase().contains(movieTitle.trim().toLowerCase())).
		forEach(title -> result.append(title + "\n"));
		
		// Se não encontrar o filme no cache, busca do serviço do IMDb.
		
		if(result.length() == 0) {
			
			String moviesFromIMDbWebApi = getMoviesFromImdbWebApi(movieTitle);
			
			result.append(moviesFromIMDbWebApi);
		}
		
		return (result.length() > 0 ? 
				result.toString() : 
					"Nenhum filme foi encontrado com o título '" + movieTitle.trim() + "' !");
	}

	/**
	 * Busca o filme procurado no serviço IMDb e atualiza o cache.
	 * 
	 * @return Quantidade de filmes inseridos na lista.
	 * @see IMDbServerSocket#loadMovieLlistFromImdb()
	 */
	private String getMoviesFromImdbWebApi(String movieTitle) {

		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
			.url(String.format(Constants.IMDB_WEBAPI_MOVIES, movieTitle))
			.get()
			.addHeader("x-rapidapi-host", "movie-database-imdb-alternative.p.rapidapi.com")
			.addHeader("x-rapidapi-key", "eb03ccf8f6mshd7752e6aae152d3p1a9472jsna859698a1b1c")
			.build();

		Optional<Response> response = Optional.empty();
		
		try {
			
			response = Optional.of(client.newCall(request).execute());
			
			String json = response.get().body().string();
			
			Gson gson = new Gson();
			
			ResultFromJsonIMDbWebApi resultFromJsonIMDbWebApi = gson.fromJson(json, ResultFromJsonIMDbWebApi.class);
			
			List<Movie> movieList = Arrays.stream(resultFromJsonIMDbWebApi.Search).collect(Collectors.toList());
			
			updateMovieCacheList(movieList);
			
			StringBuilder result = new StringBuilder();

			movieList.
			stream().
			forEachOrdered(movie -> result.append(movie + "\n"));
			
			return result.toString();
			
		} catch (IOException e) {

			logger.error("Erro no preenchimento do cache pelo serviço IMDb: " + e.getMessage());
		}
		
		return Constants.STRING_EMPTY;
	}
	
	/**
	 * Atualiza o cache de filmes.
	 * 
	 * @param movieList Filmes retornados pelo serviço IMDb.
	 */
	private void updateMovieCacheList(List<Movie> movieList) {
		
		// Adiciona no cache.
		movieList.
		stream().
		forEach(movie -> {
			
			if(!movieCacheList.contains(movie)) {
			
				movieCacheList.add(movie);
			}
		});
	}
	
	public static void main(String[] args) {

//		Gson gson = new Gson();
		
//		Movie[] movieArray = gson.fromJson(jsont, Movie[].class);

		IMDbWebApiConnectionImpl iMDbWebApiConnectionImpl = new IMDbWebApiConnectionImpl();
		
		String result = iMDbWebApiConnectionImpl.getMoviesFound("Batman");
		
		System.out.println(result);
		
	}
}
