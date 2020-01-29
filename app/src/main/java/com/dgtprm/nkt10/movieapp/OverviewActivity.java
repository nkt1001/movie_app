package com.dgtprm.nkt10.movieapp;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.dgtprm.nkt10.movieapp.fragment.OverviewFragment;
import com.dgtprm.nkt10.movieapp.models.MovieData;
import com.dgtprm.nkt10.movieapp.network.HttpHelper;
import com.dgtprm.nkt10.movieapp.network.ImageLoader;

import butterknife.BindView;

public class OverviewActivity extends BaseActivity {

    public static final String EXTRA_MOVIE = "EXTRA_MOVIE";
    public static final String EXTRA_TRAILER = "EXTRA_TRAILER";
    public static final String EXTRA_REVIEWS = "EXTRA_REVIEWS";

    @BindView(R.id.content_layout) View mContentView;
    @BindView(R.id.overview_ctbar) CollapsingToolbarLayout mColToolbar;
    @BindView(R.id.overview_tbar_imV) ImageView mColImv;
    private MovieData.Result item;

    @Override
    protected int layoutResId() {
        return R.layout.activity_overview;
    }

    @Override
    protected int menuResId() {
        return R.menu.main_menu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            FragmentManager mFragmentManager = getSupportFragmentManager();
            item = getIntent().getParcelableExtra(EXTRA_MOVIE);
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, OverviewFragment.newInstance(item)).commit();
        } else {
            item = savedInstanceState.getParcelable(EXTRA_MOVIE);
        }

        if (item != null) {
            mColToolbar.setTitle(item.getTitle());
            if (!item.isFavorite()) {
                loadImage(item);
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menu_filter);
        item.setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (R.id.menu_search == item.getItemId()) {
            onSearchRequested();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadImage(MovieData.Result item) {
        String collapsingImageUrl = HttpHelper.getImgUrl(HttpHelper.SizeList.W500.getString(), item.getBackdropPath());
        ImageLoader.getInstance().loadImageWithPlaceholder(this, collapsingImageUrl, mColImv, android.R.color.transparent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(EXTRA_MOVIE, item);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected boolean isDisplayHomeUpEnabled() {
        return true;
    }

    @Override
    public void dbItem(MovieData.Result item) {
        this.item = item;
        loadImage(item);
    }

    @Override
    public View getContentLayout() {
        return mContentView;
    }
}
