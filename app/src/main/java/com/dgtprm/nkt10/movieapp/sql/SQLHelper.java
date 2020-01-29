package com.dgtprm.nkt10.movieapp.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.dgtprm.nkt10.movieapp.sql.FavoriteMoviesContract.DBEntries;

/**
 * Created by nkt01 on 17.04.2016.
 */
public class SQLHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Favorites.db";
    private static final int DB_VERSION = 1;

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_MAIN_ENTRIES =
            "CREATE TABLE " + FavoriteMoviesContract.Favorites.TABLE_MAIN + " (" +
                    FavoriteMoviesContract.Favorites._ID + " INTEGER PRIMARY KEY," +
                    FavoriteMoviesContract.Favorites.COLUMN_ID + " INTEGER" + COMMA_SEP +
                    FavoriteMoviesContract.Favorites.COLUMN_TITLE + TEXT_TYPE + COMMA_SEP +
                    FavoriteMoviesContract.Favorites.COLUMN_POSTER_PATH + TEXT_TYPE +
            " )";

    private static final String SQL_CREATE_DETAIL_ENTRIES =
            "CREATE TABLE " + FavoriteMoviesContract.FavoritesDetails.TABLE_DETAIL + " (" +
                    FavoriteMoviesContract.FavoritesDetails._ID + " INTEGER" + COMMA_SEP +
                    FavoriteMoviesContract.FavoritesDetails.COLUMN_OVERVIEW + TEXT_TYPE + COMMA_SEP +
                    FavoriteMoviesContract.FavoritesDetails.COLUMN_RELEASE_DATE + TEXT_TYPE + COMMA_SEP +
                    FavoriteMoviesContract.FavoritesDetails.COLUMN_VOTE_AVERAGE + TEXT_TYPE + COMMA_SEP +
                    FavoriteMoviesContract.FavoritesDetails.COLUMN_BACKDROP_POSTER_PATH + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_MAIN_ENTRIES =
            "DROP TABLE IF EXISTS " + FavoriteMoviesContract.Favorites.TABLE_MAIN;

    private static final String SQL_DELETE_DETAIL_ENTRIES =
            "DROP TABLE IF EXISTS " + FavoriteMoviesContract.FavoritesDetails.TABLE_DETAIL;

    public static long getItemId(Uri itemUri) {
        return Long.parseLong(itemUri.getPathSegments().get(1));
    }


    public SQLHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MAIN_ENTRIES);
        db.execSQL(SQL_CREATE_DETAIL_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_MAIN_ENTRIES);
        db.execSQL(SQL_DELETE_DETAIL_ENTRIES);
    }
}
