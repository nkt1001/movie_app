package com.dgtprm.nkt10.movieapp.fragment;

import com.dgtprm.nkt10.movieapp.models.MovieData;

public interface IMovesFragment {
    void showPb();
    void hidePb();
    void onFailed(Throwable throwable);
    void movieListFetched(MovieData movieData);
}
