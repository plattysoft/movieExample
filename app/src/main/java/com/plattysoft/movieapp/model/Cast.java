package com.plattysoft.movieapp.model;

public class Cast {

	public String character;
	public String name;

	/*
	 * Sample response
	 * 
	 {
      "cast_id": 4,
      "character": "The Narrator",
      "credit_id": "52fe4250c3a36847f80149f3",
      "id": 819,
      "name": "Edward Norton",
      "order": 0,
      "profile_path": "/iUiePUAQKN4GY6jorH9m23cbVli.jpg"
    },
	 * 
	 */
	public String getText() {
		return character+" - "+name;
	}

}
