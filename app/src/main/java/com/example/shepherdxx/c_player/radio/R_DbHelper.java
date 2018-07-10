package com.example.shepherdxx.c_player.radio;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.shepherdxx.c_player.radio.Contract.RDB_Entry;


/**
 * Created by Shepherdxx on 06.01.2018.
 */

public class R_DbHelper extends SQLiteOpenHelper {

    /** Name of the database file */
    private static final String DATABASE_NAME = "channels.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link R_DbHelper}.
     *
     * @param context of the app
     */
    public R_DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_RADIO_TABLE = " CREATE TABLE "
                + RDB_Entry.TABLE_NAME + " ("
                + RDB_Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RDB_Entry.COLUMN_IMAGE_URL + " TEXT, "
                + RDB_Entry.COLUMN_NAME + " TEXT NOT NULL, "
                + RDB_Entry.COLUMN_URL + " TEXT NOT NULL, "
                + RDB_Entry.COLUMN_DESCRIPTION + " TEXT, "
                + RDB_Entry.COLUMN_FAVOURITE + " INTEGER NOT NULL DEFAULT 0, "
                + RDB_Entry.COLUMN_SOURCE + " INTEGER NOT NULL DEFAULT 0 "
//                + RDB_Entry.COLUMN_TIMESTAMP    + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP "
                + "); ";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_RADIO_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
        db.execSQL("DROP TABLE IF EXISTS " +  RDB_Entry.TABLE_NAME );
        onCreate(db);
    }
}

