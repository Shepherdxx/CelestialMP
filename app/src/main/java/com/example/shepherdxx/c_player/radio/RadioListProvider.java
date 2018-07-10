package com.example.shepherdxx.c_player.radio;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.shepherdxx.c_player.radio.Contract.RDB_Entry;

/**
 * Created by Shepherdxx on 06.01.2018.
 */

public class RadioListProvider extends ContentProvider {


    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    String LOG_TAG = RadioListProvider.class.getSimpleName();
    static final int RADIO_LIST     = 1001;
    static final int RADIO_CHANEL   = 1000;
    /** Database helper object */
    private R_DbHelper mRDbHelper;

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.example.android.pets/pets" will map to the
        // integer code {@link #RADIO_LIST}. This URI is used to provide access to MULTIPLE rows
        // of the pets table.
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_RADIO_CHANNELS, RADIO_LIST);

        // The content URI of the form "content://com.example.android.pets/pets/#" will map to the
        // integer code {@link #RADIO_CHANEL}. This URI is used to provide access to ONE single row
        // of the pets table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.example.android.pets/pets/3" matches, but
        // "content://com.example.android.pets/pets" (without a number at the end) doesn't match.
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_RADIO_CHANNELS + "/#", RADIO_CHANEL);
    }

    ContentResolver contentResolver;
    @Override
    public boolean onCreate() {
        mRDbHelper = new R_DbHelper(getContext());
        try {
            contentResolver = getContext().getContentResolver();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mRDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case RADIO_LIST:
//                sortOrder = RDB_Entry.SORT_ORDER_DEFAULT;
                cursor = database.query(RDB_Entry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case RADIO_CHANEL:
                selection = RDB_Entry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                // Cursor containing that row of the table.
                cursor = database.query(RDB_Entry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(contentResolver, RDB_Entry.CONTENT_URI);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RADIO_LIST:
                return RDB_Entry.CONTENT_LIST_TYPE;
            case RADIO_CHANEL:
                return RDB_Entry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RADIO_LIST:
                return insertRadioChanel(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mRDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case RADIO_LIST:
                // Delete all rows that match the selection and selection args
                rowsDeleted=database.delete(RDB_Entry.TABLE_NAME, selection, selectionArgs);
                break;
            case RADIO_CHANEL:
                // Delete a single row given by the ID in the URI
                selection = RDB_Entry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted=database.delete(RDB_Entry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            contentResolver.notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues values,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);

        int rowsUpdated;
        switch (match) {
            case RADIO_LIST:
                rowsUpdated=updateRadioChanel(uri, values, selection, selectionArgs);
                break;
            case RADIO_CHANEL:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = RDB_Entry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsUpdated=updateRadioChanel(uri, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
        if (rowsUpdated != 0) {
            contentResolver.notifyChange(uri, null);
        }
        return rowsUpdated;
    }


    private Uri insertRadioChanel(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(RDB_Entry.COLUMN_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Radio Chanel requires a name");
        }
        // Check that the name is not null
        String address = values.getAsString(RDB_Entry.COLUMN_URL);
        if (address == null) {
            throw new IllegalArgumentException("Radio Chanel requires an address");
        }
        Integer favour = values.getAsInteger(RDB_Entry.COLUMN_FAVOURITE);
        if (favour == null || !RDB_Entry.isValidValue(favour)) {
            throw new IllegalArgumentException("Radio Chanel requires valid value");
        }

        // Get writeable database
        SQLiteDatabase database = mRDbHelper.getWritableDatabase();
        // Insert the new pet with the given values
        long id = database.insert(RDB_Entry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        contentResolver.notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private int updateRadioChanel(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link RDB_Entry#COLUMN_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(RDB_Entry.COLUMN_NAME)) {
            String name = values.getAsString(RDB_Entry.COLUMN_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Radio Chanel requires a name");
            }
        }

        // If the {@link RDB_Entry#COLUMN_FAVOURITE} key is present,
        // check that the gender value is valid.
        if (values.containsKey(RDB_Entry.COLUMN_FAVOURITE)) {
            Integer favour = values.getAsInteger(RDB_Entry.COLUMN_FAVOURITE);
            if (favour == null || !RDB_Entry.isValidValue(favour)) {
                throw new IllegalArgumentException("Radio Chanel requires valid value");
            }
        }

        // If the {@link RDB_Entry#COLUMN_URL} key is present,
        // check that the value is not null.
        if (values.containsKey(RDB_Entry.COLUMN_URL)) {
            String address = values.getAsString(RDB_Entry.COLUMN_URL);
            if (address == null) {
                throw new IllegalArgumentException("Radio Chanel requires an address");
            }
        }

        // No need to check the description or image URL, any value is valid (including null).

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mRDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        return database.update(RDB_Entry.TABLE_NAME, values, selection, selectionArgs);
    }

}
