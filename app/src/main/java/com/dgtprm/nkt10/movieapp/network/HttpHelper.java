package com.dgtprm.nkt10.movieapp.network;

import android.content.Context;
import android.preference.PreferenceManager;

import com.dgtprm.nkt10.movieapp.MainActivity;

import java.io.File;

public class HttpHelper {
    private static final String API_KEY = "fbe4e6280f6a460beaad8ebe2bc130ac";
    private static final String BASE_URL = "http://api.themoviedb.org/3/";
    private static final String BASE_IMG_URL = "http://image.tmdb.org/t/p/%s%s";

    private static final String CACHE_DIR = "tmdb_movies_cache";

    public enum SizeList{
        W154("w154", 154), W185("w185", 185), W342("w342", 342), W500("w500", 500);

        private String stringWidth;
        private int numWidth;

        SizeList(String string, int num){
            stringWidth = string;
            numWidth = num;
        }

        public String getString(){
            return stringWidth;
        }

        public int getInteger(){
            return numWidth;
        }
    }

    public static String getSortOrderString(Context context){

        int lastPosition = PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(MainActivity.SORT_ORDER_PREF, 0);

        switch (lastPosition){
            case 0:
                return "popularity.desc";

            default:
                return  "vote_average.desc";

        }
    }

    public static File getMoviesCacheDir(Context context) {
        return new File(context.getCacheDir(), CACHE_DIR);
    }

    public static String getApiKey() {
        return API_KEY;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getImgUrl(String size, String path) {

        return String.format(BASE_IMG_URL, size, path);
    }

}
