package com.appiadev.popularmovies.ui.detail;

import android.content.Intent;
import android.os.Bundle;

import com.appiadev.popularmovies.R;
import com.appiadev.popularmovies.model.Movie;
import com.appiadev.popularmovies.ui.detail.videoAndReview.VideoAndReviewActivity;
import com.appiadev.popularmovies.ui.list.ItemListActivity;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    ImageView favorite_movie, play_videos, review_videos;
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
        favorite_movie = view.findViewById(R.id.favorite_movie);
        play_videos = view.findViewById(R.id.play_videos);
        review_videos = view.findViewById(R.id.review_videos);

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

            play_videos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), VideoAndReviewActivity.class).putExtra("id",movie.getMovieId()).putExtra("type","video"));
                }
            });

            review_videos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), VideoAndReviewActivity.class).putExtra("id",movie.getMovieId()).putExtra("type","review"));
                }
            });
        }else{
            Toast.makeText(getContext(), "No API Data", Toast.LENGTH_SHORT).show();
        }
    }
}
