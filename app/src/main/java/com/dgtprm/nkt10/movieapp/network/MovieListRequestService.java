package com.dgtprm.nkt10.movieapp.network;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dgtprm.nkt10.movieapp.models.MovieData;

import java.util.List;

import rx.Observable;
import rx.Subscription;

public class MovieListRequestService extends RequestService<RequestService.BaseCallback<MovieData>, MovieData> {

    private final API mApi;
    private List<Integer> mGenres;
    private Integer mPage;

    public MovieListRequestService(Context context, API api, @Nullable Integer page, @Nullable List<Integer> genres) {
        super(context);
        mApi = api;
        mPage = page;
        mGenres = genres;
    }

    private static final String TAG = "MovieListRequestService";
    @Override
    public Subscription execute(BaseCallback<MovieData> callback) {

        String genres = mGenres == null ? null : mGenres.toString();

        Observable<MovieData> observable =
                setBackgroundSubscription(mApi.movieList(HttpHelper.getApiKey(),
                        HttpHelper.getSortOrderString(getContext()), mPage, trimString(genres)));

        return subscribe(onErrorResumeNext(observable), callback);
    }

    private String trimString(String genres) {
        if (genres == null) {
            return null;
        }

        int length = genres.length();
        return genres.substring(1, length - 1).replace(" ", "").trim();
    }

    public final void setGenres(List<Integer> genres) {
        this.mGenres = genres;
    }

    public void setPage(Integer page) {
        this.mPage = page;
    }
}
