package com.dgtprm.nkt10.movieapp;

import android.app.Activity;
import android.app.Application;
import android.graphics.Point;
import android.support.annotation.IdRes;
import android.view.Display;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import retrofit2.Retrofit;

public class App extends Application {

    public static final int ORIENTATION_VERTICAL = 0;
    public static final int ORIENTATION_HORIZONTAL = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        Picasso.Builder builder = new Picasso.Builder(getApplicationContext());
        builder.downloader(new OkHttpDownloader(getApplicationContext(), Integer.MAX_VALUE));
        Picasso built = builder.build();
        Picasso.setSingletonInstance(built);
    }

    public Point screenMetrics(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size;
    }

    public int defineOrientation(int screenWidth, int screenHeight){
        return screenWidth > screenHeight ? ORIENTATION_HORIZONTAL : ORIENTATION_VERTICAL;
    }

    public int calcColumnNum(int screenWidth, int screenHeight){

        int currentOrientation = defineOrientation(screenWidth, screenHeight);

        if(currentOrientation == ORIENTATION_HORIZONTAL){
            return 3;
        } else {
            return 2;
        }
    }

    public static String makeTag(Class<?> cls, @IdRes int viewId) {
        return cls.getName() + ":" + viewId;
    }
}
