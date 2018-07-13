package com.example.shepherdxx.c_player.radio;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.shepherdxx.c_player.R;
import com.example.shepherdxx.c_player.data.PopUpToast;
import com.example.shepherdxx.c_player.player.PreService;
import com.example.shepherdxx.c_player.radio.Contract.RDB_Entry;

import static com.example.shepherdxx.c_player.data.Constants.PLAYLIST_RADIO;


public class Fragment_Radio extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    //ToDO add DataBaseListener to update playlist on change

    public Fragment_Radio() {
    }

    private SQLiteDatabase mDb;
    R_DbHelper mRDbHelper;
    PopUpToast toast;
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_ID = "playlist_id";

    /**
     * Identifier for the channel data loader
     */
    private static final int DATA_LOADER = 0;
    RadioChannelsCursorAdapter mCursorAdapter;

    public static Fragment_Radio newInstance(int columnCount, int playlistId) {
        Fragment_Radio fragment = new Fragment_Radio();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putInt(ARG_ID, playlistId);
        fragment.setArguments(args);
        Log.i("Fragment_Playlist", "newInstance " + String.valueOf(playlistId));
        return fragment;
    }

    public static Fragment_Radio newInstance() {
        return newInstance(1, PLAYLIST_RADIO);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        toast = new PopUpToast(getContext());
        mRDbHelper = new R_DbHelper(getContext());
        mDb = mRDbHelper.getWritableDatabase();
        if (getAll().getCount() == 0) {
            RadioChannel.insertBaseData(mDb);
            getAll().close();
        }

        View view = inflater.inflate(R.layout.fragment_radio, container, false);
        // Find the ListView which will be populated with the pet data
        final ListView radioListView = view.findViewById(R.id.radio_list);
        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = view.findViewById(R.id.empty_view);
        radioListView.setEmptyView(emptyView);
        // Setup an Adapter to create a list item for each row of pet data in the Cursor.
        // There is no data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new RadioChannelsCursorAdapter(getContext(), null);
        radioListView.setAdapter(mCursorAdapter);

        // Setup the item click listener
        radioListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                try{
                    getContext().startService(
                        PreService.startBGService(getContext(),PLAYLIST_RADIO,position));
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }

        });

        radioListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                                     @Override
                                                     public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long id) {
                                                         // Create new intent to go to {@link EditorActivity}
                                                         Intent intent = new Intent(getContext(), RadioDB_Edit.class);
                                                         Uri currentDataUri = ContentUris.withAppendedId(RDB_Entry.CONTENT_URI, id);
                                                         // Set the URI on the data field of the intent
                                                         intent.setData(currentDataUri);
                                                         // Launch the {@link EditorActivity} to display the data for the current pet.
                                                         startActivity(intent);
                                                         return false;
                                                     }
                                                 }
        );

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = view.findViewById(R.id.fab_radio);
        fab.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       Intent intent = new Intent(getContext(), RadioDB_Edit.class);
                                       startActivity(intent);
                                   }
                               }
        );

        // Kick off the loader
        try {
            getActivity().getLoaderManager().initLoader(DATA_LOADER, null, this);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e("Fragment_Radio", "Loader error");
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Cursor getAll() {
        return mDb.query(
                RDB_Entry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
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
                getActivity(),   // Parent activity context
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