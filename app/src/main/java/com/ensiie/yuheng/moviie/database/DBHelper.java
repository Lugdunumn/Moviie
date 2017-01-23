package com.ensiie.yuheng.moviie.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ensiie.yuheng.moviie.model.Movie;

import java.util.ArrayList;

/**
 * Created by solael on 2017/1/22.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "db_movie";
    private static final int DATABASE_VERSION = 1;
    private static final String MOVIE = "movie";

    interface MovieColumns {
        String MOVIE_ID = "movie_id";
        String MOVIE_TITLE = "movie_title";
        String MOVIE_OVERVIEW = "movie_overview";
        String MOVIE_RATE = "movie_rate";
        String MOVIE_BACKDROP = "movie_backdrop";
        String MOVIE_DATE = "movie_date";
        String MOVIE_POSTER = "movie_poster";
        String MOVIE_WATCHED = "movie_watched";

    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE movie (movie_id INTEGER PRIMARY KEY,movie_title TEXT,movie_overview TEXT,movie_rate TEXT,movie_date TEXT,movie_poster TEXT,movie_backdrop TEXT,movie_watched INTEGER)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS movie");
        onCreate(db);
    }

    public long addMovie(Movie movie) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MovieColumns.MOVIE_ID, movie.getId());
        values.put(MovieColumns.MOVIE_TITLE, movie.getTitle());
        values.put(MovieColumns.MOVIE_OVERVIEW, movie.getOverview());
        values.put(MovieColumns.MOVIE_RATE, movie.getVoteAverage());
        values.put(MovieColumns.MOVIE_DATE, movie.getReleaseDate());
        values.put(MovieColumns.MOVIE_POSTER, movie.getPosterPath());
        values.put(MovieColumns.MOVIE_BACKDROP, movie.getBackdropPath());
        values.put(MovieColumns.MOVIE_WATCHED, movie.isWatched() ? DATABASE_VERSION : 0);
        long res = db.insert(MOVIE, null, values);
        db.close();
        return res;
    }

    public Movie getMovie(int movieId) {
        SQLiteDatabase db = getReadableDatabase();
        boolean watched = true;
        Movie movie = null;
        Cursor cursor = db.rawQuery("SELECT * FROM movie where movie_id=" + movieId, null);

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MovieColumns.MOVIE_ID));
            String title = cursor.getString(cursor.getColumnIndex(MovieColumns.MOVIE_TITLE));
            String overview = cursor.getString(cursor.getColumnIndex(MovieColumns.MOVIE_OVERVIEW));
            String voteAverage = cursor.getString(cursor.getColumnIndex(MovieColumns.MOVIE_RATE));
            String releaseDate = cursor.getString(cursor.getColumnIndex(MovieColumns.MOVIE_DATE));
            String posterPath = cursor.getString(cursor.getColumnIndex(MovieColumns.MOVIE_POSTER));
            String backdropPath = cursor.getString(cursor.getColumnIndex(MovieColumns.MOVIE_BACKDROP));
            if (cursor.getInt(cursor.getColumnIndex(MovieColumns.MOVIE_WATCHED)) != DATABASE_VERSION) {
                watched = false;
            }
            movie = new Movie(id, title, overview, voteAverage, releaseDate, posterPath, backdropPath, watched);
        }
        cursor.close();
        db.close();
        return movie;
    }

    public ArrayList<Movie> getAllMovie(int selectedSpinner) {
        String selectQuery;
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Movie> movies = new ArrayList<>();
        if (selectedSpinner == 0) {
            selectQuery = "SELECT * FROM movie";
        } else {
            selectQuery = "SELECT * FROM movie where movie_watched=" + (selectedSpinner == DATABASE_VERSION ? 0 : DATABASE_VERSION);
        }
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                movies.add(new Movie(cursor.getInt(cursor.getColumnIndex(MovieColumns.MOVIE_ID)),
                        cursor.getString(cursor.getColumnIndex(MovieColumns.MOVIE_TITLE)),
                        cursor.getString(cursor.getColumnIndex(MovieColumns.MOVIE_OVERVIEW)),
                        cursor.getString(cursor.getColumnIndex(MovieColumns.MOVIE_RATE)),
                        cursor.getString(cursor.getColumnIndex(MovieColumns.MOVIE_DATE)),
                        cursor.getString(cursor.getColumnIndex(MovieColumns.MOVIE_POSTER)),
                        cursor.getString(cursor.getColumnIndex(MovieColumns.MOVIE_BACKDROP)),
                        cursor.getInt(cursor.getColumnIndex(MovieColumns.MOVIE_WATCHED)) == DATABASE_VERSION));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return movies;
    }

    public int updateMovie(Movie movie) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MovieColumns.MOVIE_ID, movie.getId());
        values.put(MovieColumns.MOVIE_TITLE, movie.getTitle());
        values.put(MovieColumns.MOVIE_OVERVIEW, movie.getOverview());
        values.put(MovieColumns.MOVIE_RATE, movie.getVoteAverage());
        values.put(MovieColumns.MOVIE_DATE, movie.getReleaseDate());
        values.put(MovieColumns.MOVIE_POSTER, movie.getPosterPath());
        values.put(MovieColumns.MOVIE_BACKDROP, movie.getBackdropPath());
        values.put(MovieColumns.MOVIE_WATCHED, movie.isWatched() ? DATABASE_VERSION : 0);
        String[] strArr = new String[DATABASE_VERSION];
        strArr[0] = String.valueOf(movie.getId());
        int res = db.update(MOVIE, values, "movie_id = ?", strArr);
        db.close();
        return res;
    }

    public int deleteMovie(Movie movie) {

        SQLiteDatabase db = getWritableDatabase();
        String[] strArr = new String[DATABASE_VERSION];
        strArr[0] = String.valueOf(movie.getId());
        int ret = db.delete(MOVIE, "movie_id = ?", strArr);
        db.close();
        return ret;
    }
}
