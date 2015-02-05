package com.plattysoft.movieapp.api;

import android.net.Uri;
import android.os.AsyncTask;

import com.plattysoft.movieapp.model.Cast;
import com.plattysoft.movieapp.model.Movie;
import com.plattysoft.movieapp.model.MovieDetails;
import com.plattysoft.movieapp.model.MovieGenre;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoviesApiSimpleImpl implements MoviesApi {

    private static final String POPULAR_MOVIES_PATH = "/movie/popular";

    // movie/popular to find movies
    // movie/{id} to get extra info, like genres
    // movie/{id}/credits To get the cast
    // movie/{id}/images To get the images

    public void getMovieDetails(final Movie movie, final Movie.MovieDetailsListener listener) {
        new AsyncTask<Void, Void, MovieDetails>() {
            @Override
            protected MovieDetails doInBackground(Void... params) {
                return getMovieDetails(movie);
            }
            @Override
            protected void onPostExecute(MovieDetails result) {
                listener.onMovieDetailsReceived(result);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    protected MovieDetails getMovieDetails(Movie movie) {
        JSONObject jsonResult = makeCall("/movie/"+movie.getId());

        MovieDetails movieDetails = new MovieDetails();

        try {
            movieDetails.overview = jsonResult.getString("overview");
            JSONArray jsonGenres = jsonResult.getJSONArray("genres");
            movieDetails.genres = new ArrayList<MovieGenre>();
            for (int i = 0; i < jsonGenres.length(); i++) {
                MovieGenre currentGenre = new MovieGenre();
                currentGenre.name = jsonGenres.getJSONObject(i).getString("name");
                movieDetails.genres.add(currentGenre);
            }
            movieDetails.id = jsonResult.getLong("id");
        }
        catch (JSONException e) {
            // On the case of an exception the object stays unitinialized
        }

        return movieDetails;
    }

    public void getMovieImages(final Movie movie, final MovieImagesListener listener) {
        // Run the request on an AsyncTask to not block the UI
        new AsyncTask<Void, Void, List<Uri>>() {
            @Override
            protected List<Uri> doInBackground(Void... params) {
                return getMovieImages(movie);
            }
            @Override
            protected void onPostExecute(List<Uri> result) {
                listener.onMovieImagesReady(result);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    protected List<Uri> getMovieImages(Movie movie) {
        JSONObject jsonResult = makeCall("/movie/"+movie.getId()+"/images");
        List<Uri> result = new ArrayList<Uri>();
        try {
            JSONArray results = jsonResult.getJSONArray("backdrops");
            // For each movie create an object
            for (int i=0; i<results.length() && i<MAX_IMAGES_SHOWN;  i++) {
                String filePath = results.getJSONObject(i).getString("file_path");
                result.add(Uri.parse(IMAGE_BASE_URL+filePath));
            }
        } catch (JSONException e) {
            // If the JSON is incorrect, just stop parsing, the JSON should always be correct
        }
        return result;
    }

    public void getMovieCast(final Movie movie, final MovieCastListener listener) {
        // Run the request on an AsyncTask to not block the UI
        new AsyncTask<Void, Void, List<Cast>>() {
            @Override
            protected List<Cast> doInBackground(Void... params) {
                return getMovieCast(movie);
            }
            @Override
            protected void onPostExecute(List<Cast> result) {
                listener.onMovieCastReady(result);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    protected List<Cast> getMovieCast(Movie movie) {
        JSONObject jsonResult = makeCall("/movie/"+movie.getId()+"/credits");
        List<Cast> result = new ArrayList<Cast>();
        try {
            JSONArray results = jsonResult.getJSONArray("cast");
            // For each movie create an object
            for (int i=0; i<results.length() && i<MAX_CAST_SHOWN;  i++) {
                JSONObject jsonCast = results.getJSONObject(i);

                Cast cast = new Cast();
                cast.character = jsonCast.getString("character");
                cast.name = jsonCast.getString("name");

                result.add(cast);
            }
        } catch (JSONException e) {
            // If the JSON is incorrect, just stop parsing, the JSON should always be correct
        }
        return result;
    }

    public void getPopularMovies(final MovieListener listener) {
        // Run the request on an AsyncTask to not block the UI
        new AsyncTask<Void, Void, List<Movie>>() {
            @Override
            protected List<Movie> doInBackground(Void... params) {
                return getPopularMovies();
            }
            @Override
            protected void onPostExecute(List<Movie> result) {
                listener.onMoviesReady(result);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    protected List<Movie> getPopularMovies() {
        JSONObject jsonResult = makeCall(POPULAR_MOVIES_PATH);
        List<Movie> result = new ArrayList<Movie>();
        try {
            JSONArray results = jsonResult.getJSONArray("results");
            // For each movie create an object
            for (int i=0; i<results.length(); i++) {
                Movie movie = new Movie();
                JSONObject jsonMovie = results.getJSONObject(i);

                try {
                    movie.id = jsonMovie.getLong("id");
                    movie.poster_path = jsonMovie.getString("poster_path");
                    movie.title = jsonMovie.getString("title");
                    movie.popularity = jsonMovie.getDouble("popularity");
                    movie.vote_average = jsonMovie.getDouble("vote_average");
                    movie.release_date = jsonMovie.getString("release_date");
                } catch (JSONException e) {
                    // IF some field is not how it should be, we just stop populating the oject and return
                }
                result.add(movie);
            }
            // Now sort the list by popularity
            Collections.sort(result);
        } catch (JSONException e) {
            // If the JSON is incorrect, just stop parsing, the JSON should always be correct
        }
        return result;
    }

    protected JSONObject makeCall (String path) {
        try {
            // Prepare the request
            HttpRequestBase request = new HttpGet(API_ENDPOINT+path+"?api_key="+API_KEY);
            HttpParams httpParameters = new BasicHttpParams();
            HttpClient httpClient = new DefaultHttpClient(httpParameters);

            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            // Evaluate the response code
            switch (statusCode) {
                case HttpURLConnection.HTTP_OK:{
                    // Parse the result
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                    StringBuilder builder = new StringBuilder();
                    for (String line = null; (line = reader.readLine()) != null;) {
                        builder.append(line).append("\n");
                    }
                    JSONTokener tokener = new JSONTokener(builder.toString());
                    JSONObject finalResult = new JSONObject(tokener);
                    // Parse the response
                    return finalResult;
                }
                default:
                    // TODO: Handle all the different http errors and act accordingly (not doing it for a simple app)
                    return null;
            }
        }
        catch (IOException e) {
            // TODO: Handle the case for network error (not doing it for a sample app)
        }
        catch (Exception e) {
            // TODO: Handle other special cases (not doing it for a sample app)
        }
        return null;
    }
}
