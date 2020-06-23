package com.appiadev.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.appiadev.popularmovies.R;
import com.appiadev.popularmovies.model.Review;
import com.appiadev.popularmovies.model.Video;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Review> reviewList;

    public ReviewAdapter(Context mContext, List<Review> movieList){
        this.mContext = mContext;
        this.reviewList = movieList;
    }

    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.review_card, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReviewAdapter.MyViewHolder viewHolder, int i){
        viewHolder.autor.setText(reviewList.get(i).getAuthor());
        viewHolder.text.setText(reviewList.get(i).getContent());
    }

    @Override
    public int getItemCount(){
        return reviewList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView autor, text;

        MyViewHolder(View view){
            super(view);
            autor = view.findViewById(R.id.autor);
            text = view.findViewById(R.id.text);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        Review clickedDataItem = reviewList.get(pos);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(clickedDataItem.getUrl()));
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}
