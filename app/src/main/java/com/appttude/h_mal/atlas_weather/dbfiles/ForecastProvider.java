package com.appttude.h_mal.atlas_weather.dbfiles;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.appttude.h_mal.atlas_weather.dbfiles.ForecastContract.ForecastEntry;

public class ForecastProvider extends ContentProvider {

    public static final String LOG_TAG = ForecastProvider.class.getSimpleName();

    private static final int FORECASTS = 100;
    private static final int FORECAST_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(ForecastContract.CONTENT_AUTHORITY, ForecastContract.PATH_FORECASTS, FORECASTS);

        sUriMatcher.addURI(ForecastContract.CONTENT_AUTHORITY, ForecastContract.PATH_FORECASTS + "/#", FORECAST_ID);
    }

    ForecastDBHelper mDbHelper;
    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        mDbHelper = new ForecastDBHelper(getContext());
        database = mDbHelper.getReadableDatabase();
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case FORECASTS:

                cursor = database.query(ForecastEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case FORECAST_ID:

                selection = ForecastEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(ForecastEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FORECASTS:
                return ForecastEntry.CONTENT_LIST_TYPE;
            case FORECAST_ID:
                return ForecastEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FORECASTS:
                String name = values.getAsString(ForecastEntry.COLUMN_FORECAST_NAME);
                if (name == null) {
                    throw new IllegalArgumentException("name required");
                }

                SQLiteDatabase database = mDbHelper.getWritableDatabase();

                long id = database.insert(ForecastEntry.TABLE_NAME, null, values);
                if (id == -1) {
                    Log.e(LOG_TAG, "row failed " + uri);
                    return null;
                }

                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);

            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FORECASTS:
                rowsDeleted = database.delete(ForecastEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FORECAST_ID:
                selection = ForecastEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(ForecastEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FORECASTS:
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case FORECAST_ID:

                selection = ForecastEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateProduct(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(ForecastEntry.COLUMN_FORECAST_NAME)) {
            String name = values.getAsString(ForecastEntry.COLUMN_FORECAST_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Name required");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(ForecastEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}
