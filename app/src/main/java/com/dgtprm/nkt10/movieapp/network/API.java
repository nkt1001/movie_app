package com.dgtprm.nkt10.movieapp.network;

import android.support.annotation.Nullable;

import com.dgtprm.nkt10.movieapp.models.Genres;
import com.dgtprm.nkt10.movieapp.models.MovieData;
import com.dgtprm.nkt10.movieapp.models.Reviews;
import com.dgtprm.nkt10.movieapp.models.Trailers;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface API {
    @GET("discover/movie")
    Observable<MovieData> movieList(@Query("api_key") String apiKey, @Query("sort_by") String sortBy,
                                    @Query("page") @Nullable Integer page, @Query("with_genres") @Nullable String genres);

    @GET("movie/{id}/videos")
    Observable<Trailers> trailerList(@Path("id") Long id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Observable<Reviews> reviewList(@Path("id") Long id, @Query("api_key") String apiKey);

    @GET("genre/movie/list")
    Observable<Genres> genresList(@Query("api_key") String apiKey);

    @GET("search/movie")
    Observable<MovieData> searchMovie(@Query("api_key") String apiKey, @Query("query") String query,
                                      @Query("page") @Nullable Integer page);
//
//    @GET("movie/{movie_id}")
//    Observable getMovieInfo(@Path("id") Long id, @Query("api_key") String apiKey);
}
