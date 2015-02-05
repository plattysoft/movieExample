package com.plattysoft.movieapp;

import java.util.List;

import com.plattysoft.movieapp.model.Movie;
import com.plattysoft.movieapp.model.Movie.MovieDetailsListener;
import com.plattysoft.movieapp.model.MovieDetails;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MoviesAdapter extends BaseAdapter {

	public static class MovieViewController implements MovieDetailsListener {

		private Movie mItem;
		
		private TextView mTitleView;
		private TextView mReleaseDateView;
		private TextView mPopularityView;
		private ImageView mThumbnail;
		private TextView mGenres;

		public MovieViewController(View theView) {
			mTitleView = (TextView) theView.findViewById(R.id.movieTitle);
			mReleaseDateView = (TextView) theView.findViewById(R.id.movieReleaseDate);
			mPopularityView = (TextView) theView.findViewById(R.id.moviePopularity);
			mThumbnail = (ImageView) theView.findViewById(R.id.movieThumbnail);
			mGenres = (TextView) theView.findViewById(R.id.movieGenres);
		}

		public void populateWithMovie(Movie item) {
			mItem = item;
			
			mTitleView.setText(item.getTitle());
			mReleaseDateView.setText(item.getReleaseDateAsString());
			mPopularityView.setText(String.valueOf(item.getScore()));
			Picasso.with(mTitleView.getContext())
			.load(item.getThumbnailUrl())
			.placeholder(R.drawable.ic_launcher)
			.into(mThumbnail);
			
			item.getDetails(this);
		}

		@Override
		public void onMovieDetailsReceived(MovieDetails details) {
			if (details.getId() == mItem.getId()) {
				mGenres.setText(details.getGenres());
			}
		}
	}

	private List<Movie> mList;
	private LayoutInflater mInflater;

	public MoviesAdapter(Context context, List<Movie> result) {
		mList = result;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Movie getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View theView = convertView;
		if (theView == null) {
			// Inflate the view and create a view holder
			theView = mInflater.inflate(R.layout.list_item_movie, null);
			theView.setTag(new MovieViewController(theView));
			
		}
		MovieViewController holder = (MovieViewController) theView.getTag();
		// Get the view holder and populate the items
		Movie item = getItem(position);
		holder.populateWithMovie (item);
		return theView; 
	}
	
}