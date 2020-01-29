package com.dgtprm.nkt10.movieapp.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dgtprm.nkt10.movieapp.di.HasComponent;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment<T> extends Fragment implements HasComponent<T> {
    private Unbinder mUnbinder;

    private T mComponent;

    public BaseFragment() {}

    @LayoutRes
    protected abstract int contentLayout();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComponent = createComponent();
        injectDeps();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(contentLayout(), container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    abstract T createComponent();

    public T getComponent() {
        return mComponent;
    }

    abstract void injectDeps();
}
