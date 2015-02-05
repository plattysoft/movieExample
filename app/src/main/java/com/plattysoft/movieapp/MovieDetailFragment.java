package com.plattysoft.movieapp;

import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.plattysoft.movieapp.MoviesAdapter.MovieViewController;
import com.plattysoft.movieapp.api.MoviesApi.MovieCastListener;
import com.plattysoft.movieapp.api.MoviesApi.MovieImagesListener;
import com.plattysoft.movieapp.api.MoviesApiFactory;
import com.plattysoft.movieapp.model.Cast;
import com.plattysoft.movieapp.model.Movie;
import com.plattysoft.movieapp.model.Movie.MovieDetailsListener;
import com.plattysoft.movieapp.model.MovieDetails;
import com.squareup.picasso.Picasso;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment implements MovieImagesListener, MovieCastListener, MovieDetailsListener {

	public static final String ARG_MOVIE = "movie";
    private Movie mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_MOVIE)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = (Movie) getArguments().getSerializable(ARG_MOVIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false);
    }

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (mItem != null) {
        	// Populate the view
        	new MovieViewController(getView()).populateWithMovie(mItem);;
        	// Populate the extra fields
        	MoviesApiFactory.getInstance().getMovieCast(mItem, this);
        	mItem.getDetails(this);
        }

	}

	@Override
	public void onMovieCastReady(List<Cast> result) {
		try {
			// Get the cast and populate the entry
			TextView cast = (TextView) getView().findViewById(R.id.movieDetailsCast);
			StringBuffer sb = new StringBuffer();
			for (Cast actor : result) {
				sb.append(actor.getText());
				sb.append("\n");
			}
			cast.setText(sb.toString());
			// Now we can also ask for pictures
            MoviesApiFactory.getInstance().getMovieImages(mItem, this);
		}
		catch (NullPointerException e) {
			// It can happen that the request arrives when the fragment is no longer active (view is null)
			// We just do nothing in that case, the user is no longer interested in this information
		}
	}

	@Override
	public void onMovieImagesReady(List<Uri> result) {
		try {
			ViewGroup parent = (ViewGroup) getView().findViewById(R.id.movie_detail_layout);
			LayoutInflater inflater = LayoutInflater.from(getActivity());
			for (Uri uri : result) {
				// For each picture, create an ImageView (of certain characteristics, so better inflate it than just create it)
				ImageView imageView = (ImageView) inflater.inflate(R.layout.view_movie_image, parent, false);
				// Add it to the LinearLayout
				parent.addView(imageView);
				// And request the image
				Picasso.with(getActivity())
				.load(uri)
				.placeholder(R.drawable.ic_launcher)
				.into(imageView);
			}
		}
		catch (NullPointerException e) {
			// It can happen that the request arrives when the fragment is no longer active (view is null)
			// We just do nothing in that case, the user is no longer interested in this information
		}
	}

	@Override
	public void onMovieDetailsReceived(MovieDetails details) {
		// Populate genres and description
		try {
			TextView synopsisView = (TextView) getView().findViewById(R.id.movieSynopsis);
			synopsisView.setText(details.getSynopsis());
		}
		catch (NullPointerException e) {
			// It can happen that the request arrives when the fragment is no longer active (view is null)
			// We just do nothing in that case, the user is no longer interested in this information
		}

	}
}
