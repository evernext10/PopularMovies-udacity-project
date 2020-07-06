package com.appiadev.popularmovies.ui.list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.appiadev.popularmovies.R;
import com.appiadev.popularmovies.adapter.MoviesAdapter;
import com.appiadev.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

import static com.appiadev.popularmovies.utils.Common.MOVIES_LIST;
import static com.appiadev.popularmovies.utils.Common.RECYCLER_VIEW_LAYOUT_MANAGER_STATE;

public class FavoriteListActivity extends AppCompatActivity {

    RecyclerView recycler_favorites;
    Toolbar toolbar;
    private ListMoviesViewModel viewModel;
    private ArrayList<Movie> movies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);
        viewModel = ViewModelProviders.of(this).get(ListMoviesViewModel.class);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        recycler_favorites = findViewById(R.id.recycler_favorites);
        recycler_favorites.setLayoutManager(new GridLayoutManager(this,2));

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(MOVIES_LIST)) {
                movies = savedInstanceState.getParcelableArrayList(MOVIES_LIST);
                recycler_favorites.setAdapter(new MoviesAdapter(getApplicationContext(), movies));
                recycler_favorites.smoothScrollToPosition(0);
            }
        }else{
            viewModel.getFavoriteMovies().observe(this, favorites -> {
                if (favorites != null) {
                    movies = new ArrayList<>();
                    movies.addAll(favorites);
                    recycler_favorites.setAdapter(new MoviesAdapter(getApplicationContext(), favorites));
                    recycler_favorites.smoothScrollToPosition(0);
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIES_LIST, movies);
        outState.putParcelable(RECYCLER_VIEW_LAYOUT_MANAGER_STATE, recycler_favorites.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

}
