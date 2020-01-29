package com.dgtprm.nkt10.movieapp.network;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ImageLoader {
    private static final String TAG = ImageLoader.class.getSimpleName();

    private static ImageLoader loader=null;

    private ImageLoader(){
    }

    public static ImageLoader getInstance(){
        if (null != loader)
            return loader;

        loader = new ImageLoader();
        return loader;
    }


    public void loadImageWithPlaceholder(final Context context, String path, final ImageView where, final int placeholderResId){

        final String finalImgPosterUrl = path;
        Picasso.with(context).load(path).networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(placeholderResId)
                .error(placeholderResId).fit().into(where, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(context).load(finalImgPosterUrl)
                        .placeholder(placeholderResId)
                        .error(placeholderResId).fit().into(where, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                    }
                });
            }
        });
    }

    public void loadImageWithCrop(final Context context, final String path, final ImageView where,
                                  final int resizeWidth, final int resizeHeight, final int placeholderResId){

        Picasso.with(context).load(path).resize(resizeWidth, resizeHeight).centerCrop()
                .networkPolicy(NetworkPolicy.OFFLINE).into(where, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(context).load(path)
                        .placeholder(placeholderResId)
                        .error(placeholderResId).resize(resizeWidth, resizeHeight)
                        .centerCrop().into(where, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                    }
                });
            }
        });
    }

    public void loadImage(final Context context, String path, final ImageView where){
        final String finalImgPosterUrl = path;
        Picasso.with(context).load(path).networkPolicy(NetworkPolicy.OFFLINE).into(where, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(context).load(finalImgPosterUrl).fit().into(where, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                    }
                });
            }
        });
    }

}
