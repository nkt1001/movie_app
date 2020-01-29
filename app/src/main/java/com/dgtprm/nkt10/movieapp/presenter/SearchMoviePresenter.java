package com.dgtprm.nkt10.movieapp.presenter;

import android.util.Log;

import com.dgtprm.nkt10.movieapp.fragment.ISearchMoviesFragment;
import com.dgtprm.nkt10.movieapp.models.MovieData;
import com.dgtprm.nkt10.movieapp.network.RequestService;
import com.dgtprm.nkt10.movieapp.network.SearchMovieRequestService;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class SearchMoviePresenter {

    private final SearchMovieRequestService mService;
    private final ISearchMoviesFragment mView;
    private CompositeSubscription subscriptions;


    public SearchMoviePresenter(SearchMovieRequestService service, ISearchMoviesFragment view) {
        mService = service;
        mView = view;
        subscriptions = new CompositeSubscription();
    }

    public void getMovieList() {
        mView.showPb();

        Subscription subscription = mService.execute(new RequestService.BaseCallback<MovieData>() {
            @Override
            public void onError(Throwable throwable) {
                mView.onFailed(throwable);
                mView.hidePb();
            }

            @Override
            public void onSuccess(MovieData data) {
                mView.movieListFetched(data);
                mView.hidePb();
            }
        });

        subscriptions.add(subscription);
    }

    public void onDestroy() {
        subscriptions.unsubscribe();
    }

    public void doSearch(String query) {
        mService.setQuery(query);
        getMovieList();
    }

    public void loadPage(int page) {
        mService.setPage(page);
        getMovieList();
    }

    public void refresh() {
        mService.setPage(null);
        getMovieList();
    }
}
