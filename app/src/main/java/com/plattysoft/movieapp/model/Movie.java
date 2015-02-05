package com.plattysoft.movieapp.model;

import java.io.Serializable;

import com.plattysoft.movieapp.api.MoviesApiFactory;
import com.plattysoft.movieapp.api.retrofit.MoviesApiRetrofitImpl;

import android.net.Uri;

public class Movie implements Serializable, Comparable<Movie> {
	
	public interface MovieDetailsListener {
		void onMovieDetailsReceived(MovieDetails details);
	}

	private static final long serialVersionUID = -2351677725648457058L;
	
	public String title;
	public double popularity;
	public String poster_path;
	public String release_date;
	public double vote_average;
	public long id;

	private MovieDetails mDetails;

	/* 
	 * Example of a JSON object with a movie
	 {
      "adult": false,
      "backdrop_path": "/cKw3HY835PMp6bzse3LMivIY5Nl.jpg",
      "id": 1884,
      "original_title": "The Ewok Adventure",
      "release_date": "1984-11-25",
      "poster_path": "/x2nKP0FCJwNLHgCyUI1cL8bF6nL.jpg",
      "popularity": 0.72905031478,
      "title": "The Ewok Adventure",
      "vote_average": 10.0,
      "vote_count": 4
    }
	*/

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getReleaseDateAsString() {
		return release_date;
	}
	
	public void getDetails(final MovieDetailsListener listener) {
		if (mDetails != null) {
			listener.onMovieDetailsReceived(mDetails);			
		}
		else {
			MoviesApiFactory.getInstance().getMovieDetails(this, new MovieDetailsListener() {
                @Override
                public void onMovieDetailsReceived(MovieDetails details) {
                    mDetails = details;
                    listener.onMovieDetailsReceived(mDetails);
                }
            });
		}
	}
	
	public double getScore() {
		return vote_average;
	}

	@Override
	public int compareTo(Movie arg0) {
		if (popularity > arg0.popularity) {
			return -1;
		}
		else if (popularity > arg0.popularity) {
			return 1;
		}
		return 0;
	}

	public Uri getThumbnailUrl() {
		return Uri.parse(MoviesApiRetrofitImpl.IMAGE_BASE_URL+ poster_path);
	}

}
