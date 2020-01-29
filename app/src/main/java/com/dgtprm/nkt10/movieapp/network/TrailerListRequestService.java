package com.dgtprm.nkt10.movieapp.network;

import android.content.Context;

import com.dgtprm.nkt10.movieapp.models.MovieData;
import com.dgtprm.nkt10.movieapp.models.Trailers;

import rx.Observable;
import rx.Subscription;

public class TrailerListRequestService extends RequestService<RequestService.BaseCallback<Trailers>, Trailers> {

    private MovieData.Result mItem;
    private final API mApi;

    public TrailerListRequestService(API api, Context context, MovieData.Result item) {
        super(context);
        mApi = api;
        mItem = item;
    }


    @Override
    public Subscription execute(BaseCallback<Trailers> callback) {
        Observable<Trailers> observable = setBackgroundSubscription(mApi.trailerList(mItem.getId(), HttpHelper.getApiKey()));
        return subscribe(onErrorResumeNext(observable), callback);
    }

    public void setmItem(MovieData.Result item) {
        this.mItem = item;
    }
}
