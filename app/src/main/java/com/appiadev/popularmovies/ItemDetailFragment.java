package com.appiadev.popularmovies;

import android.app.Activity;
import android.os.Bundle;

import com.appiadev.popularmovies.model.Movie;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.appiadev.popularmovies.dummy.DummyContent;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "movies";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    TextView nameOfMovie, plotSynopsis, userRating, releaseDate;
    ImageView imageView;
    Movie movie;

    public ItemDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.thumbnail_image_header);
        nameOfMovie = view.findViewById(R.id.title);
        plotSynopsis = view.findViewById(R.id.plotsynopsis);
        userRating = view.findViewById(R.id.userrating);
        releaseDate = view.findViewById(R.id.releasedate);

        Bundle bundle = getArguments();
        if(bundle.getParcelable(ARG_ITEM_ID) != null){
            movie = bundle.getParcelable(ARG_ITEM_ID);
            String poster = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
            Glide.with(this)
                    .load(poster)
                    .placeholder(R.drawable.load)
                    .into(imageView);

            nameOfMovie.setText("Name: "+movie.getOriginalTitle());
            plotSynopsis.setText("Synopsis: "+movie.getOverview());
            userRating.setText("Rating: "+ movie.getVoteAverage());
            releaseDate.setText("Date: "+ movie.getReleaseDate());
        }else{
            Toast.makeText(getContext(), "No API Data", Toast.LENGTH_SHORT).show();
        }
    }
}
