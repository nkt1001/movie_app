package com.dgtprm.nkt10.movieapp.sql;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dgtprm.nkt10.movieapp.sql.FavoriteMoviesContract.DBEntries;

import java.util.Arrays;

public class FavoriteMoviesProvider extends ContentProvider {

    private static final String TAG = "FavoriteMoviesProvider";

    private static final int URI_MAIN_TABLE = 0;
    private static final int URI_MAIN_TABLE_ITEM = 1;
    private static final int URI_DETAIL_TABLE = 2;
    private static final int URI_DETAIL_TABLE_ITEM = 3;

    private SQLHelper mHelper;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoriteMoviesContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, FavoriteMoviesContract.Favorites.PATH, URI_MAIN_TABLE);
        uriMatcher.addURI(authority, FavoriteMoviesContract.Favorites.PATH_ITEM, URI_MAIN_TABLE_ITEM);
        uriMatcher.addURI(authority, FavoriteMoviesContract.FavoritesDetails.PATH, URI_DETAIL_TABLE);
        uriMatcher.addURI(authority, FavoriteMoviesContract.FavoritesDetails.PATH_ITEM, URI_DETAIL_TABLE_ITEM);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        mHelper = new SQLHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor queryCursor;

        switch (uriMatcher.match(uri)) {
            case URI_MAIN_TABLE:
                queryCursor = mHelper.getWritableDatabase().query(FavoriteMoviesContract.Favorites.TABLE_MAIN,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case URI_MAIN_TABLE_ITEM:
                selection = DBEntries._ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                queryCursor = mHelper.getWritableDatabase()
                        .query(FavoriteMoviesContract.Favorites.TABLE_MAIN, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case URI_DETAIL_TABLE:
                queryCursor = mHelper.getWritableDatabase().query(FavoriteMoviesContract.FavoritesDetails.TABLE_DETAIL,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case URI_DETAIL_TABLE_ITEM:
                //playing with inner join. just for fun
                selection = "SELECT * FROM " + FavoriteMoviesContract.Favorites.TABLE_MAIN + " INNER JOIN "
                        + FavoriteMoviesContract.FavoritesDetails.TABLE_DETAIL + " ON "
                        + FavoriteMoviesContract.Favorites.TABLE_MAIN + "." + DBEntries.COLUMN_ID + "="
                        + FavoriteMoviesContract.FavoritesDetails.TABLE_DETAIL + "." + DBEntries._ID
                        + " WHERE " + FavoriteMoviesContract.FavoritesDetails.TABLE_DETAIL + "." + DBEntries._ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};

                queryCursor = mHelper.getWritableDatabase().rawQuery(selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException();
        }
        return queryCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_MAIN_TABLE:
                return FavoriteMoviesContract.Favorites.CONTENT_TYPE;
            case URI_DETAIL_TABLE:
                return FavoriteMoviesContract.FavoritesDetails.CONTENT_TYPE;
            case URI_MAIN_TABLE_ITEM:
                return FavoriteMoviesContract.Favorites.CONTENT_ITEM_TYPE;
            case URI_DETAIL_TABLE_ITEM:
                return FavoriteMoviesContract.FavoritesDetails.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        long rowId;

        switch (uriMatcher.match(uri)) {
            case URI_MAIN_TABLE:
                rowId = mHelper.getWritableDatabase()
                        .insert(FavoriteMoviesContract.Favorites.TABLE_MAIN, null, values);
                break;
            case URI_DETAIL_TABLE:
                rowId = mHelper.getWritableDatabase()
                        .insert(FavoriteMoviesContract.FavoritesDetails.TABLE_DETAIL, null, values);
                break;
            default:
                throw new IllegalArgumentException();
        }


        return ContentUris.withAppendedId(uri, rowId);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        int deletedItems;

        switch (uriMatcher.match(uri)) {
            case URI_MAIN_TABLE:
                deletedItems = mHelper.getWritableDatabase()
                        .delete(FavoriteMoviesContract.Favorites.TABLE_MAIN, selection, selectionArgs);
                break;
            case URI_DETAIL_TABLE:
                deletedItems = mHelper.getWritableDatabase()
                        .delete(FavoriteMoviesContract.FavoritesDetails.TABLE_DETAIL, selection, selectionArgs);
                break;
            case URI_MAIN_TABLE_ITEM:
                selection = DBEntries.COLUMN_ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                deletedItems = mHelper.getWritableDatabase()
                        .delete(FavoriteMoviesContract.Favorites.TABLE_MAIN, selection, selectionArgs);
                break;
            case URI_DETAIL_TABLE_ITEM:
                selection = DBEntries._ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};

                deletedItems = mHelper.getWritableDatabase()
                        .delete(FavoriteMoviesContract.FavoritesDetails.TABLE_DETAIL, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException();
        }

        return deletedItems;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updatedItems;

        switch (uriMatcher.match(uri)) {
            case URI_MAIN_TABLE:
                updatedItems = mHelper.getWritableDatabase().update(FavoriteMoviesContract.Favorites.TABLE_MAIN, values,
                        selection, selectionArgs);
                break;
            case URI_DETAIL_TABLE:
                updatedItems = mHelper.getWritableDatabase().update(FavoriteMoviesContract.FavoritesDetails.TABLE_DETAIL, values,
                        selection, selectionArgs);
                break;
            case URI_MAIN_TABLE_ITEM:
                selection = DBEntries.COLUMN_ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                updatedItems = mHelper.getWritableDatabase()
                        .update(FavoriteMoviesContract.Favorites.TABLE_MAIN, values, selection, selectionArgs);
                break;
            case URI_DETAIL_TABLE_ITEM:
                selection = DBEntries._ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                updatedItems = mHelper.getWritableDatabase()
                        .update(FavoriteMoviesContract.FavoritesDetails.TABLE_DETAIL, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException();
        }

        return updatedItems;
    }
}
