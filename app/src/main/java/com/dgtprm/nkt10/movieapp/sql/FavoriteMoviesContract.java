package com.dgtprm.nkt10.movieapp.sql;

import android.net.Uri;
import android.provider.BaseColumns;

public final class FavoriteMoviesContract {

    public static final String CONTENT_AUTHORITY = "com.dgtprm.nkt10.movieapp";
    public static final Uri BASE_URI = Uri.parse("content://com.dgtprm.nkt10.movieapp");

    private FavoriteMoviesContract() {}

    public interface DBEntries extends BaseColumns {
        String COLUMN_ID = "movie_id";
        String COLUMN_OVERVIEW = "overview";
        String COLUMN_TITLE = "title";
        String COLUMN_RELEASE_DATE = "release";
        String COLUMN_VOTE_AVERAGE = "vote";
        String COLUMN_POSTER_PATH = "poster";
        String COLUMN_BACKDROP_POSTER_PATH = "backdrop";
    }

    public static class Favorites implements DBEntries {
        public static final String TABLE_MAIN = "favorites";
        static final String PATH = "items";
        static final String PATH_ITEM = "items/#";


        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + PATH;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + PATH;

        public static Uri buildDirUri() {
            return BASE_URI.buildUpon().appendPath(PATH).build();
        }

        public static Uri buildItemUri(long _id) {
            return BASE_URI.buildUpon().appendPath(PATH).appendPath(Long.toString(_id)).build();
        }
    }

    public static class FavoritesDetails implements DBEntries {
        public static final String TABLE_DETAIL = "details";
        static final String PATH = "details";
        static final String PATH_ITEM = "details/#";

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + PATH;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + PATH;

        public static Uri buildDirUri() {
            return BASE_URI.buildUpon().appendPath(PATH).build();
        }

        public static Uri buildItemUri(long _id) {
            return BASE_URI.buildUpon().appendPath(PATH).appendPath(Long.toString(_id)).build();
        }
    }
}
