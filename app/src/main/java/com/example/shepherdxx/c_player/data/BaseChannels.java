package com.example.shepherdxx.c_player.data;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class BaseChannels {
    public static void insertBaseData (SQLiteDatabase database){
        if(database == null){
            return;
        }
        //create a list of fake guests
        List<ContentValues> list = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, "John");
        cv.put(Contract.RDB_Entry.COLUMN_URL, 12);
        list.add(cv);

        cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, "Tim");
        cv.put(Contract.RDB_Entry.COLUMN_URL, 2);
        list.add(cv);

        cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, "Jessica");
        cv.put(Contract.RDB_Entry.COLUMN_URL, 99);
        list.add(cv);

        cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, "Larry");
        cv.put(Contract.RDB_Entry.COLUMN_URL, 1);
        list.add(cv);

        cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, "Kim");
        cv.put(Contract.RDB_Entry.COLUMN_URL, 45);
        list.add(cv);

        //insert all guests in one transaction
        try
        {
            database.beginTransaction();
            //clear the table first
            database.delete (Contract.RDB_Entry.TABLE_NAME,null,null);
            //go through the list and add one by one
            for(ContentValues c:list){
                database.insert(Contract.RDB_Entry.TABLE_NAME, null, c);
            }
            database.setTransactionSuccessful();
        }
        catch (SQLException e) {
            //too bad :(
        }
        finally
        {
            database.endTransaction();
        }

    }
}
