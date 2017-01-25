package com.ensiie.yuheng.moviie.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ensiie.yuheng.moviie.model.Movie;

import java.util.ArrayList;

import static com.ensiie.yuheng.moviie.database.DBHelper.MovieColumns.MOVIE_BACKDROP;
import static com.ensiie.yuheng.moviie.database.DBHelper.MovieColumns.MOVIE_DATE;
import static com.ensiie.yuheng.moviie.database.DBHelper.MovieColumns.MOVIE_ID;
import static com.ensiie.yuheng.moviie.database.DBHelper.MovieColumns.MOVIE_OVERVIEW;
import static com.ensiie.yuheng.moviie.database.DBHelper.MovieColumns.MOVIE_POSTER;
import static com.ensiie.yuheng.moviie.database.DBHelper.MovieColumns.MOVIE_RATE;
import static com.ensiie.yuheng.moviie.database.DBHelper.MovieColumns.MOVIE_TITLE;
import static com.ensiie.yuheng.moviie.database.DBHelper.MovieColumns.MOVIE_WATCHED;

/**
 * Created by solael on 2017/1/22.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_MOVIES_NAME = "movies";
    private static final String TABLE_ACTORS_NAME = "actors";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, version);
    }

    // create db
    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    // database upgrade callback
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ConstantString.DROPTABLE + TABLE_MOVIES_NAME);
        createTable(db);
    }

    // create table movies
    private void createTable(SQLiteDatabase db) {
        createMoviesTable(db);
    }

    static class MovieColumns {
        public static final String MOVIE_ID = "movie_id";
        public static final String MOVIE_TITLE = "movie_title";
        public static final String MOVIE_OVERVIEW = "movie_overview";
        public static final String MOVIE_RATE = "movie_rate";
        public static final String MOVIE_BACKDROP = "movie_backdrop";
        public static final String MOVIE_DATE = "movie_date";
        public static final String MOVIE_POSTER = "movie_poster";
        public static final String MOVIE_WATCHED = "movie_watched";

    }

    static class ConstantString {
        public static final String CREATETABLE = "create table if not exists ";
        public static final String ID = " integer primary key";
        public static final String TEXT = " text ";
        public static final String INTEGER = " integer ";
        public static final String TEXT_NOT_NULL = "text not null";
        public static final String LEFT_PARENTHESE = "(";
        public static final String RIGHT_PARENTHESE = ")";
        public static final String COMMA = ",";
        public static final String ALTER = "alter table ";
        public static final String RENAME = " rename to ";
        public static final String INSERT = "insert into";
        public static final String DROPTABLE = "drop table if exists";
        public static final String SELECT = " select ";
        public static final String ADD = " add ";
        public static final String FROM = " from ";
        public static final String SPACE = " ";
        public static final String DEFAULT = " default ";
        public static final String STAR = " * ";
        public static final String WHERE = " where ";
        public static final String EQUAL = " = ";
    }

    public void createMoviesTable(SQLiteDatabase db) {
        StringBuffer createMoviesTableSQL = new StringBuffer();
        createMoviesTableSQL
                .append(ConstantString.CREATETABLE).append(TABLE_MOVIES_NAME)
                .append(ConstantString.LEFT_PARENTHESE)
                .append(MovieColumns.MOVIE_ID).append(ConstantString.ID).append(ConstantString.COMMA)
                .append(MovieColumns.MOVIE_TITLE).append(ConstantString.TEXT).append(ConstantString.COMMA)
                .append(MovieColumns.MOVIE_OVERVIEW).append(ConstantString.TEXT).append(ConstantString.COMMA)
                .append(MovieColumns.MOVIE_RATE).append(ConstantString.TEXT).append(ConstantString.COMMA)
                .append(MovieColumns.MOVIE_DATE).append(ConstantString.TEXT).append(ConstantString.COMMA)
                .append(MovieColumns.MOVIE_POSTER).append(ConstantString.TEXT).append(ConstantString.COMMA)
                .append(MovieColumns.MOVIE_BACKDROP).append(ConstantString.TEXT).append(ConstantString.COMMA)
                .append(MovieColumns.MOVIE_WATCHED).append(ConstantString.INTEGER)
                .append(ConstantString.RIGHT_PARENTHESE);
        db.execSQL(createMoviesTableSQL.toString());
    }

    public long addMovie(Movie movie) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MOVIE_ID, movie.getId());
        values.put(MovieColumns.MOVIE_TITLE, movie.getTitle());
        values.put(MovieColumns.MOVIE_OVERVIEW, movie.getOverview());
        values.put(MovieColumns.MOVIE_RATE, movie.getVoteAverage());
        values.put(MovieColumns.MOVIE_DATE, movie.getReleaseDate());
        values.put(MovieColumns.MOVIE_POSTER, movie.getPosterPath());
        values.put(MovieColumns.MOVIE_BACKDROP, movie.getBackdropPath());
        values.put(MovieColumns.MOVIE_WATCHED, movie.isWatched() ? 1 : 0);
        long result = db.insert(TABLE_MOVIES_NAME, null, values);
        db.close();
        return result;
    }

    public Movie getMovie(int movieID) {
        SQLiteDatabase db = getReadableDatabase();
        boolean watched = true;
        Movie movie = null;
        Cursor cursor = db.rawQuery(ConstantString.SELECT + ConstantString.STAR + ConstantString.FROM +
                TABLE_MOVIES_NAME + ConstantString.WHERE + MovieColumns.MOVIE_ID + ConstantString.EQUAL + movieID, null); // select * from movies where movie_id = movieID

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
        Log.d(TAG, "getAllMovie: " + selectedSpinner);
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Movie> movies = new ArrayList<>();
        if (selectedSpinner == 0) {
            selectQuery = ConstantString.SELECT + ConstantString.STAR + ConstantString.FROM + TABLE_MOVIES_NAME; // select * from movies
        } else {
            selectQuery = ConstantString.SELECT + ConstantString.STAR + ConstantString.FROM +
                    TABLE_MOVIES_NAME + ConstantString.WHERE + MovieColumns.MOVIE_WATCHED + ConstantString.EQUAL + (selectedSpinner == 1 ? 0 : 1);
            // select * from movies where movie_watched = 0 or 1
        }
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                movies.add(new Movie(cursor.getInt(cursor.getColumnIndex(MOVIE_ID)),
                        cursor.getString(cursor.getColumnIndex(MovieColumns.MOVIE_TITLE)),
                        cursor.getString(cursor.getColumnIndex(MovieColumns.MOVIE_OVERVIEW)),
                        cursor.getString(cursor.getColumnIndex(MovieColumns.MOVIE_RATE)),
                        cursor.getString(cursor.getColumnIndex(MovieColumns.MOVIE_DATE)),
                        cursor.getString(cursor.getColumnIndex(MovieColumns.MOVIE_POSTER)),
                        cursor.getString(cursor.getColumnIndex(MovieColumns.MOVIE_BACKDROP)),
                        cursor.getInt(cursor.getColumnIndex(MovieColumns.MOVIE_WATCHED)) == 1));
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
        values.put(MovieColumns.MOVIE_WATCHED, movie.isWatched() ? 1 : 0);

        int result = db.update(TABLE_MOVIES_NAME, values, "movie_id = ?", new String[]{ String.valueOf(movie.getId())} );
        db.close();
        return result;
    }

    public int deleteMovie(Movie movie) {

        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(TABLE_MOVIES_NAME, "movie_id = ?", new String[]{ String.valueOf(movie.getId()) });
        db.close();
        return result;
    }
}
