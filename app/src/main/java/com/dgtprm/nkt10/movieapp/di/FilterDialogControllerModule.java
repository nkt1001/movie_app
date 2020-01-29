package com.dgtprm.nkt10.movieapp.di;

import android.support.v4.app.FragmentManager;

import com.dgtprm.nkt10.movieapp.dialog.FilterDialog;
import com.dgtprm.nkt10.movieapp.dialog.FilterDialogController;

import dagger.Module;
import dagger.Provides;

@Module
public class FilterDialogControllerModule {
    private FilterDialog.OnFilterSetListener mListener;
    private FragmentManager mFragmentManager;

    public FilterDialogControllerModule(FragmentManager fragmentManager, FilterDialog.OnFilterSetListener listener) {
        mListener = listener;
        mFragmentManager = fragmentManager;
    }

    @Provides
    FilterDialog.OnFilterSetListener provideListener() {
        return mListener;
    }

    @Provides
    FragmentManager provideFragmentManager() {
        return mFragmentManager;
    }

    @Provides
    FilterDialogController provideController(FragmentManager fragmentManager, FilterDialog.OnFilterSetListener listener) {
        return new FilterDialogController(fragmentManager, listener);
    }
}
