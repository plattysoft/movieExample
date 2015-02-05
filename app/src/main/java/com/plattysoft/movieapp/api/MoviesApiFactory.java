package com.plattysoft.movieapp.api;

import com.plattysoft.movieapp.api.retrofit.MoviesApiRetrofitImpl;

public class MoviesApiFactory {

    private static final boolean USE_RETROFIT = false;

    private static MoviesApi sInstance;

    public static MoviesApi getInstance() {
        if (sInstance == null) {
            if (USE_RETROFIT) {
                sInstance = new MoviesApiRetrofitImpl();
            }
            else {
                sInstance = new MoviesApiSimpleImpl();
            }
        }
        return sInstance;
    }
}
