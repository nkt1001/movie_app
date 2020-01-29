package com.dgtprm.nkt10.movieapp.network;

import android.content.Context;

import com.dgtprm.nkt10.movieapp.models.MovieData;


import rx.Observable;
import rx.Subscription;

public class SearchMovieRequestService extends RequestService<RequestService.BaseCallback<MovieData>, MovieData> {

    private final API mApi;
    private String mQuery;
    private Integer mPage;

    public SearchMovieRequestService(Context context, API api, String query, Integer page) {
        super(context);
        mApi = api;
        mPage = page;
        mQuery = query;
    }

    @Override
    public Subscription execute(BaseCallback<MovieData> callback) {
        Observable<MovieData> observable = setBackgroundSubscription(mApi.searchMovie(HttpHelper.getApiKey(), mQuery, mPage));
        return subscribe(onErrorResumeNext(observable), callback);
    }

    public void setPage(Integer page) {
        this.mPage = page;
    }

    public void setQuery(String query) {
        mQuery = query;
    }
}
