package com.dgtprm.nkt10.movieapp.di;

import android.content.Context;
import android.support.annotation.Nullable;

import com.dgtprm.nkt10.movieapp.models.Genres;
import com.dgtprm.nkt10.movieapp.network.API;
import com.dgtprm.nkt10.movieapp.network.MovieListRequestService;

import java.util.List;

import dagger.Module;
import dagger.Provides;

@Module(includes = {RetrofitModule.class, ContextModule.class})
public class MoviesFragmentModule {

    private Integer mPage = null;
    private List<Integer> mGenres = null;

    public MoviesFragmentModule() {
    }

    public MoviesFragmentModule(Integer page, @Nullable Genres genres) {
        this.mPage = page;

        if (genres != null) {
            this.mGenres = genres.getIdList();
        }
    }

    @Provides
    MovieListRequestService provideMovieListService(Context context, API api, @Nullable Integer page, @Nullable List<Integer> genres) {
        return new MovieListRequestService(context, api, page, genres);
    }

    @Provides
    @Nullable
    Integer providePage() {
        return mPage;
    }

    @Provides
    @Nullable
    List<Integer> provideGenres() {
        return mGenres;
    }
}
