package com.dgtprm.nkt10.movieapp.di;

import com.dgtprm.nkt10.movieapp.fragment.SearchMoviesFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = SearchFragmentModule.class)
public interface SearchFragmentComponent {
    void inject(SearchMoviesFragment fragment);
}
