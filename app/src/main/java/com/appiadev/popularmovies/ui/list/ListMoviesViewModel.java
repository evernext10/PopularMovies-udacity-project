package com.appiadev.popularmovies.ui.list;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.appiadev.popularmovies.database.MovieRepository;
import com.appiadev.popularmovies.model.Movie;

import java.util.List;

public class ListMoviesViewModel extends AndroidViewModel {

    private static final String TAG = ListMoviesViewModel.class.getSimpleName();
    private LiveData<List<Movie>> favMovies;

    public ListMoviesViewModel(@NonNull Application application) {
        super(application);
        MovieRepository movieRepository = new MovieRepository(application);
        Log.d(TAG, "Retrieving tasks from database via ViewModel");
        favMovies = movieRepository.loadAllFavoriteMovies();
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return favMovies;
    }
}
