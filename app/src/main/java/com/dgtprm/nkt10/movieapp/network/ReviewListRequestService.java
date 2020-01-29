package com.dgtprm.nkt10.movieapp.network;

import android.content.Context;

import com.dgtprm.nkt10.movieapp.models.MovieData;
import com.dgtprm.nkt10.movieapp.models.Reviews;

import rx.Observable;
import rx.Subscription;

public class ReviewListRequestService extends RequestService<RequestService.BaseCallback<Reviews>, Reviews> {
    private MovieData.Result mItem;
    private final API mApi;


    public ReviewListRequestService(API api, Context context, MovieData.Result item) {
        super(context);
        mApi = api;
        mItem = item;
    }

    @Override
    public Subscription execute(BaseCallback<Reviews> callback) {
        Observable<Reviews> observable = setBackgroundSubscription(mApi.reviewList(mItem.getId(), HttpHelper.getApiKey()));
        return subscribe(onErrorResumeNext(observable), callback);
    }

    public void setItem(MovieData.Result item) {
        this.mItem = item;
    }
}
