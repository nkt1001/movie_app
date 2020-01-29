package com.dgtprm.nkt10.movieapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dgtprm.nkt10.movieapp.App;
import com.dgtprm.nkt10.movieapp.MainActivity;
import com.dgtprm.nkt10.movieapp.PosterRecyclerListAdapter;
import com.dgtprm.nkt10.movieapp.R;
import com.dgtprm.nkt10.movieapp.dialog.FilterDialog;
import com.dgtprm.nkt10.movieapp.models.Genres;
import com.dgtprm.nkt10.movieapp.models.MovieData;

import java.util.List;

import butterknife.BindView;

public abstract class MoviesFragment<C> extends BaseFragment<C> implements IMovesFragment, FilterDialog.OnFilterSetListener {

    public static final int POSITION_FAVORITES = 2;

    private App app;

    private OnListFragmentInteractionListener mListener;

    @BindView(R.id.recycler_posterList) RecyclerView recyclerView;
//    @BindView(R.id.spinner_sort_order) Spinner spinner;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.empty_view) TextView emptyView;

    private GridLayoutManager layoutManager;
    PosterRecyclerListAdapter adapter;

    private MovieData movieData;
    private Genres mPickedGenres;

    private SharedPreferences mPref;

//    private ArrayAdapter<CharSequence> spinnerAdapter;

    public MoviesFragment() {
    }

    @Override
    protected int contentLayout() {
        return R.layout.fragment_item_list;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (App) getActivity().getApplication();
        mPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if(savedInstanceState != null){
            movieData = savedInstanceState.getParcelable(MainActivity.EXTRA_RESTORED_DATA);
        } else {
            movieData = getActivity().getIntent().getParcelableExtra(MainActivity.EXTRA_DATA);
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutManager = layoutManager();

        if (movieData == null) {
            int position = mPref.getInt(MainActivity.SORT_ORDER_PREF, 0);

            if (position == POSITION_FAVORITES) {
                onLoadFavorites();
            } else {
                onRequestItems();
            }
        } else {
            adapter = new PosterRecyclerListAdapter(getContext(), movieData.getResults(), mListener);
            adjustRecyclerView(recyclerView, layoutManager, adapter);
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                boolean isLastPage = movieData.getTotalPages().equals(movieData.getPage());
                boolean isLoading = swipeRefreshLayout.isRefreshing();
                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        onLoadPage(movieData.getPage() + 1);
                    }
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                int current = mPref.getInt(MainActivity.SORT_ORDER_PREF, 0);
                if (current == POSITION_FAVORITES) {
                    swipeRefreshLayout.setRefreshing(false);
                    return;
                }

                if (mPickedGenres != null) {
                    mPickedGenres.clearSelection();
                }
                onRefreshItems();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        adjustTitle();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(MainActivity.EXTRA_RESTORED_DATA, movieData);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener = null;
    }

    private void adjustTitle(){
        if (mListener == null) {
            return;
        }

        int position = mPref.getInt(MainActivity.SORT_ORDER_PREF, 0);
        mListener.onBindTitle(getResources().getStringArray(R.array.sort_by)[position]);
    }

    protected boolean isFavoritesData() {
        return POSITION_FAVORITES == mPref.getInt(MainActivity.SORT_ORDER_PREF, 0);
    }

    protected MovieData getMovieData() {
        return movieData;
    }

    protected abstract void onLoadFavorites();
    protected abstract void onRequestItems();
    protected abstract void onRefreshItems();
    protected abstract void onLoadPage(int page);

    private void setMovieData(MovieData data, boolean nextPage) {
        movieData = data;

        if (adapter != null && nextPage) {
            adapter.updateDataList(data.getResults());
        } else {
            adapter = new PosterRecyclerListAdapter(getContext(), data.getResults(), mListener);
            adjustRecyclerView(recyclerView, layoutManager, adapter);
        }

        if (movieData == null || movieData.getResults() == null || movieData.getResults().size() == 0) {
            showEmptyView();
        } else {
            hideEmptyView();
        }
    }

    private GridLayoutManager layoutManager() {
        Point size = app.screenMetrics(getActivity());

        return new GridLayoutManager(getActivity(),
                app.calcColumnNum(size.x, size.y));
    }

    private void adjustRecyclerView(RecyclerView recycler, RecyclerView.LayoutManager layoutManager, RecyclerView.Adapter adapter){
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);
    }

    protected Genres getGenres() {
        return mPickedGenres;
    }

    @Override
    public void showPb() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hidePb() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailed(Throwable throwable) {
        Toast.makeText(getContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void movieListFetched(MovieData movieData) {
        if (movieData.getPage() > 1 && this.movieData != null) {
            List<MovieData.Result> mResultArray = this.movieData.getResults();
            mResultArray.addAll(movieData.getResults());
            movieData.setResults(mResultArray);
            setMovieData(movieData, true);
        } else {
            setMovieData(movieData, false);
        }
    }

    @Override
    public void onFilterSet(int position, Genres genres) {
        mPref.edit().putInt(MainActivity.SORT_ORDER_PREF, position).commit();
        adjustTitle();
        mPickedGenres = genres;
        if (isFavoritesData()) {
            onLoadFavorites();
        } else {
            onFilterSet(genres);
        }
    }

    protected abstract void onFilterSet(Genres genres);

    private void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView() {
        emptyView.setVisibility(View.GONE);
    }

    public interface OnListFragmentInteractionListener {
        void onPosterClicked(MovieData.Result item);
        void onBindTitle(String title);
    }
}
