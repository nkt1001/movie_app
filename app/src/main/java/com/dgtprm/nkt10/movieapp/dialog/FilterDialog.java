package com.dgtprm.nkt10.movieapp.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.dgtprm.nkt10.movieapp.MainActivity;
import com.dgtprm.nkt10.movieapp.R;
import com.dgtprm.nkt10.movieapp.di.ContextModule;
import com.dgtprm.nkt10.movieapp.di.DaggerFilterComponent;
import com.dgtprm.nkt10.movieapp.di.FilterComponent;
import com.dgtprm.nkt10.movieapp.di.FilterModule;
import com.dgtprm.nkt10.movieapp.di.RetrofitModule;
import com.dgtprm.nkt10.movieapp.fragment.MoviesFragment;
import com.dgtprm.nkt10.movieapp.models.Genres;
import com.dgtprm.nkt10.movieapp.network.GetGenresRequestService;
import com.dgtprm.nkt10.movieapp.network.RequestService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class FilterDialog extends DialogFragment implements RequestService.BaseCallback<Genres> {

    private static final String TAG = "FilterDialog";
    private static final String EXTRA_PICKED_GENRES = "com.dgtprm.nkt10.movieapp.dialog.FilterDialog.EXTRA_PICKED_GENRES";
    private static final String EXTRA_GENRES = "com.dgtprm.nkt10.movieapp.dialog.extra.FilterDialog.EXTRA_GENRES";

    private CompositeSubscription mSubscriptions;

    @Inject
    GetGenresRequestService mService;

    private ListView mSortByList;
    private ListView mGenresList;
    @Nullable
    private ProgressBar mProgressBar;

    private Genres mGenres;

    private OnFilterSetListener mListener;
    private List<String> mGenresArrayList;
    @Nullable
    private Genres mPickedGenres;


    public static FilterDialog newInstance(@Nullable Genres pickedGenres, OnFilterSetListener listener) {

        FilterDialog fragment = new FilterDialog();
        fragment.mListener = listener;

        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PICKED_GENRES, pickedGenres);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onError(Throwable throwable) {
        hidePb();
        showEmptyView();
        initSortByListView(mSortByList);
    }

    private void hidePb() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void showEmptyView() {
        if (mGenresList == null) {
            return;
        }

        ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(), R.array.no_genres, android.R.layout.simple_list_item_1);
        mGenresList.setAdapter(adapter);
    }

    @Override
    public void onSuccess(Genres data) {

        hidePb();
        mGenres = data;

        if (mGenresArrayList == null || mGenresArrayList.size() == 0) {
            mGenresArrayList = new ArrayList<>();
            for (Genres.Genre genre : mGenres.getGenres()) {
                mGenresArrayList.add(genre.getName());
            }
        }

        initSortByListView(mSortByList);
    }

    public interface OnFilterSetListener {
        void onFilterSet(int position, Genres genres);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mPickedGenres = savedInstanceState.getParcelable(EXTRA_PICKED_GENRES);
            mGenres = savedInstanceState.getParcelable(EXTRA_GENRES);
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                mPickedGenres = bundle.getParcelable(EXTRA_PICKED_GENRES);
            }
        }

        FilterComponent mFilterComponent = DaggerFilterComponent.builder()
                .retrofitModule(new RetrofitModule(getContext().getCacheDir()))
                .contextModule(new ContextModule(getContext()))
                .filterModule(new FilterModule())
                .build();

        mService = mFilterComponent.getGenresService();

        mSubscriptions = new CompositeSubscription();
    }

    private void getGenres() {
        showPb();
        Subscription subscription = mService.execute(this);
        mSubscriptions.add(subscription);
    }

    private void showPb() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.filter_dialog_layout, null, false);

        mSortByList = (ListView) rootView.findViewById(R.id.sort_by_list);
        mGenresList = (ListView) rootView.findViewById(R.id.genres_list);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.filter)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mListener != null) {
                            countPickedItems(mPickedGenres);
                            int position = mSortByList.getCheckedItemPosition();
                            mListener.onFilterSet(position, mPickedGenres);
                        }
                    }
                }).setView(rootView);

        getGenres();

        return builder.create();
    }

    private void initGenresListView(final ListView genresListView) {
        if (genresListView == null || mGenres == null || genresListView.getCount() > 1) {
            return;
        }

        genresListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_multiple_choice, mGenresArrayList);
        genresListView.setAdapter(adapter);

        genresListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (genresListView.isItemChecked(position)) {
                    genresListView.setItemChecked(position, true);
                } else {
                    genresListView.setItemChecked(position, false);
                }
            }
        });

        if (mPickedGenres != null) {
            for (Genres.Genre genre : mPickedGenres.getGenres()) {
                int position = mGenresArrayList.indexOf(genre.getName());
                genresListView.setItemChecked(position, true);
            }
        } else {
            mPickedGenres = new Genres();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(EXTRA_GENRES, mGenres);
        countPickedItems(mPickedGenres);
        outState.putParcelable(EXTRA_PICKED_GENRES, mPickedGenres);
        super.onSaveInstanceState(outState);
    }

    public void setListener(OnFilterSetListener listener) {
        this.mListener = listener;
    }

    private void countPickedItems(Genres holder) {
        SparseBooleanArray sbArray = mGenresList.getCheckedItemPositions();
        List<Genres.Genre> pickedGenres = new ArrayList<>();

        if (sbArray == null || sbArray.size() == 0) {
            return;
        }

        for (int i = 0; i < sbArray.size(); i++) {
            int key = sbArray.keyAt(i);

            if (!sbArray.get(key)) {
                continue;
            }

            pickedGenres.add(mGenres.getGenres().get(key));
        }

        holder.setGenres(pickedGenres);
    }

    private void initSortByListView(ListView sortByList) {
        if (sortByList == null) {
            return;
        }

        int checkedItem = PreferenceManager.getDefaultSharedPreferences(getContext()).getInt(MainActivity.SORT_ORDER_PREF, 0);

        toggleGenresListView(checkedItem);

        sortByList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.sort_by,
                android.R.layout.simple_list_item_single_choice);
        sortByList.setAdapter(adapter);
        sortByList.setItemChecked(checkedItem, true);

        sortByList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toggleGenresListView(position);
            }
        });
    }

    private void toggleGenresListView(int position) {
        if (mGenresList == null) {
            return;
        }

        if (position == MoviesFragment.POSITION_FAVORITES) {
            showEmptyView();
        } else {
            initGenresListView(mGenresList);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }
}
