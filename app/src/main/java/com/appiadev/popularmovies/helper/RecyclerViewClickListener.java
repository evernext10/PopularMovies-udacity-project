package com.appiadev.popularmovies.helper;

import android.view.View;

import com.appiadev.popularmovies.model.Movie;

public interface RecyclerViewClickListener {
    void recyclerViewListClicked(View v, Movie currentMovie);
}
