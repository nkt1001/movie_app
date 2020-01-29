package com.dgtprm.nkt10.movieapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.dgtprm.nkt10.movieapp.SearchActivity;
import com.dgtprm.nkt10.movieapp.di.ContextModule;
import com.dgtprm.nkt10.movieapp.di.DaggerSearchFragmentComponent;
import com.dgtprm.nkt10.movieapp.di.RetrofitModule;
import com.dgtprm.nkt10.movieapp.di.SearchFragmentComponent;
import com.dgtprm.nkt10.movieapp.di.SearchFragmentModule;
import com.dgtprm.nkt10.movieapp.models.Genres;
import com.dgtprm.nkt10.movieapp.network.SearchMovieRequestService;
import com.dgtprm.nkt10.movieapp.presenter.SearchMoviePresenter;

import javax.inject.Inject;

public class SearchMoviesFragment extends MoviesFragment<SearchFragmentComponent> implements ISearchMoviesFragment {

    private static final String TAG = "SearchMoviesFragment";

    @Inject
    SearchMovieRequestService mService;

    private SearchMoviePresenter mPresenter;
    private String mQuery;

    public static SearchMoviesFragment newInstance(@NonNull String query) {
        SearchMoviesFragment fragment = new SearchMoviesFragment();
        Bundle b = new Bundle();
        b.putString(SearchActivity.EXTRA_QUERY, query);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mQuery = getArguments().getString(SearchActivity.EXTRA_QUERY);
        if (mQuery == null) {
            throw new IllegalStateException("Fragment must be created from static method");
        }

        super.onCreate(savedInstanceState);

        mPresenter = new SearchMoviePresenter(mService, this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void doSearch(String query) {
        mPresenter.doSearch(query);
    }

    @Override
    SearchFragmentComponent createComponent() {
        return DaggerSearchFragmentComponent.builder().contextModule(new ContextModule(getContext()))
                .retrofitModule(new RetrofitModule(getContext().getCacheDir()))
                .searchFragmentModule(new SearchFragmentModule(mQuery, null))
                .build();
    }

    @Override
    void injectDeps() {
        getComponent().inject(this);
    }

    @Override
    protected void onLoadFavorites() {}

    @Override
    protected void onRequestItems() {
        mPresenter.getMovieList();
    }

    @Override
    protected void onRefreshItems() {
        mPresenter.refresh();
    }

    @Override
    protected void onLoadPage(int page) {
        mPresenter.loadPage(page);
    }

    @Override
    protected void onFilterSet(Genres genres) {

    }
}
