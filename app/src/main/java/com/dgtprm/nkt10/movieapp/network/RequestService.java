package com.dgtprm.nkt10.movieapp.network;

import android.content.Context;
import android.util.Log;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public abstract class RequestService<C extends RequestService.BaseCallback<M>, M> {
    private final Context mContext;

    private static final String TAG = "RequestService";

    RequestService(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public abstract Subscription execute(C callback);

    Observable<M> setBackgroundSubscription(Observable<M> observable) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    Observable<M> onErrorResumeNext(Observable<M> observable) {
        return observable.onErrorResumeNext(new Func1<Throwable, Observable<? extends M>>() {
            @Override
            public Observable<? extends M> call(Throwable throwable) {
                return Observable.error(throwable);
            }
        });
    }

    Subscription subscribe(Observable<M> observable, final C callback) {
        return observable.subscribe(new Subscriber<M>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                callback.onError(e);
            }

            @Override
            public void onNext(M data) {
                callback.onSuccess(data);
            }
        });
    }

    public interface BaseCallback<M> {
        void onError(Throwable throwable);
        void onSuccess(M data);
    }
}
