package com.dgtprm.nkt10.movieapp.di;

import com.dgtprm.nkt10.movieapp.network.API;
import com.dgtprm.nkt10.movieapp.network.HttpHelper;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RetrofitModule {
    private static final String TAG = "RetrofitModule";

    private File mCacheDir;

    public RetrofitModule(File cacheDir) {
        this.mCacheDir = cacheDir;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(HttpHelper.getBaseUrl())
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    private OkHttpClient getOkHttpClient() {
        int cacheSize = 50 * 1024 * 1024;
        Cache cache = new Cache(mCacheDir, cacheSize);

        return new OkHttpClient.Builder()
                .cache(cache)
                .build();
    }

    @Provides
    @Singleton
    API provideApiService(Retrofit retrofit) {
        return retrofit.create(API.class);
    }
}
