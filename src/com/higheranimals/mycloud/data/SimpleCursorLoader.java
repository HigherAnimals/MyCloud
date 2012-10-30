package com.higheranimals.mycloud.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.AsyncTaskLoader;

public class SimpleCursorLoader extends AsyncTaskLoader<Cursor> {

    private static final String TAG = "SimpleCursorLoader";

    private Cursor mCursor;

    private final SQLiteDatabase db;
    private final String table;
    private final String[] columns;
    private final String selection;
    private final String[] selectionArgs;

    public SimpleCursorLoader(Context context, String table, String[] columns,
            String selection, String[] selectionArgs) {
        super(context);
        db = new DbHelper(context).getReadableDatabase();
        this.table = table;
        this.columns = columns;
        this.selection = selection;
        this.selectionArgs = selectionArgs;
    }

    @Override
    public Cursor loadInBackground() {
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        mCursor = db.query(table, columns, selection, selectionArgs, null,
                null, null);
        return mCursor;
    }

    @Override
    public void deliverResult(Cursor cursor) {
        if (isReset()) {
            if (cursor != null) {
                cursor.close();
            }
            return;
        }
        Cursor oldCursor = mCursor;
        mCursor = cursor;

        if (isStarted()) {
            super.deliverResult(cursor);
        }
        if (oldCursor != null && oldCursor != cursor && !oldCursor.isClosed()) {
            oldCursor.close();
        }
    }

    @Override
    protected void onStartLoading() {
        if (mCursor != null) {
            deliverResult(mCursor);
        }
        if (takeContentChanged() || mCursor == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }
}
