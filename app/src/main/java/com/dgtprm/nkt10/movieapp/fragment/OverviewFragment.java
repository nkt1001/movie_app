package com.dgtprm.nkt10.movieapp.fragment;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dgtprm.nkt10.movieapp.OverviewActivity;
import com.dgtprm.nkt10.movieapp.R;
import com.dgtprm.nkt10.movieapp.di.ContextModule;
import com.dgtprm.nkt10.movieapp.di.DaggerOverviewComponent;
import com.dgtprm.nkt10.movieapp.di.OverviewComponent;
import com.dgtprm.nkt10.movieapp.di.OverviewFragmentModule;
import com.dgtprm.nkt10.movieapp.di.RetrofitModule;
import com.dgtprm.nkt10.movieapp.models.MovieData;
import com.dgtprm.nkt10.movieapp.models.Reviews;
import com.dgtprm.nkt10.movieapp.models.Trailers;
import com.dgtprm.nkt10.movieapp.network.HttpHelper;
import com.dgtprm.nkt10.movieapp.network.ImageLoader;
import com.dgtprm.nkt10.movieapp.network.ReviewListRequestService;
import com.dgtprm.nkt10.movieapp.network.TrailerListRequestService;
import com.dgtprm.nkt10.movieapp.presenter.OverviewFrPresenter;
import com.dgtprm.nkt10.movieapp.sql.AsyncDatabaseUpdateHandler;
import com.dgtprm.nkt10.movieapp.sql.FavoriteMoviesDetailLoader;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class OverviewFragment extends BaseFragment<OverviewComponent>
        implements IOverviewFragment, LoaderManager.LoaderCallbacks<MovieData.Result> {

    private final static String TAG = OverviewFragment.class.getSimpleName();
    private static final String STATE_KEY_OPEN_REVIEW = "STATE_KEY_OPEN_REVIEW";

    private MovieData.Result mItem;

    @Nullable
    private Trailers mTrailerList;
    @Nullable
    private Reviews mReviews;

    @BindView(R.id.trailer_layout) LinearLayout trailersLayout;
    @BindView(R.id.reviews_layout) LinearLayout reviewsLayout;

    @BindView(R.id.overview_mainPoster_imV) ImageView mPosterImv;

    @BindView(R.id.readReviewsBtn) Button mReadReviewsBtn;
    @BindView(R.id.addToFavoritesBtn) Button mAddToFavoritesBtn;

    @BindView(R.id.overview_progressBar) ProgressBar mPB;

    @BindView(R.id.tv_release) TextView mTvRelease;
    @BindView(R.id.tv_rating) TextView mTvRate;
    @BindView(R.id.overview_plot) TextView mTvPlot;

    @Inject
    ReviewListRequestService mReviewListService;
    @Inject
    TrailerListRequestService mTrailersService;
    @Inject
    AsyncDatabaseUpdateHandler mDbUpdateHandler;

    private OverviewFrPresenter mPresenter;
    private boolean mReviewsOpen;

    @Nullable
    private OnDbItemReady mListener;

    public interface OnDbItemReady {
        void dbItem(MovieData.Result item);
        @Nullable
        View getContentLayout();
    }

    public OverviewFragment() {}

    public static OverviewFragment newInstance(MovieData.Result movie) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle b = new Bundle();
        b.putParcelable(OverviewActivity.EXTRA_MOVIE, movie);
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mItem = savedInstanceState.getParcelable(OverviewActivity.EXTRA_MOVIE);
            mTrailerList = savedInstanceState.getParcelable(OverviewActivity.EXTRA_TRAILER);
            mReviewsOpen = savedInstanceState.getBoolean(STATE_KEY_OPEN_REVIEW);
            mReviews = savedInstanceState.getParcelable(OverviewActivity.EXTRA_REVIEWS);

        } else if (getArguments() == null) {
            throw new IllegalStateException("fragment must be initialized from static method");
        } else {
            Bundle args = getArguments();
            mItem = args.getParcelable(OverviewActivity.EXTRA_MOVIE);
        }

        super.onCreate(savedInstanceState);

        mPresenter = new OverviewFrPresenter(mTrailersService, mReviewListService, this, mDbUpdateHandler);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnDbItemReady) {
            mListener = (OnDbItemReady) context;
        }
    }

    @Override
    protected int contentLayout() {
        return R.layout.fragment_overview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mItem.isFavorite()) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            loadPosters(mItem);
            bindViews(mItem);
        }

        if (mReviewsOpen) {
            mReadReviewsBtn.setText(getString(R.string.hide_reviews));
        } else {
            mReadReviewsBtn.setText(getString(R.string.show_reviews));
        }

        if (mTrailerList == null) {
            mPresenter.getTrailers();
        } else {
            initTrailerList(mTrailerList);
        }

        adjustReviewLayout(!mReviewsOpen);
    }

    private void loadPosters(@Nullable MovieData.Result item) {
        if (item != null) {

            String posterUrl = HttpHelper
                    .getImgUrl(HttpHelper.SizeList.W154.getString(), item.getPosterPath());
            ImageLoader.getInstance().loadImageWithCrop(getActivity(), posterUrl, mPosterImv,
                    100, 100, R.mipmap.grid_item_placeholder);
        }

    }

    private void bindViews(MovieData.Result item) {
        mTvRelease.setText(item.getReleaseDate());
        mTvRate.setText(getVoteAverage(item));
        mTvPlot.setText(item.getOverview());
        mAddToFavoritesBtn.setText(item.isFavorite() ? R.string.remove_from_fav : R.string.add_to_favorites);
    }

    @Override
    OverviewComponent createComponent() {

        return DaggerOverviewComponent.builder()
                .contextModule(new ContextModule(getContext()))
                .overviewFragmentModule(new OverviewFragmentModule(mItem))
                .retrofitModule(new RetrofitModule(HttpHelper.getMoviesCacheDir(getContext())))
                .build();
    }

    @Override
    void injectDeps() {
        getComponent().inject(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(OverviewActivity.EXTRA_MOVIE, mItem);
        outState.putParcelable(OverviewActivity.EXTRA_TRAILER, mTrailerList);

        if (mReviews != null) {
            outState.putParcelable(OverviewActivity.EXTRA_REVIEWS, mReviews);
        }

        outState.putBoolean(STATE_KEY_OPEN_REVIEW, mReviewsOpen);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
        mListener = null;
    }

    private void initTrailerList(@Nullable Trailers trailers) {

        if (trailers == null) {
            return;
        }

        for (Trailers.Result res : trailers.getResults()) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.trailers_item, null, false);
            v.setTag(res);
            ((TextView) v.findViewById(R.id.overview_trailerName)).setText(res.getName());
            trailersLayout.addView(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Trailers.Result trailer = (Trailers.Result) v.getTag();
                    watchTrailer(trailer.getKey());
                }
            });
        }
    }

    private void watchTrailer(String id){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            startActivity(intent);
        }
    }

    private String getVoteAverage(MovieData.Result result){
        return result.getVoteAverage() + "/10";
    }

    @OnClick({R.id.readReviewsBtn, R.id.addToFavoritesBtn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addToFavoritesBtn:
                if (mItem.isFavorite()) {
                    mPresenter.removeMovieFromFavorites(mItem);
                } else {
                    mPresenter.addMovieToFavorites(mItem);
                }
                break;
            case R.id.readReviewsBtn:
                boolean isReviewLayoutVisible = reviewsLayout.getVisibility() == View.VISIBLE;
                mReadReviewsBtn.setText(isReviewLayoutVisible ? R.string.show_reviews : R.string.hide_reviews);

                if (!isReviewLayoutVisible) {
                    mPresenter.getReviews();
                } else {
                    adjustReviewLayout(true);
                }

                break;
        }
    }

    private void adjustReviewLayout(boolean isReviewsVisible) {
        mReviewsOpen = !isReviewsVisible;
        reviewsLayout.setVisibility(!isReviewsVisible ? View.VISIBLE : View.GONE);

        if (!mReviewsOpen) {
            return;
        }

        if (mReviews == null || mReviews.getResults().size() == 0) {
            showEmptyView();
        } else {
            inflateReviewItem(mReviews);
        }
    }

    private void inflateReviewItem(Reviews reviews) {
        if (reviewsLayout.getChildCount() > 0) {
            reviewsLayout.removeAllViews();
        }
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        for (Reviews.Result item : reviews.getResults()){
            View reviewItemLayout = inflater.inflate(R.layout.review_item_layout, reviewsLayout, false);
            TextView tvAuthor = (TextView)reviewItemLayout.findViewById(R.id.review_author);
            tvAuthor.setVisibility(View.VISIBLE);
            TextView tvContent = (TextView)reviewItemLayout.findViewById(R.id.review_content);
            tvContent.setVisibility(View.VISIBLE);
            tvAuthor.setText(item.getAuthor());
            tvContent.setText(item.getContent());

            reviewsLayout.addView(reviewItemLayout);
        }
    }

    private void showEmptyView() {
        if (reviewsLayout.getChildCount() > 0) {
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View reviewItemLayout = inflater.inflate(R.layout.review_item_layout, reviewsLayout, false);
        TextView textView = (TextView)reviewItemLayout.findViewById(R.id.review_author);
        textView.setText(R.string.no_reviews);
        textView.setVisibility(View.VISIBLE);
        reviewsLayout.addView(reviewItemLayout);
    }

    @Override
    public void showPb() {
        mPB.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePb() {
        mPB.setVisibility(View.GONE);
    }

    @Override
    public void onFailedTrailers(Throwable throwable) {
        Toast.makeText(getContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailedOverviews(Throwable throwable) {
        Toast.makeText(getContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
        adjustReviewLayout(false);
    }

    @Override
    public void trailerListFetched(Trailers trailers) {
        mTrailerList = trailers;
        initTrailerList(trailers);
    }

    @Override
    public void reviewListFetched(Reviews reviews) {
        mReviews = reviews;
        adjustReviewLayout(false);
    }

    @Override
    public void favoriteMovieInserted(Long result, MovieData.Result item) {
        bindViews(item);
    }

    @Override
    public void favoriteMovieUpdate(Long result, MovieData.Result item) {
        bindViews(item);
    }

    @Override
    public void favoriteMovieDeleted(Integer result, final MovieData.Result item) {
        bindViews(item);
        String message = getString(R.string.message_removed_item, item.getTitle());
        if (mListener != null && mListener.getContentLayout() != null) {
            Snackbar.make(mListener.getContentLayout(), message, Snackbar.LENGTH_LONG)
                    .setAction(R.string.snackbar_undo_item_deleted, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPresenter.addMovieToFavorites(item);
                        }
                    }).show();
        }
    }

    @Override
    public void disableButton() {
        mAddToFavoritesBtn.setEnabled(false);
    }

    @Override
    public void enableButton() {
        mAddToFavoritesBtn.setEnabled(true);
    }

    @Override
    public Loader<MovieData.Result> onCreateLoader(int id, Bundle args) {
        return new FavoriteMoviesDetailLoader(getContext(), mItem.getId());
    }

    @Override
    public void onLoadFinished(Loader<MovieData.Result> loader, MovieData.Result data) {
        if (mListener != null) {
            mListener.dbItem(data);
        }

        mItem = data;
        loadPosters(data);
        bindViews(data);
    }

    @Override
    public void onLoaderReset(Loader<MovieData.Result> loader) {
        mItem = null;
    }
}
