package com.dgtprm.nkt10.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.dgtprm.nkt10.movieapp.di.DaggerFilterDialogControllerComponent;
import com.dgtprm.nkt10.movieapp.di.FilterDialogControllerComponent;
import com.dgtprm.nkt10.movieapp.di.FilterDialogControllerModule;
import com.dgtprm.nkt10.movieapp.dialog.FilterDialog;
import com.dgtprm.nkt10.movieapp.dialog.FilterDialogController;
import com.dgtprm.nkt10.movieapp.fragment.MovieListFragment;
import com.dgtprm.nkt10.movieapp.fragment.MoviesFragment;
import com.dgtprm.nkt10.movieapp.fragment.OverviewFragment;
import com.dgtprm.nkt10.movieapp.models.Genres;
import com.dgtprm.nkt10.movieapp.models.MovieData;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements MoviesFragment.OnListFragmentInteractionListener {

    private static final String TAG = "MainActivity";

    public static final String SORT_ORDER_PREF = "sort_by";
    public static final String EXTRA_RESTORED_DATA = "EXTRA_RESTORED_DATA";
    public static final String EXTRA_DATA = "extra data";

    public static boolean mTwoPaneMode;

    private FragmentManager mFragmentManager;
//    @Inject
//    FilterDialogController mFilterDialogController;

    @Override
    protected int layoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected int menuResId() {
        return R.menu.main_menu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTwoPaneMode = findViewById(R.id.overview_container) != null;
        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new MovieListFragment())
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_search:
                onSearchRequested();
                return true;
            case R.id.menu_filter:
                onFilterRequested();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onFilterRequested() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment != null && fragment instanceof MovieListFragment) {
            ((MovieListFragment)fragment).onFilterRequested();
        }
    }

    @Override
    protected boolean isDisplayHomeUpEnabled() {
        return false;
    }

    @Override
    protected boolean isDisplayShowTitleEnabled() {
        return false;
    }

    @Override
    public void onPosterClicked(final MovieData.Result item) {

        if (mTwoPaneMode){
            mFragmentManager.beginTransaction().replace(R.id.overview_container,
                    OverviewFragment.newInstance(item)).commit();
        } else {
            Intent intent = new Intent(this, OverviewActivity.class);
            intent.putExtra(OverviewActivity.EXTRA_MOVIE, item);
            startActivity(intent);
        }

    }

    @Override
    public void onBindTitle(String title) {
        Toolbar toolbar = getToolbar();

        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    @Override
    public void dbItem(MovieData.Result item) {

    }

    @Override
    public View getContentLayout() {
        return null;
    }
}
