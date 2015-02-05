package com.plattysoft.movieapp.api;

import android.net.Uri;

import com.plattysoft.movieapp.model.Cast;
import com.plattysoft.movieapp.model.Movie;

import java.util.List;

public interface MoviesApi {

    int MAX_CAST_SHOWN = 10;
    int MAX_IMAGES_SHOWN = 10;

    String API_ENDPOINT = "https://api.themoviedb.org/3";
    String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w130/";

    String API_KEY = "a5c1950eaa7741716957de641c84bec2";

    public interface MovieImagesListener {
        void onMovieImagesReady(List<Uri> result);
    }

    public interface MovieCastListener {
        void onMovieCastReady(List<Cast> result);
    }

    public interface MovieListener {
        public void onMoviesReady(List<Movie> result);
    }

    void getPopularMovies(final MovieListener listener);

    void getMovieDetails(final Movie movie, final Movie.MovieDetailsListener listener);

    void getMovieImages(final Movie movie, final MovieImagesListener listener);

    void getMovieCast(final Movie movie, final MovieCastListener listener);
}
