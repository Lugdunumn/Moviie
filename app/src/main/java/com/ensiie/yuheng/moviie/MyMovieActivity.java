package com.ensiie.yuheng.moviie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.ensiie.yuheng.moviie.database.DBHelper;
import com.ensiie.yuheng.moviie.model.Movie;

import java.util.ArrayList;

public class MyMovieActivity extends AppCompatActivity {

    private static final String TAG = MyMovieActivity.class.getSimpleName();
    private static final String SELECTED_SPINNER_ITEM = "selected_spinner";
    private DBHelper dbHelper;

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private Spinner spinner;

    private TextView textView;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_movie);

        this.dbHelper = new DBHelper(this);
        //disable title moviie
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        this.progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        this.textView = (TextView) findViewById(R.id.no_movie_text);
        this.spinner = (Spinner) findViewById(R.id.spinner);
        // create 3 spinner items
        String[] spinnerItems = new String[]{
                getString(R.string.spinner_all_movie),
                getString(R.string.spinner_movie_not_watched),
                getString(R.string.spinner_movie_watched)
        };
        // set spinner dropdown items
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter(this, R.layout.spinner, spinnerItems);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.spinner.setAdapter(spinnerArrayAdapter);
        this.spinner.setOnItemSelectedListener(this.spinnerItemClickListener);
        // go to MovieActivity
        ((FloatingActionButton) findViewById(R.id.add_movie_fab)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyMovieActivity.this.startActivity(new Intent(MyMovieActivity.this, MovieActivity.class));
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.span_count));
        this.recyclerView = (RecyclerView) findViewById(R.id.my_movie_rv);
        this.recyclerView.setLayoutManager(gridLayoutManager);
    }
    // keep the last selected spinner item
    protected void onResume() {
        super.onResume();
        this.spinner.setSelection(getSpinnerIndex());
        new GetMoviesTask().execute(getSpinnerIndex());
    }
    // get selected spinner position
    private int getSpinnerIndex() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPreferences.getInt(SELECTED_SPINNER_ITEM, 0);
    }
    // store spinner item position when its selected
    private void storeSpinnerIndex(int position) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPreferences.edit().putInt(SELECTED_SPINNER_ITEM, position).apply();
    }
    // click spinner item, store item position and get current category movies from datebase
    AdapterView.OnItemSelectedListener spinnerItemClickListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            MyMovieActivity.this.storeSpinnerIndex(position);
            new GetMoviesTask().execute(MyMovieActivity.this.getSpinnerIndex());
        }

        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
    // before
    public class GetMoviesTask extends AsyncTask<Integer, Void, ArrayList<Movie>> {
        protected void onPreExecute() {
            MyMovieActivity.this.progressBar.setVisibility(View.VISIBLE);
            MyMovieActivity.this.recyclerView.setVisibility(View.GONE);
            MyMovieActivity.this.textView.setVisibility(View.GONE);
        }

        protected ArrayList<Movie> doInBackground(Integer... params) {
            return MyMovieActivity.this.dbHelper.getAllMovie(params[0]);
        }

        protected void onPostExecute(ArrayList<Movie> movies) {
            MyMovieActivity.this.progressBar.setVisibility(View.GONE);
            if (movies.isEmpty()) {
                MyMovieActivity.this.textView.setVisibility(View.VISIBLE);
                return;
            }
            MyMovieActivity.this.recyclerView.setVisibility(View.VISIBLE);
            MovieAdapter adapter = new MovieAdapter(movies);
            adapter.setListener(MyMovieActivity.this.itemClickListener);
            MyMovieActivity.this.recyclerView.setAdapter(adapter);
        }
    }

    MovieAdapter.MovieClickListener itemClickListener = new MovieAdapter.MovieClickListener() {
        public void onMovieClickListener(Movie movie) {
            Log.d(TAG, "onMovieClickListener: ");
            Intent intent = new Intent(MyMovieActivity.this, MovieDetailActivity.class);
            intent.putExtra("extra_movie", movie);
            startActivity(intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


}
