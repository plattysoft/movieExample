package com.plattysoft.movieapp.api.retrofit;

import com.plattysoft.movieapp.model.MovieDetails;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface MovieRestApi {

    @GET("/movie/popular")
    void getPopularMovies (@Query("api_key") String apiKey,
                          Callback<PopularMovies> cb);

    @GET("/movie/{movieId}")
    void getMovieDetails (@Query("api_key") String apiKey,
                          @Path("movieId") long movieId,
                          Callback<MovieDetails> cb);

    @GET("/movie/{movieId}/credits")
    void getMovieCast (@Query("api_key") String apiKey,
                       @Path("movieId") long movieId,
                       Callback<MovieCast> cb);

    @GET("/movie/{movieId}/images")
    void getMovieImages(@Query("api_key") String apiKey,
                        @Path("movieId") long movieId,
                        Callback<MovieImages> cb);

}
