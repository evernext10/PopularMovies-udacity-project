package com.appiadev.popularmovies.ui.detail.video;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.appiadev.popularmovies.R;
import com.appiadev.popularmovies.adapter.VideosAdapter;
import com.appiadev.popularmovies.model.VideoResponse;
import com.appiadev.popularmovies.service.MovieDBApi;
import com.appiadev.popularmovies.service.RetrofitClient;
import com.appiadev.popularmovies.utils.Common;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Toolbar toolbar;
    Integer id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = findViewById(R.id.recycler_videos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        id = getIntent().getExtras().getInt("id");
        if(Common.isNetworkAvailable(getBaseContext())){
            RetrofitClient Client = new RetrofitClient();
            MovieDBApi apiService = Client.getClient().create(MovieDBApi.class);
            Call<VideoResponse> call = apiService.getVideosByMovie(id,Common.MOVIEDB_API_KEY);

            call.enqueue(new Callback<VideoResponse>() {
                @Override
                public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {

                    recyclerView.setAdapter(new VideosAdapter(getApplicationContext(), response.body().getVideos()));
                    recyclerView.smoothScrollToPosition(0);
                }

                @Override
                public void onFailure(Call<VideoResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });
        }
    }
}
