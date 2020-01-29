package com.dgtprm.nkt10.movieapp.presenter;

import android.util.Log;

import com.dgtprm.nkt10.movieapp.fragment.IOverviewFragment;
import com.dgtprm.nkt10.movieapp.models.MovieData;
import com.dgtprm.nkt10.movieapp.models.Reviews;
import com.dgtprm.nkt10.movieapp.models.Trailers;
import com.dgtprm.nkt10.movieapp.network.RequestService;
import com.dgtprm.nkt10.movieapp.network.ReviewListRequestService;
import com.dgtprm.nkt10.movieapp.network.TrailerListRequestService;
import com.dgtprm.nkt10.movieapp.sql.AsyncDatabaseUpdateHandler;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class OverviewFrPresenter implements AsyncDatabaseUpdateHandler.Callback {
    private static final String TAG = "OverviewFrPresenter";

    private final TrailerListRequestService mTrailersService;
    private final ReviewListRequestService mReviewsService;
    private final IOverviewFragment mView;
    private final AsyncDatabaseUpdateHandler mDatabaseUpdater;
    private CompositeSubscription mSubscriptions;

    public OverviewFrPresenter(TrailerListRequestService trailersService,
                               ReviewListRequestService reviewsService,
                               IOverviewFragment view,
                               AsyncDatabaseUpdateHandler databaseUpdater) {
        this.mTrailersService = trailersService;
        this.mReviewsService = reviewsService;
        this.mView = view;
        this.mDatabaseUpdater = databaseUpdater;
        mSubscriptions = new CompositeSubscription();
    }

    public void getTrailers() {
        mView.showPb();
        Subscription subscription = mTrailersService.execute(new RequestService.BaseCallback<Trailers>() {
            @Override
            public void onError(Throwable throwable) {
                mView.hidePb();
                mView.onFailedTrailers(throwable);
            }

            @Override
            public void onSuccess(Trailers data) {
                mView.hidePb();
                mView.trailerListFetched(data);
            }
        });

        mSubscriptions.add(subscription);
    }

    public void getReviews() {
        mView.showPb();
        Subscription subscription = mReviewsService.execute(new RequestService.BaseCallback<Reviews>() {
            @Override
            public void onError(Throwable throwable) {
                mView.hidePb();
                mView.onFailedOverviews(throwable);
            }

            @Override
            public void onSuccess(Reviews data) {
                mView.hidePb();
                mView.reviewListFetched(data);
            }
        });
        mSubscriptions.add(subscription);
    }

    public void addMovieToFavorites(MovieData.Result movie) {
        mView.disableButton();
        mDatabaseUpdater.asyncInsert(movie, this);
    }

    public void removeMovieFromFavorites(MovieData.Result movie) {
        mView.disableButton();
        mDatabaseUpdater.asyncDelete(movie, this);
    }

    public void updateFavoriteMovie(MovieData.Result movie) {
        mDatabaseUpdater.asyncUpdate(movie.getId(), movie, this);
    }

    @Override
    public void onPostAsyncDelete(Integer result, MovieData.Result item) {
        item.setFavorite(false);
        mView.favoriteMovieDeleted(result, item);
        mView.enableButton();
    }

    @Override
    public void onPostAsyncInsert(Long result, MovieData.Result item) {
        item.setFavorite(true);
        mView.favoriteMovieInserted(result, item);
        mView.enableButton();
    }

    @Override
    public void onPostAsyncUpdate(Long result, MovieData.Result item) {
        mView.favoriteMovieUpdate(result, item);
    }

    public void onDestroy() {
        mSubscriptions.unsubscribe();
    }
}
