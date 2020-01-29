package com.dgtprm.nkt10.movieapp.presenter;


import android.support.annotation.Nullable;
import android.util.Log;

import com.dgtprm.nkt10.movieapp.fragment.IMovesFragment;
import com.dgtprm.nkt10.movieapp.models.Genres;
import com.dgtprm.nkt10.movieapp.models.MovieData;
import com.dgtprm.nkt10.movieapp.network.MovieListRequestService;
import com.dgtprm.nkt10.movieapp.network.RequestService;

import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class MoviesFrPresenter {
    private static final String TAG = "MoviesFrPresenter";

    private final MovieListRequestService service;
    private final IMovesFragment view;
    private CompositeSubscription subscriptions;

    public MoviesFrPresenter(MovieListRequestService service, IMovesFragment view) {
        this.service = service;
        this.view = view;
        subscriptions = new CompositeSubscription();
    }

    public void getMovieList() {
        view.showPb();

        Subscription subscription = service.execute(new RequestService.BaseCallback<MovieData>() {
            @Override
            public void onError(Throwable throwable) {
                view.onFailed(throwable);
                view.hidePb();
            }

            @Override
            public void onSuccess(MovieData data) {
                view.movieListFetched(data);
                view.hidePb();
            }
        });

        subscriptions.add(subscription);
    }

    public void onDestroy() {
        subscriptions.unsubscribe();
    }

    public void loadPage(MovieData data, int page) {
        data.setPage(page);
        service.setPage(page);
        getMovieList();
    }

    public void loadGenres(MovieData data, @Nullable Genres genres) {
        data.setPage(1);
        service.setPage(1);
        service.setGenres(genres != null ? genres.getIdList() : null);
        getMovieList();
    }

    public void refresh(MovieData data, @Nullable Genres genres) {
        data.setPage(1);
        if (genres != null) {
            genres.clearSelection();
        }
        service.setPage(null);
        service.setGenres(null);
        getMovieList();
    }
}
