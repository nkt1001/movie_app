package com.dgtprm.nkt10.movieapp.sql;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.dgtprm.nkt10.movieapp.models.MovieData;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMoviesLoader extends AsyncTaskLoader<MovieData> {

    private static final String TAG = "FavoriteMoviesLoader";
    private DBManager mDbManager;

    public FavoriteMoviesLoader(Context context) {
        super(context);
        mDbManager = new DBManager(context);
    }

    @Override
    public MovieData loadInBackground() {
        List<MovieData.Result> data = new ArrayList<>();

        Cursor myFavorites = mDbManager.queryItems(FavoriteMoviesContract.Favorites.buildDirUri());

        if (myFavorites != null) {
            if (myFavorites.moveToFirst()) {
                do {
                    int itemId = myFavorites.getInt(myFavorites.getColumnIndex(FavoriteMoviesContract.Favorites.COLUMN_ID));
                    String posterPath = myFavorites.getString(myFavorites.getColumnIndex(FavoriteMoviesContract.Favorites.COLUMN_POSTER_PATH));
                    String title = myFavorites.getString(myFavorites.getColumnIndex(FavoriteMoviesContract.Favorites.COLUMN_TITLE));
                    MovieData.Result item = new MovieData.Result(itemId, posterPath, title);
                    item.setFavorite(true);

                    data.add(item);
                } while (myFavorites.moveToNext());

                myFavorites.close();
            }
        }

        MovieData movieData = new MovieData();
        movieData.setResults(data);
        movieData.setPage(1);
        movieData.setTotalPages(1);
        movieData.setTotalResults(data.size());
        return movieData;
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
    public void deliverResult(MovieData data) {
        super.deliverResult(data);
    }
}
