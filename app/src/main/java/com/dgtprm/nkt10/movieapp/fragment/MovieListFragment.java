package com.dgtprm.nkt10.movieapp.fragment;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.dgtprm.nkt10.movieapp.App;
import com.dgtprm.nkt10.movieapp.R;
import com.dgtprm.nkt10.movieapp.di.ContextModule;
import com.dgtprm.nkt10.movieapp.di.DaggerMoviesComponent;
import com.dgtprm.nkt10.movieapp.di.MoviesComponent;
import com.dgtprm.nkt10.movieapp.di.MoviesFragmentModule;
import com.dgtprm.nkt10.movieapp.di.RetrofitModule;
import com.dgtprm.nkt10.movieapp.dialog.FilterDialogController;
import com.dgtprm.nkt10.movieapp.models.Genres;
import com.dgtprm.nkt10.movieapp.models.MovieData;
import com.dgtprm.nkt10.movieapp.network.HttpHelper;
import com.dgtprm.nkt10.movieapp.network.MovieListRequestService;
import com.dgtprm.nkt10.movieapp.presenter.MoviesFrPresenter;
import com.dgtprm.nkt10.movieapp.sql.FavoriteMoviesLoader;

import java.util.ArrayList;

import javax.inject.Inject;

public class MovieListFragment extends MoviesFragment<MoviesComponent> implements LoaderManager.LoaderCallbacks<MovieData> {

    @Inject
    MovieListRequestService mService;
//    @Inject
    private FilterDialogController mFilterDialogController;

    private MoviesFrPresenter mPresenter;

    public MovieListFragment() {
    }

    @Override
    protected int contentLayout() {
        return R.layout.fragment_item_list;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new MoviesFrPresenter(mService, this);

        mFilterDialogController = new FilterDialogController(getFragmentManager(), this);
        mFilterDialogController.tryRestoreCallback(App.makeTag(MovieListFragment.class, R.id.menu_filter));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    public void onFilterRequested() {
        mFilterDialogController.show(getGenres(), App.makeTag(MovieListFragment.class, R.id.menu_filter));
    }

    @Override
    protected void onLoadFavorites() {
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onRequestItems() {
        mPresenter.getMovieList();
    }

    @Override
    protected void onRefreshItems() {
        mPresenter.refresh(getMovieData(), getGenres());
    }

    @Override
    protected void onLoadPage(int page) {
        mPresenter.loadPage(getMovieData(), page);
    }

    @Override
    public MoviesComponent createComponent() {
        return DaggerMoviesComponent.builder().contextModule(new ContextModule(getContext()))
                .retrofitModule(new RetrofitModule(HttpHelper.getMoviesCacheDir(getContext())))
                .moviesFragmentModule(new MoviesFragmentModule(null, getGenres())).build();
    }

    @Override
    void injectDeps() {
        getComponent().inject(this);
    }

    @Override
    public Loader<MovieData> onCreateLoader(int id, Bundle args) {
        return new FavoriteMoviesLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<MovieData> loader, MovieData data) {
        if (isFavoritesData()) {
            movieListFetched(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<MovieData> loader) {
        adapter.updateDataList(new ArrayList<MovieData.Result>());
    }

    @Override
    protected void onFilterSet(Genres genres) {
        mPresenter.loadGenres(getMovieData(), getGenres());
    }
}
