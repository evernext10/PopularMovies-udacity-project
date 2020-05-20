package com.appiadev.popularmovies;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appiadev.popularmovies.adapter.MoviesAdapter;
import com.appiadev.popularmovies.model.Movie;
import com.appiadev.popularmovies.model.MoviesResponse;
import com.appiadev.popularmovies.service.MovieDBApi;
import com.appiadev.popularmovies.service.RetrofitClient;
import com.appiadev.popularmovies.utils.Common;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private static final String TYPE_BUT_POPULATE = "But popular";
    private static final String TYPE_AVERAGE_VOTE = "Average vote";

    ProgressDialog pd;
    private List<Movie> movieList;
    private AppCompatActivity activity = ItemListActivity.this;
    public static final String LOG_TAG = MoviesAdapter.class.getName();
    private FloatingActionMenu fab_menu;
    FloatingActionButton fab_most, fab_vote;
    RecyclerView recyclerView;
    private MoviesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        initViews();

    }


    private void initViews(){

        recyclerView = findViewById(R.id.item_list);

        movieList = new ArrayList<>();
        adapter = new MoviesAdapter(this, movieList);

        fab_menu = findViewById(R.id.fab_menu);
        fab_most = findViewById(R.id.fab_mas_popular);
        fab_vote = findViewById(R.id.fab_voto_promedio);

        fab_most.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String sortOrder = preferences.getString(
                        getString(R.string.pref_sort_order_key),
                        getString(R.string.pref_most_popular)
                );

                Log.d(LOG_TAG, "Sorting by most popular");
                loadJSON(TYPE_BUT_POPULATE);

            }
        });

        fab_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String sortOrder = preferences.getString(
                        getString(R.string.pref_sort_order_key),
                        getString(R.string.pref_most_popular)
                );

                Log.d(LOG_TAG, "Sorting by vote average");
                loadJSON(TYPE_AVERAGE_VOTE);

            }
        });


        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        checkSortOrder();

    }

    private void loadJSON(String TYPE){

        try{
            if (Common.MOVIEDB_API_KEY.isEmpty()){
                Toast.makeText(getApplicationContext(), "Por favor obtener una KEY en themoviedb.org", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                return;
            }
            if(Common.isNetworkAvailable(getBaseContext())){
                RetrofitClient Client = new RetrofitClient();
                MovieDBApi apiService = Client.getClient().create(MovieDBApi.class);
                Call<MoviesResponse> call = null;
                switch (TYPE){
                    case TYPE_BUT_POPULATE: call = apiService.getPopularMovies(Common.MOVIEDB_API_KEY);break;
                    case TYPE_AVERAGE_VOTE: call = apiService.getTopRatedMovies(Common.MOVIEDB_API_KEY);break;
                }

                call.enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                        List<Movie> movies = response.body().getResults();
                        Collections.sort(movies, Movie.BY_NAME_ALPHABETICAL);
                        recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), movies));
                        recyclerView.smoothScrollToPosition(0);
                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        Log.d("Error", t.getMessage());
                    }
                });
            }else{
            }

        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s){
        checkSortOrder();
    }
    private void checkSortOrder(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortOrder = preferences.getString(
                this.getString(R.string.pref_sort_order_key),
                this.getString(R.string.pref_most_popular)
        );
        if (sortOrder.equals(this.getString(R.string.pref_most_popular))) {
            loadJSON(TYPE_BUT_POPULATE);
        } else if (sortOrder.equals(this.getString(R.string.favorite))){
            Log.d(LOG_TAG, "Sorting by favorite");
        } else{
            Log.d(LOG_TAG, "Sorting by vote average");
            loadJSON(TYPE_AVERAGE_VOTE);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (movieList.isEmpty()){
            checkSortOrder();
        }else{

            checkSortOrder();
        }
    }

    public Activity getActivity(){
        Context context = this;
        while (context instanceof ContextWrapper){
            if (context instanceof Activity){
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;

    }


}
