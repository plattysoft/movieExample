package com.plattysoft.movieapp.api.retrofit;

import java.util.ArrayList;
import java.util.List;

import com.plattysoft.movieapp.api.MoviesApi;
import com.plattysoft.movieapp.model.Cast;
import com.plattysoft.movieapp.model.Movie;
import com.plattysoft.movieapp.model.Movie.MovieDetailsListener;
import com.plattysoft.movieapp.model.MovieDetails;

import android.net.Uri;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MoviesApiRetrofitImpl implements MoviesApi {

    private MovieRestApi mApiService;

    public MoviesApiRetrofitImpl() {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        mApiService = restAdapter.create(MovieRestApi.class);
    }

	
	public void getMovieDetails(final Movie movie, final MovieDetailsListener listener) {
        mApiService.getMovieDetails(API_KEY, movie.getId(), new Callback<MovieDetails>() {
            @Override
            public void success(MovieDetails details, Response response) {
                listener.onMovieDetailsReceived(details);
            }
            @Override
            public void failure(RetrofitError error) {
                // Not handing error for a sample project
            }
        });
	}
	
	public void getMovieImages(final Movie movie, final MovieImagesListener listener) {
        mApiService.getMovieImages(API_KEY, movie.getId(), new Callback<MovieImages>() {
            @Override
            public void success(MovieImages movieImages, Response response) {
                // Get only MAX_IMAGES_SHOWN
                List<Uri> result = new ArrayList<Uri>();
                for (int i=0; i<MAX_IMAGES_SHOWN && i<movieImages.backdrops.size(); i++) {
                    String filePath = movieImages.backdrops.get(i).file_path;
                    Uri uri = Uri.parse(IMAGE_BASE_URL+filePath);
                    result.add(uri);
                }
                listener.onMovieImagesReady(result);
            }
            @Override
            public void failure(RetrofitError error) {
                // Not handling errors for a simple project
            }
        });
	}

    public void getMovieCast(final Movie movie, final MovieCastListener listener) {
        mApiService.getMovieCast(API_KEY, movie.getId(), new Callback<MovieCast>() {
            @Override
            public void success(MovieCast cast, Response response) {
                // Get only MAX_CAST_SHOWN
                List<Cast> result = new ArrayList<Cast>();
                for (int i=0; i<MAX_CAST_SHOWN && i<cast.cast.size(); i++) {
                    result.add(cast.cast.get(i));
                }
                listener.onMovieCastReady(result);
            }
            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

	public void getPopularMovies(final MovieListener listener) {
		mApiService.getPopularMovies(API_KEY, new Callback<PopularMovies>() {
            @Override
            public void success(PopularMovies movies, Response response) {
                listener.onMoviesReady(movies.results);
            }
            @Override
            public void failure(RetrofitError error) {
                // Not handling errors on a sample project
            }
        });
	}
}
