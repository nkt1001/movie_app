package com.dgtprm.nkt10.movieapp.di;

import com.dgtprm.nkt10.movieapp.network.GetGenresRequestService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = FilterModule.class)
public interface FilterComponent {
    GetGenresRequestService getGenresService();
}
