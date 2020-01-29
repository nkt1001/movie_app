package com.dgtprm.nkt10.movieapp.di;

import android.content.Context;

import com.dgtprm.nkt10.movieapp.network.API;
import com.dgtprm.nkt10.movieapp.network.GetGenresRequestService;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ContextModule.class, RetrofitModule.class})
public class FilterModule {

    @Provides
    GetGenresRequestService provideGenresServies(Context context, API api) {
        return new GetGenresRequestService(context, api);
    }
}
