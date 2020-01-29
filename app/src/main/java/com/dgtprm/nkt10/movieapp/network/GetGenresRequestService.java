package com.dgtprm.nkt10.movieapp.network;

import android.content.Context;

import com.dgtprm.nkt10.movieapp.models.Genres;

import rx.Observable;
import rx.Subscription;

public class GetGenresRequestService extends RequestService<RequestService.BaseCallback<Genres>,Genres> {

    private final API mApi;

    public GetGenresRequestService(Context context, API api) {
        super(context);
        mApi = api;
    }

    @Override
    public Subscription execute(BaseCallback<Genres> callback) {
        Observable<Genres> observable = setBackgroundSubscription(mApi.genresList(HttpHelper.getApiKey()));

        return subscribe(onErrorResumeNext(observable), callback);
    }
}
