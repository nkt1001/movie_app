package com.dgtprm.nkt10.movieapp.sql;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.dgtprm.nkt10.movieapp.models.MovieData;

public class FavoriteMoviesDetailLoader extends AsyncTaskLoader<MovieData.Result> {
    private final long mId;
    private DBManager mDbManager;

    public FavoriteMoviesDetailLoader(Context context, long id) {
        super(context);
        mId = id;
        mDbManager = new DBManager(context);
    }

    @Override
    public MovieData.Result loadInBackground() {
        Cursor requestCursor = mDbManager.queryItem(FavoriteMoviesContract.FavoritesDetails.buildItemUri(mId), mId);

        MovieData.Result item = null;

        if (requestCursor != null) {
            if (requestCursor.moveToFirst()) {
                int itemId = requestCursor.getInt(requestCursor.getColumnIndex(FavoriteMoviesContract.FavoritesDetails.COLUMN_ID));
                String posterPath = requestCursor.getString(requestCursor.getColumnIndex(FavoriteMoviesContract.FavoritesDetails.COLUMN_POSTER_PATH));
                String title = requestCursor.getString(requestCursor.getColumnIndex(FavoriteMoviesContract.FavoritesDetails.COLUMN_TITLE));
                String backdropPath = requestCursor.getString(requestCursor.getColumnIndex(FavoriteMoviesContract.FavoritesDetails.COLUMN_BACKDROP_POSTER_PATH));
                String overview = requestCursor.getString(requestCursor.getColumnIndex(FavoriteMoviesContract.FavoritesDetails.COLUMN_OVERVIEW));
                String release = requestCursor.getString(requestCursor.getColumnIndex(FavoriteMoviesContract.FavoritesDetails.COLUMN_RELEASE_DATE));
                String vote = requestCursor.getString(requestCursor.getColumnIndex(FavoriteMoviesContract.FavoritesDetails.COLUMN_VOTE_AVERAGE));

                item = new MovieData.Result(itemId, posterPath, title);
                item.setBackdropPath(backdropPath);
                item.setOverview(overview);
                item.setReleaseDate(release);
                item.setVoteAverage(Double.parseDouble(vote));
                item.setFavorite(true);

            }
            requestCursor.close();
        }
        return item;
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
    }

    @Override
    public void deliverResult(MovieData.Result data) {
        super.deliverResult(data);
    }
}
