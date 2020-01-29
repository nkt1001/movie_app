package com.dgtprm.nkt10.movieapp.dialog;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.dgtprm.nkt10.movieapp.models.Genres;

public class FilterDialogController {

    private final FragmentManager mFragmentManager;

    private FilterDialog.OnFilterSetListener mListener;

    public FilterDialogController(FragmentManager fragmentManager, FilterDialog.OnFilterSetListener listener) {
        mFragmentManager = fragmentManager;
        mListener = listener;
    }

    public final void show(Genres initialGenres, String tag) {
        FilterDialog filterDialog = FilterDialog.newInstance(initialGenres, mListener);
        filterDialog.show(mFragmentManager, tag);
    }

    public void tryRestoreCallback(String tag) {
        FilterDialog dialog = findDialog(tag);
        if (dialog != null) {
            dialog.setListener(mListener);
        }
    }

    @Nullable
    private FilterDialog findDialog(String tag) {
        return (FilterDialog) mFragmentManager.findFragmentByTag(tag);
    }
}
