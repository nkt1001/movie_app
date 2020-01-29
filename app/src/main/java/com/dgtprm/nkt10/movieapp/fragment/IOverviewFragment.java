package com.dgtprm.nkt10.movieapp.fragment;

import com.dgtprm.nkt10.movieapp.models.MovieData;
import com.dgtprm.nkt10.movieapp.models.Reviews;
import com.dgtprm.nkt10.movieapp.models.Trailers;

public interface IOverviewFragment {
    void showPb();
    void hidePb();
    void onFailedTrailers(Throwable throwable);
    void onFailedOverviews(Throwable throwable);
    void trailerListFetched(Trailers trailers);
    void reviewListFetched(Reviews reviews);
    void favoriteMovieInserted(Long result, MovieData.Result item);
    void favoriteMovieUpdate(Long result, MovieData.Result item);
    void favoriteMovieDeleted(Integer result, MovieData.Result item);
    void disableButton();
    void enableButton();
}
