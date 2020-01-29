package com.dgtprm.nkt10.movieapp.di;

import com.dgtprm.nkt10.movieapp.fragment.OverviewFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {DbUpdateHandlerModule.class, OverviewFragmentModule.class, ContextModule.class})
public interface OverviewComponent {
    void inject(OverviewFragment fragment);
}
