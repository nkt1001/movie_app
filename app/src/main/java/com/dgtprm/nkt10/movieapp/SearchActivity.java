package com.dgtprm.nkt10.movieapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.dgtprm.nkt10.movieapp.fragment.MoviesFragment;
import com.dgtprm.nkt10.movieapp.fragment.OverviewFragment;
import com.dgtprm.nkt10.movieapp.fragment.SearchMoviesFragment;
import com.dgtprm.nkt10.movieapp.models.MovieData;
import com.dgtprm.nkt10.movieapp.sql.SuggestionsProvider;

import butterknife.BindView;

public class SearchActivity extends BaseActivity implements MoviesFragment.OnListFragmentInteractionListener {

    public static final String EXTRA_QUERY = "com.dgtprm.nkt10.movieapp.SearchActivity";
    @BindView(R.id.search_root_view) View mRootView;
    private SearchView mSearchView;
    private String mQuery = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleIntent(getIntent());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    SearchMoviesFragment.newInstance(mQuery)).commit();
        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_search;
    }

    @Override
    protected int menuResId() {
        return R.menu.search_menu;
    }

    @Override
    protected void onResume() {
        super.onResume();

        mRootView.requestFocus();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setIconified(false);
        mSearchView.setMaxWidth(Integer.MAX_VALUE);

        Intent intent = getIntent();
        if (intent != null && Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            mSearchView.setQuery(query, false);
            mSearchView.clearFocus();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean isDisplayHomeUpEnabled() {
        return true;
    }

    @Override
    protected boolean isDisplayShowTitleEnabled() {
        return false;
    }

    private void doSearch(String query) {
        if (mSearchView != null) {
            mSearchView.setQuery(query, false);
        }

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof SearchMoviesFragment) {
            ((SearchMoviesFragment) fragment).doSearch(query);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
        doSearch(mQuery);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            mQuery = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    SuggestionsProvider.AUTHORITY, SuggestionsProvider.MODE);
            suggestions.saveRecentQuery(mQuery, null);
        }
    }

    @Override
    public void onPosterClicked(MovieData.Result item) {
        if (MainActivity.mTwoPaneMode){
            getSupportFragmentManager().beginTransaction().replace(R.id.overview_container,
                    OverviewFragment.newInstance(item)).commit();
        } else {
            Intent intent = new Intent(this, OverviewActivity.class);
            intent.putExtra(OverviewActivity.EXTRA_MOVIE, item);
            startActivity(intent);
        }
    }

    @Override
    public void onBindTitle(String title) {}

    @Override
    public void dbItem(MovieData.Result item) {

    }

    @Override
    public View getContentLayout() {
        return null;
    }
}
