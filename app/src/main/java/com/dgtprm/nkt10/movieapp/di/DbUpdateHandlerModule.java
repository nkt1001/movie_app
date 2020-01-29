package com.dgtprm.nkt10.movieapp.di;

import android.content.Context;

import com.dgtprm.nkt10.movieapp.sql.AsyncDatabaseUpdateHandler;
import com.dgtprm.nkt10.movieapp.sql.DBManager;

import dagger.Module;
import dagger.Provides;

@Module(includes = DbModule.class)
public class DbUpdateHandlerModule {

    @Provides
    AsyncDatabaseUpdateHandler provideHandler(Context context, DBManager manager) {
        return new AsyncDatabaseUpdateHandler(context, manager);
    }
}
