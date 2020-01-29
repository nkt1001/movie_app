package com.dgtprm.nkt10.movieapp.di;

import android.content.Context;
import android.support.annotation.Nullable;

import com.dgtprm.nkt10.movieapp.network.API;
import com.dgtprm.nkt10.movieapp.network.SearchMovieRequestService;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ContextModule.class, RetrofitModule.class})
public class SearchFragmentModule {

    private String mQuery;
    private Integer mPage = null;

    public SearchFragmentModule(String query, @Nullable Integer page) {
        this.mQuery = query;
        this.mPage = page;
    }

    @Provides
    SearchMovieRequestService provideMovieListService(Context context, API api, String query, @Nullable Integer page) {
        return new SearchMovieRequestService(context, api, query, page);
    }

    @Provides
    @Nullable
    Integer providePage() {
        return mPage;
    }

    @Provides
    String provideQuery() {
        return mQuery;
    }
}
