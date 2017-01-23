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

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private static final String POSTER_ROOT_URL = "https://image.tmdb.org/t/p/w300/";

    private ArrayList<Movie> movies;
    private MovieClickListener listener;

    interface MovieClickListener {
        void onMovieClickListener(Movie movie);
    }

    MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int itemType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list, parent, false));
    }

    @Override
    public int getItemCount() {
        return this.movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ViewGroup watchedStatus;
        private ImageView poster;
        private ImageView watched;

        ViewHolder(View itemView) {
            super(itemView);

            this.watchedStatus = (ViewGroup) itemView.findViewById(R.id.watched_status);
            this.poster = (ImageView) itemView.findViewById(R.id.poster);
            this.watched = (ImageView) itemView.findViewById(R.id.watched_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    MovieAdapter.this.listener.onMovieClickListener(MovieAdapter.this.movies.get(ViewHolder.this.getAdapterPosition()));
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Movie movie = this.movies.get(position);

        ImageView imageView = viewHolder.watched;

        if (movie.isWatched()) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }

        ViewGroup viewGroup = viewHolder.watchedStatus;
        if (movie.isWatched()) {
            viewGroup.setVisibility(View.VISIBLE);
        } else {
            viewGroup.setVisibility(View.GONE);
        }
        Picasso.with(viewHolder.poster.getContext()).load(POSTER_ROOT_URL + movie.getPosterPath()).into(viewHolder.poster);
    }

    void setListener(MovieClickListener listener) {
        this.listener = listener;
    }

}
