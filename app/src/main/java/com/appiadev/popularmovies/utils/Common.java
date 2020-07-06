package com.appiadev.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Common {
    /**
     * The constant MOVIEDB_API_KEY.
     */
    //public final static String MOVIEDB_API_KEY = "<YOUR_MOVIEDB_API_KEY>";
    public final static String MOVIEDB_API_KEY = "0d677b16a44d2b5a79edf325bc1ee9b7";

    public static final String MOVIES_LIST = "MOVIES_LIST";
    public static final String RECYCLER_VIEW_LAYOUT_MANAGER_STATE = "RECYCLER_VIEW_LAYOUT_MANAGER_STATE";

    /**
     * The constant MOVIEDB_API_URL.
     */
    public final static String MOVIEDB_API_URL = "http://api.themoviedb.org/3/";

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
