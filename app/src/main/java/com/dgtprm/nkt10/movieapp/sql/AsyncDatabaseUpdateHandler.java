package com.dgtprm.nkt10.movieapp.sql;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.dgtprm.nkt10.movieapp.models.MovieData;

public class AsyncDatabaseUpdateHandler {
    private final Context mAppContext;
    private final DBManager mTableManager;

    public AsyncDatabaseUpdateHandler(Context mAppContext, DBManager mTableManager) {
        this.mAppContext = mAppContext;
        this.mTableManager = mTableManager;
    }

    public final void asyncInsert(final MovieData.Result item, @Nullable Callback callback) {
        new InsertAsyncTask(item, callback).execute();
    }

    public final void asyncUpdate(final long id, final MovieData.Result newItem, @Nullable Callback callback) {
        new UpdateAsyncTask(id, newItem, callback).execute();
    }

    public final void asyncDelete(final MovieData.Result item, @Nullable final Callback callback) {
        // TODO: If we want to scroll somewhere after we delete this item, we can
        // create a DeleteAsyncTask subclass of the BaseAsyncTask. This involves
        // using a Long result, however.
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                return mTableManager.deleteItem(item);
            }

            @Override
            protected void onPostExecute(Integer integer) {
                if (callback != null) {
                    callback.onPostAsyncDelete(integer, item);
                }
            }
        }.execute();
    }

    public final void asyncClear() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                mTableManager.clear();
                return null;
            }
        }.execute();
    }

    public final DBManager getTableManager() {
        return mTableManager;
    }

    protected final Context getContext() {
        return mAppContext;
    }

    public interface Callback {
        void onPostAsyncDelete(Integer result, MovieData.Result item);
        void onPostAsyncInsert(Long result, MovieData.Result item);
        void onPostAsyncUpdate(Long result, MovieData.Result item);
    }

    ////////////////////////////////////////////////////////////
    // Insert and update AsyncTasks
    ////////////////////////////////////////////////////////////

    /**
     * Created because the code in insert and update AsyncTasks are exactly the same.
     */
    private abstract class BaseAsyncTask extends AsyncTask<Void, Void, Long> {
        final MovieData.Result mItem;

        BaseAsyncTask(MovieData.Result item) {
            mItem = item;
        }
    }

    private class InsertAsyncTask extends BaseAsyncTask {
        @Nullable
        private final Callback mmCallback;

        InsertAsyncTask(MovieData.Result item, @Nullable Callback callback) {
            super(item);
            mmCallback = callback;
        }

        @Override
        protected Long doInBackground(Void... params) {
            return mTableManager.insertItem(mItem);
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            if (mmCallback != null) {
                mmCallback.onPostAsyncInsert(result, mItem);
            }
        }
    }

    private class UpdateAsyncTask extends BaseAsyncTask {
        private final long mId;
        @Nullable
        private final Callback mmCallback;

        UpdateAsyncTask(long id, MovieData.Result item, @Nullable Callback callback) {
            super(item);
            mId = id;
            mmCallback = callback;
        }

        @Override
        protected Long doInBackground(Void... params) {
            mTableManager.updateItem(mId, mItem);
            return mId;
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            if (mmCallback != null) {
                mmCallback.onPostAsyncUpdate(result, mItem);
            }
        }
    }
}
