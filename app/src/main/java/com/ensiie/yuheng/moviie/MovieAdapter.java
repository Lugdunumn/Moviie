package com.ensiie.yuheng.moviie;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ensiie.yuheng.moviie.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by solael on 2017/1/22.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private static final String POSTER_ROOT_URL = "https://image.tmdb.org/t/p/w300/";

    private ArrayList<Movie> movies;

    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ViewGroup seenStatus;
        private ImageView poster;
        private ImageView seen;

        public MyViewHolder(View view) {
            super(view);

            this.seenStatus = (ViewGroup) view.findViewById(R.id.seen_status);
            this.poster = (ImageView) view.findViewById(R.id.poster);
            this.seen = (ImageView) view.findViewById(R.id.seen_image);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int itemType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {
        Movie movie = (Movie) this.movies.get(position);
        ImageView imageView = myViewHolder.seen;

        if (movie.isSeen()) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }

        ViewGroup viewGroup = myViewHolder.seenStatus;
        if (movie.isSeen()) {
            viewGroup.setVisibility(View.VISIBLE);
        } else {
            viewGroup.setVisibility(View.GONE);
        }
        Picasso.with(myViewHolder.poster.getContext()).load(POSTER_ROOT_URL + movie.getPosterPath()).into(myViewHolder.poster);
    }

    public int getItemCount() {
        return this.movies.size();
    }

}
