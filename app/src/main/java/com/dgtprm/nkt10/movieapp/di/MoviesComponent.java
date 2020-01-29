package com.dgtprm.nkt10.movieapp.di;

import com.dgtprm.nkt10.movieapp.fragment.MovieListFragment;
import com.dgtprm.nkt10.movieapp.fragment.MoviesFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = MoviesFragmentModule.class)
public interface MoviesComponent {
    void inject(MovieListFragment fragment);
}
