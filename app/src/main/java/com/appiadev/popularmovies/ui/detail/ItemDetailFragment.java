package com.appiadev.popularmovies.ui.detail;

import android.content.Intent;
import android.os.Bundle;

import com.appiadev.popularmovies.R;
import com.appiadev.popularmovies.adapter.ReviewAdapter;
import com.appiadev.popularmovies.adapter.VideosAdapter;
import com.appiadev.popularmovies.database.AppDatabase;
import com.appiadev.popularmovies.model.Movie;
import com.appiadev.popularmovies.model.ReviewResponse;
import com.appiadev.popularmovies.model.VideoResponse;
import com.appiadev.popularmovies.service.MovieDBApi;
import com.appiadev.popularmovies.service.RetrofitClient;
import com.appiadev.popularmovies.ui.list.ItemListActivity;
import com.appiadev.popularmovies.utils.AppExecutors;
import com.appiadev.popularmovies.utils.Common;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    ImageView favorite_movie;
    RecyclerView recyclerTrailers, recyclerReviews;
    Movie movie;
    private boolean isFavorite;
    private int movieId;
    private DetailItemViewModel viewModel;

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
        viewModel = ViewModelProviders.of(this).get(DetailItemViewModel.class);

        imageView = view.findViewById(R.id.thumbnail_image_header);
        nameOfMovie = view.findViewById(R.id.title);
        plotSynopsis = view.findViewById(R.id.plotsynopsis);
        userRating = view.findViewById(R.id.userrating);
        releaseDate = view.findViewById(R.id.releasedate);
        favorite_movie = view.findViewById(R.id.favorite_movie);
        recyclerTrailers = view.findViewById(R.id.recyclerTrailers);
        recyclerReviews = view.findViewById(R.id.recyclerReviews);

        Bundle bundle = getArguments();
        if(bundle.getParcelable(ARG_ITEM_ID) != null){
            movie = bundle.getParcelable(ARG_ITEM_ID);
            movieId = movie.getMovieId();
            recyclerTrailers.setLayoutManager(new LinearLayoutManager(requireContext()));
            recyclerReviews.setLayoutManager(new LinearLayoutManager(requireContext()));
            setAdditionalInformation();
            String poster = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
            AppExecutors.getExecutorInstance().getDiskIO().execute(() -> {
                isFavorite = viewModel.isFavorite(movieId);
                if (isFavorite) {
                    movie = AppDatabase.getInstance(requireContext()).movieDao().getMovie(movieId);
                    requireActivity().runOnUiThread(() -> favorite_movie.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite_black_24dp)));
                } else {
                    requireActivity().runOnUiThread(() -> favorite_movie.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite_unselected)));
                }
            });
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

    private void setAdditionalInformation() {
        if(Common.isNetworkAvailable(requireContext())){
            RetrofitClient Client = new RetrofitClient();
            MovieDBApi apiService = Client.getClient().create(MovieDBApi.class);

            Call<VideoResponse> callTrailers = apiService.getVideosByMovie(movieId,Common.MOVIEDB_API_KEY);
            callTrailers.enqueue(new Callback<VideoResponse>() {
                @Override
                public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                    recyclerTrailers.setAdapter(new VideosAdapter(requireContext(), response.body().getVideos()));
                    recyclerTrailers.smoothScrollToPosition(0);
                }

                @Override
                public void onFailure(Call<VideoResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });

            Call<ReviewResponse> call = apiService.getReviewsByMovie(movieId,Common.MOVIEDB_API_KEY);
            call.enqueue(new Callback<ReviewResponse>() {
                @Override
                public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                    recyclerReviews.setAdapter(new ReviewAdapter(requireContext(), response.body().getReviews()));
                    recyclerReviews.smoothScrollToPosition(0);
                }

                @Override
                public void onFailure(Call<ReviewResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });


        }
    }
}
