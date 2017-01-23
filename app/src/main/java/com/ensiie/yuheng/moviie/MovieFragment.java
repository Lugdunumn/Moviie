package com.ensiie.yuheng.moviie;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.ensiie.yuheng.moviie.model.Movie;
import com.ensiie.yuheng.moviie.model.MovieResponse;
import com.ensiie.yuheng.moviie.rest.ApiClient;
import com.ensiie.yuheng.moviie.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by solael on 2017/1/21.
 */

public class MovieFragment extends Fragment implements Callback<MovieResponse> {

    private static final String TAG = MovieFragment.class.getSimpleName();

    private static final String EXTRA_MOVIE_TYPE = "extra_movies_type";
    public static final int FAIL = -1;
    public static final int SUCCESS = 0;

    public static final int POPULAR_MOVIES = 1;
    public static final int TOP_RATED_MOVIES = 2;
    public static final int UPCOMING_MOVIES = 3;

    private Singleton singleton = Singleton.getINSTANCE();

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Button retryButton;

    private ArrayList<Movie> movies;
    private int moviesType;


    // API constants
    private final static String API_KEY = "911c2608d800108548785b50933d637a";
    private final static String language = "fr-FR";
    private final static int pageNumber = 1;
    private final static String region = "FR";
    private final static String sortBy = "popularity.desc";
    private final static String includeAdult = "false";
    private final static String includeVideo = "false";
    private final static String releaseYear = "2016";

    // retrofit call
    Call<MovieResponse> call;

    public static MovieFragment newInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_MOVIE_TYPE, type);
        MovieFragment fragment = new MovieFragment();
        fragment.setArguments(bundle);
        Log.d(TAG, "newInstance: ");
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.moviesType = getArguments().getInt(EXTRA_MOVIE_TYPE);
        Log.d(TAG, "onCreate: ");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie, null, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), getActivity().getResources().getInteger(R.integer.span_count));

        Log.d(TAG, "onCreateView: ");
        this.recyclerView = (RecyclerView) view.findViewById(R.id.fragment_movie_rv);
        this.recyclerView.setLayoutManager(gridLayoutManager);

        this.retryButton = (Button) view.findViewById(R.id.retry_button);
        this.retryButton.setOnClickListener(this.onClickListener);

        this.progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        if (savedInstanceState == null || savedInstanceState.getSerializable("movie") == null) {
            if (this.moviesType == POPULAR_MOVIES && this.singleton.getPopular() != null) {
                this.movies = this.singleton.getPopular();
            } else if (this.moviesType == TOP_RATED_MOVIES && this.singleton.getTopRated() != null) {
                this.movies = this.singleton.getTopRated();
            } else if (this.moviesType == UPCOMING_MOVIES && this.singleton.getUpComing() != null) {
                this.movies = this.singleton.getUpComing();
            }
            if (this.movies != null) {
                setAdapter();
            } else {
                callAPIService();
            }
        } else {
            this.movies = (ArrayList) savedInstanceState.getSerializable("movies");
            setAdapter();
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (this.movies != null) {
            outState.putSerializable("movies", this.movies);
        }
    }

    private void setAdapter() {
        this.retryButton.setVisibility(View.GONE);
        this.progressBar.setVisibility(View.GONE);
        MovieAdapter adapter = new MovieAdapter(this.movies);
        adapter.setListener(movieClickListener);
        this.recyclerView.setAdapter(adapter);
        Log.d(TAG, "setAdapter: ");
    }

    MovieAdapter.MovieClickListener movieClickListener = new MovieAdapter.MovieClickListener() {
        public void onMovieClickListener(Movie movie) {
            Log.d(TAG, "onMovieClickListener: ");
            Intent intent = new Intent(getContext(), MovieDetailActivity.class);
            intent.putExtra("extra_movie", movie);
            startActivity(intent);
        }
    };

    // Click and reload
    View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            MovieFragment.this.retryButton.setVisibility(View.GONE);
            MovieFragment.this.progressBar.setVisibility(View.VISIBLE);
            MovieFragment.this.callAPIService();
        }
    };

    private void callAPIService() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        if (this.moviesType == POPULAR_MOVIES) {
            call = apiService.getPopularMovies(API_KEY, language, sortBy, includeAdult, includeVideo, pageNumber, releaseYear);
        } else if (this.moviesType == TOP_RATED_MOVIES) {
            call = apiService.getTopRatedMovies(API_KEY, language, pageNumber);
        } else {
            call = apiService.getUpcomingMovies(API_KEY, language, pageNumber, region);
        }
        call.enqueue(this);
    }

    private void connectionFail() {
        this.retryButton.setVisibility(View.VISIBLE);
        this.progressBar.setVisibility(View.GONE);
    }

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MovieFragment.FAIL:
                    MovieFragment.this.connectionFail();
                    return;
                case MovieFragment.SUCCESS:
                    MovieFragment.this.movies = (ArrayList) msg.obj;
                    if (MovieFragment.this.moviesType == POPULAR_MOVIES) {
                        MovieFragment.this.singleton.setPopular(MovieFragment.this.movies);
                    } else if (MovieFragment.this.moviesType == TOP_RATED_MOVIES) {
                        MovieFragment.this.singleton.setTopRated(MovieFragment.this.movies);
                    } else if (MovieFragment.this.moviesType == UPCOMING_MOVIES) {
                        MovieFragment.this.singleton.setUpComing(MovieFragment.this.movies);
                    }
                    MovieFragment.this.setAdapter();
                    return;
                default:
            }
        }
    };

    @Override
    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
        if (!response.isSuccessful()) {
            this.mHandler.sendEmptyMessage(MovieFragment.FAIL);
        } else {
            List<Movie> movies = response.body().getResults();
            Log.d(TAG, "Number of movies received: " + movies.size());
            Message msg = Message.obtain();
            msg.what = SUCCESS;
            msg.obj = movies;
            this.mHandler.sendMessage(msg);
        }
    }

    @Override
    public void onFailure(Call<MovieResponse> call, Throwable t) {
        this.mHandler.sendEmptyMessage(MovieFragment.FAIL);
        Log.e(TAG, "onFailure: "+ t.toString());
    }

}
