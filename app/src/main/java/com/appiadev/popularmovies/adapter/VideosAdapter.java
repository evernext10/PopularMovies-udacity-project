package com.appiadev.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.appiadev.popularmovies.R;
import com.appiadev.popularmovies.model.Movie;
import com.appiadev.popularmovies.model.Video;
import com.appiadev.popularmovies.ui.detail.ItemDetailActivity;
import com.bumptech.glide.Glide;

import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.MyViewHolder> {

    private Context mContext;
    private List<Video> videoList;

    public VideosAdapter(Context mContext, List<Video> movieList){
        this.mContext = mContext;
        this.videoList = movieList;
    }

    @Override
    public VideosAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.video_card, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VideosAdapter.MyViewHolder viewHolder, int i){
        viewHolder.title_video.setText(videoList.get(i).getName());
        viewHolder.type_video.setText(videoList.get(i).getType());
        viewHolder.site_video.setText(videoList.get(i).getSite());
    }

    @Override
    public int getItemCount(){
        return videoList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title_video, type_video, site_video;

        MyViewHolder(View view){
            super(view);
            title_video = view.findViewById(R.id.title_video);
            type_video = view.findViewById(R.id.type_video);
            site_video = view.findViewById(R.id.site_video);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        Video clickedDataItem = videoList.get(pos);
                        String url = "https://www.youtube.com/watch?v="+clickedDataItem.getKey();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}
