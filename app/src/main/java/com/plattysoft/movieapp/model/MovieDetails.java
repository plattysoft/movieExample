package com.plattysoft.movieapp.model;

import java.io.Serializable;
import java.util.List;

public class MovieDetails implements Serializable {
	private static final long serialVersionUID = -5109512025477500358L;
	
	public String overview;
	public List<MovieGenre> genres;
	public long id;

	/*
	 * Sample API response
	 * "adult": false,
    "backdrop_path": "/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg",
    "belongs_to_collection": null,
    "budget": 63000000,
    "genres": [
        {
            "id": 28,
            "name": "Action"
        },
        {
            "id": 18,
            "name": "Drama"
        },
        {
            "id": 53,
            "name": "Thriller"
        }
    ],
    "homepage": "",
    "id": 550,
    "imdb_id": "tt0137523",
    "original_title": "Fight Club",
    "overview": "A ticking-time-bomb insomniac and a slippery soap salesman channel primal male aggression into a shocking new form of therapy. Their concept catches on, with underground \"fight clubs\" forming in every town, until an eccentric gets in the way and ignites an out-of-control spiral toward oblivion.",
    "popularity": 61151.745000000003,
    "poster_path": "/2lECpi35Hnbpa4y46JX0aY3AWTy.jpg",
    "production_companies": [
        {
            "name": "20th Century Fox",
            "id": 25
        }
    ],
    "production_countries": [
        {
            "iso_3166_1": "DE",
            "name": "Germany"
        },
        {
            "iso_3166_1": "US",
            "name": "United States of America"
        }
    ],
    "release_date": "1999-10-15",
    "revenue": 100853753,
    "runtime": 139,
    "spoken_languages": [
        {
            "iso_639_1": "en",
            "name": "English"
        }
    ],
    "status": "Released",
    "tagline": "How much can you know about yourself if you've never been in a fight?",
    "title": "Fight Club",
    "vote_average": 9.0999999999999996,
    "vote_count": 174
	 */

	public String getSynopsis() {
		return overview;
	}

	public String getGenres() {
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<genres.size(); i++) {
            sb.append(genres.get(i).name);
            sb.append(" ");
        }
        return sb.toString();
	}

	public long getId() {
		return id;
	}

}
