package com.example.shepherdxx.c_player;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shepherdxx.c_player.data.BaseChannels;
import com.example.shepherdxx.c_player.data.Contract;
import com.example.shepherdxx.c_player.data.Contract.RDB_Entry;
import com.example.shepherdxx.c_player.data.DbHelper;
import com.example.shepherdxx.c_player.data.RadioChannelsCursorAdapter;

public class RadioListActivity extends AppCompatActivity
        implements
        LoaderManager.LoaderCallbacks<Cursor>
{


    RadioChannelsCursorAdapter mCursorAdapter;
    private SQLiteDatabase mDb;
    DbHelper mDbhelper;
    FloatingActionButton fab;

    //найти и связать объекты
    private void findView(){
        //Todo кнопка
            fab=findViewById(R.id.fab_radio);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Todo
                    //если базы данных нет - создать
                    //иначе открыть редактор
                    startActivity(new Intent(RadioListActivity.this,RadioEditActivity.class));
                }
            });
        //Todo список
            // Find the ListView which will be populated with the pet data
            ListView radioListView = findViewById(R.id.radio_list);
            // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
            View emptyView = findViewById(R.id.empty_view);
            radioListView.setEmptyView(emptyView);
            radioListView.setAdapter(mCursorAdapter);

            radioListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(getBaseContext(),"fuck" + i + l,Toast.LENGTH_SHORT).show();
                }
            });
    };

    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_list);

        mDbhelper=new DbHelper(this);
        mDb = mDbhelper.getWritableDatabase();

        mCursorAdapter = new RadioChannelsCursorAdapter(this, null);
        findView();
    }

    private Cursor getAll(){
        return
                mDb.query(
                RDB_Entry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                RDB_Entry.COLUMN_TIMESTAMP
        );
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                RDB_Entry._ID,
                RDB_Entry.COLUMN_NAME,
                RDB_Entry.COLUMN_DESCRIPTION,
                RDB_Entry.COLUMN_FAVOURITE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(
                this,   // Parent activity context
                RDB_Entry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);        // Sort order by the adding time

    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link PetCursorAdapter} with this new cursor containing updated data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }

}
