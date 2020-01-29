package com.dgtprm.nkt10.movieapp.di;

import android.content.Context;

import com.dgtprm.nkt10.movieapp.models.MovieData;
import com.dgtprm.nkt10.movieapp.network.API;
import com.dgtprm.nkt10.movieapp.network.ReviewListRequestService;
import com.dgtprm.nkt10.movieapp.network.TrailerListRequestService;

import dagger.Module;
import dagger.Provides;

@Module(includes = {RetrofitModule.class, ContextModule.class})
public class OverviewFragmentModule {

//    private Context mContext;
//    private API mApi;
    private MovieData.Result mItem;

    public OverviewFragmentModule(MovieData.Result item) {
        this.mItem = item;
    }

    @Provides
    TrailerListRequestService provideTrailerListService(Context context, API api, MovieData.Result item) {
        return new TrailerListRequestService(api, context, item);
    }

    @Provides
    ReviewListRequestService provideReviewListService(Context context, API api, MovieData.Result item) {
        return new ReviewListRequestService(api, context, item);
    }

    @Provides
    MovieData.Result provideMovie() {
        return mItem;
    }
}
