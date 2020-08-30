package com.imdb.query.entity;

/**
 * Entidade que representa o filme retornado do IMDb. 
 * 
 * @author Fábio Bentes
 * @version 1.0.0.1
 * @since 28/08/2020
 *
 */
public class Movie {

	private String imdbID;
	private String Title;
	private String Year;
	private String Type;
	
	public String getImdbID() {
		return imdbID;
	}
	
	public void setImdbID(String imdbID) {
		this.imdbID = imdbID;
	}
	
	public String getTitle() {
		return Title;
	}
	
	public void setTitle(String title) {
		this.Title = title;
	}
	
	public String getYear() {
		return Year;
	}
	
	public void setYear(String year) {
		this.Year = year;
	}
	
	public String getType() {
		return Type;
	}

	public void setType(String type) {
		this.Type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((imdbID == null) ? 0 : imdbID.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Movie other = (Movie) obj;
		if (imdbID == null) {
			if (other.imdbID != null)
				return false;
		} else if (!imdbID.equals(other.imdbID))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		
		return Title;
	}
}
