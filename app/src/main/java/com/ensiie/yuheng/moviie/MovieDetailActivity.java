package com.ensiie.yuheng.moviie;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ensiie.yuheng.moviie.database.DBHelper;
import com.ensiie.yuheng.moviie.model.Movie;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    private static final String POSTER_IMAGE_ROOT_URL = "https://image.tmdb.org/t/p/w960_and_h540_bestv2";

    private static final String EXTRA_MOVIE = "extra_movie";

    private DBHelper databaseHelper;
    private Movie movie;

    private CollapsingToolbarLayout collapsingToolbar;
    private Toolbar toolbar;
    private ImageView image;

    private CheckBox checkBoxWatched;
    private FloatingActionButton fab;

    private TextView overview;
    private ProgressBar progressBar;
    private TextView rated;
    private TextView date;

    // test case
    Movie movie1 = new Movie(278, "The Shawshank Redemption", "26262", "7.8", "1994-09-10", "/5cIUvCJQ2aNPXRCmXiOIuJJxIki.jpg", "/xBKGJQsAIeweesB79KC89FpBrVr.jpg");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        this.databaseHelper = new DBHelper(this);
        this.movie = (Movie) getIntent().getSerializableExtra(EXTRA_MOVIE);
        //this.movie = movie1;

        this.collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        this.image = (ImageView) findViewById(R.id.detail_poster_image);

        this.checkBoxWatched = (CheckBox) findViewById(R.id.watched_checkbox);
        this.fab = (FloatingActionButton) findViewById(R.id.fab_save_movie);
        this.overview = (TextView) findViewById(R.id.overview_desp);
        this.progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        this.rated = (TextView) findViewById(R.id.rated);
        this.date = (TextView) findViewById(R.id.date);

        this.fab.setOnClickListener(this.buttonClickListener);
        this.checkBoxWatched.setOnCheckedChangeListener(this.checkBoxListener);

        Picasso.with(this).load(POSTER_IMAGE_ROOT_URL + this.movie.getBackdropPath()).into(this.image);

        this.collapsingToolbar.setTitle(this.movie.getTitle());
        this.rated.setText(this.movie.getVoteAverage());
        this.overview.setText(this.movie.getOverview());

        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        try {
            this.date.setText(formatter.format(parser.parse(this.movie.getReleaseDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        new GetMovieTask().execute(this.movie.getId());
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("movie", this.movie);
    }

    View.OnClickListener buttonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (MovieDetailActivity.this.checkBoxWatched.getVisibility() == View.VISIBLE) {
                new DeleteMovieToWatchTask().execute(new Movie[]{MovieDetailActivity.this.movie});
                return;
            }
            new AddMovieToWatchTask().execute(new Movie[]{MovieDetailActivity.this.movie});
        }
    };

    CompoundButton.OnCheckedChangeListener checkBoxListener = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            MovieDetailActivity.this.movie.setWatched(isChecked);
            new UpdateMovieTask().execute(new Movie[]{MovieDetailActivity.this.movie});
        }
    };

    public class AddMovieToWatchTask extends AsyncTask<Movie, Void, Long> {
        protected Long doInBackground(Movie... params) {
            return Long.valueOf(MovieDetailActivity.this.databaseHelper.addMovie(params[0]));
        }

        protected void onPostExecute(Long res) {
            if (res.longValue() != -1) {
                MovieDetailActivity.this.fab.setImageResource(R.drawable.ic_delete_black_24dp);
                MovieDetailActivity.this.checkBoxWatched.setVisibility(View.VISIBLE);
                return;
            }
            MovieDetailActivity.this.fab.setImageResource(R.drawable.ic_add_black_24dp);
        }
    }

    public class DeleteMovieToWatchTask extends AsyncTask<Movie, Void, Integer> {
        protected Integer doInBackground(Movie... params) {
            return Integer.valueOf(MovieDetailActivity.this.databaseHelper.deleteMovie(params[0]));
        }

        protected void onPostExecute(Integer res) {
            if (res.intValue() == 0) {
                MovieDetailActivity.this.fab.setImageResource(R.drawable.ic_delete_black_24dp);
                MovieDetailActivity.this.checkBoxWatched.setVisibility(View.VISIBLE);
                return;
            }
            MovieDetailActivity.this.fab.setImageResource(R.drawable.ic_add_black_24dp);
            MovieDetailActivity.this.checkBoxWatched.setVisibility(View.INVISIBLE);
        }
    }

    public class GetMovieTask extends AsyncTask<Integer, Void, Movie> {
        protected Movie doInBackground(Integer... params) {
            return MovieDetailActivity.this.databaseHelper.getMovie(params[0].intValue());
        }

        protected void onPostExecute(Movie res) {
            if (res != null) {
                MovieDetailActivity.this.fab.setImageResource(R.drawable.ic_delete_black_24dp);
                MovieDetailActivity.this.checkBoxWatched.setVisibility(View.VISIBLE);
                MovieDetailActivity.this.checkBoxWatched.setChecked(res.isWatched());
                return;
            }
            MovieDetailActivity.this.fab.setImageResource(R.drawable.ic_add_black_24dp);
        }
    }

    public class UpdateMovieTask extends AsyncTask<Movie, Void, Integer> {
        protected Integer doInBackground(Movie... params) {
            return Integer.valueOf(MovieDetailActivity.this.databaseHelper.updateMovie(params[0]));
        }

        protected void onPostExecute(Integer res) {
        }
    }

    public static Intent newIntent(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            String text;
            if (this.checkBoxWatched.isChecked()) {
                text = String.format(getString(R.string.share_watched_movie), new Object[]{this.movie.getTitle()});
            } else {
                text = String.format(getString(R.string.share_non_watched_movie), new Object[]{this.movie.getTitle()});
            }
            Intent intent = new Intent("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.TEXT", text);
            intent.setType("text/plain");
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
