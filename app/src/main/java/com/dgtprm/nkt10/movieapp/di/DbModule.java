package com.dgtprm.nkt10.movieapp.di;

import android.content.Context;

import com.dgtprm.nkt10.movieapp.sql.DBManager;

import dagger.Module;
import dagger.Provides;

@Module(includes = ContextModule.class)
public class DbModule {
    @Provides
    DBManager provideDbManager(Context context) {
        return new DBManager(context);
    }
}
