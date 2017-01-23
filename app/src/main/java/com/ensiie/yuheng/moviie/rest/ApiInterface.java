package com.ensiie.yuheng.moviie.rest;

import com.ensiie.yuheng.moviie.model.Movie;
import com.ensiie.yuheng.moviie.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by solael on 2017/1/21.
 */

public interface ApiInterface {
    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("language") String language,
                                          @Query("page") int pageNumber);

    @GET("discover/movie")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("language") String language,
                                         @Query("sort_by") String sortBy, @Query("include_adult") String includeAdult,
                                         @Query("include_video") String includeVideo, @Query("page") int pageNumber, @Query("primary_release_year") String releaseYear);

    @GET("movie/upcoming")
    Call<MovieResponse> getUpcomingMovies(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") int pageNumber, @Query("region") String region);
    /*
    @GET("movie/{id}")
    Call<MovieResponse> getMovieDetail(@Path("id") int id, @Query("api_key") String apiKey);*/

}
