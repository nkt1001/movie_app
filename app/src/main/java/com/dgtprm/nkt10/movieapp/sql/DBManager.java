package com.dgtprm.nkt10.movieapp.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.dgtprm.nkt10.movieapp.models.MovieData;

public class DBManager {
    private static final String COLUMN_ID = "_id";

    private final Context mAppContext;

    public DBManager(@NonNull Context context) {
        mAppContext = context.getApplicationContext();
    }

    private ContentValues toContentValuesMain(MovieData.Result item) {
        ContentValues cvMain = new ContentValues();
        cvMain.put(FavoriteMoviesContract.DBEntries.COLUMN_ID, item.getId());
        cvMain.put(FavoriteMoviesContract.DBEntries.COLUMN_POSTER_PATH, item.getPosterPath());
        cvMain.put(FavoriteMoviesContract.DBEntries.COLUMN_TITLE, item.getTitle());
        return cvMain;
    }

    private ContentValues toContentValuesDetails(MovieData.Result item) {
        ContentValues cvDetails = new ContentValues();
        cvDetails.put(FavoriteMoviesContract.DBEntries.COLUMN_BACKDROP_POSTER_PATH, item.getBackdropPath());
        cvDetails.put(FavoriteMoviesContract.DBEntries.COLUMN_OVERVIEW, item.getOverview());
        cvDetails.put(FavoriteMoviesContract.DBEntries.COLUMN_RELEASE_DATE, item.getReleaseDate());
        cvDetails.put(FavoriteMoviesContract.DBEntries.COLUMN_VOTE_AVERAGE, item.getVoteAverage());
        cvDetails.put(FavoriteMoviesContract.DBEntries._ID, item.getId());
        return cvDetails;
    }

    public synchronized long insertItem(MovieData.Result item) {
        Uri idUriMain = mAppContext.getContentResolver().insert(FavoriteMoviesContract.Favorites.buildDirUri(), toContentValuesMain(item));
        mAppContext.getContentResolver().insert(FavoriteMoviesContract.FavoritesDetails.buildDirUri(), toContentValuesDetails(item));

        return SQLHelper.getItemId(idUriMain);
    }

    public synchronized int updateItem(long id, MovieData.Result newItem) {
        newItem.setId(id);

        int rowsUpdated = mAppContext.getContentResolver()
                .update(FavoriteMoviesContract.Favorites.buildItemUri(id), toContentValuesMain(newItem), null, null);
        mAppContext.getContentResolver()
                .update(FavoriteMoviesContract.FavoritesDetails.buildItemUri(id), toContentValuesMain(newItem), null, null);

        return rowsUpdated;
    }

    public synchronized int deleteItem(MovieData.Result item) {

        int rowsDeleted = mAppContext.getContentResolver()
                .delete(FavoriteMoviesContract.Favorites.buildItemUri(item.getId()), null, null);
        mAppContext.getContentResolver()
                .delete(FavoriteMoviesContract.FavoritesDetails.buildItemUri(item.getId()), null, null);

        return rowsDeleted;
    }

    public synchronized Cursor queryItem(Uri uri, long id) {
        return queryItems(uri, COLUMN_ID + " = " + id, null);
    }

    public synchronized Cursor queryItems(Uri uri) {
        return queryItems(uri, null, null);
    }

    private Cursor queryItems(Uri uri, String where, String sortOrder) {
        return mAppContext.getContentResolver().query(uri, null, where, null, sortOrder);

    }

    public synchronized final void clear() {
        mAppContext.getContentResolver().delete(FavoriteMoviesContract.Favorites.buildDirUri(), null, null);
        mAppContext.getContentResolver().delete(FavoriteMoviesContract.FavoritesDetails.buildDirUri(), null, null);
    }
}
