package com.higheranimals.mycloud;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;

import com.actionbarsherlock.app.SherlockListFragment;
import com.higheranimals.mycloud.data.DbHelper;
import com.higheranimals.mycloud.data.SimpleCursorLoader;

public class ServiceListFragment extends SherlockListFragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "ServiceListFragment";

    SimpleCursorAdapter adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "onActivityCreated");

        setEmptyText(getString(R.string.no_services));

        adapter = new SimpleCursorAdapter(getSherlockActivity(),
                android.R.layout.simple_list_item_1, null,
                new String[] { DbHelper.SERVICE_NAME },
                new int[] { android.R.id.text1 }, 0);
        setListAdapter(adapter);

        setListShown(false);

        this.getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new SimpleCursorLoader(getSherlockActivity(),
                DbHelper.SERVICE_TABLE, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);

        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        // TODO Auto-generated method stub
        adapter.swapCursor(null);
    }

}
