package com.appiadev.popularmovies.service;

import com.appiadev.popularmovies.model.MoviesResponse;
import com.appiadev.popularmovies.model.ReviewResponse;
import com.appiadev.popularmovies.model.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface MovieDBApi {

     @GET("movie/popular")
     Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<VideoResponse> getVideosByMovie(
            @Path(value = "id", encoded = true) Integer id,
            @Query("api_key") String apiKey
    );

    @GET("movie/{id}/reviews")
    Call<ReviewResponse> getReviewsByMovie(
            @Path(value = "id", encoded = true) Integer id,
            @Query("api_key") String apiKey
    );
}