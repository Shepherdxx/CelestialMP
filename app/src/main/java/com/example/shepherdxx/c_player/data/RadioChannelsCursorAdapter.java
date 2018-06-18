package com.example.shepherdxx.c_player.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.shepherdxx.c_player.R;
import com.example.shepherdxx.c_player.data.Contract.RDB_Entry;

public class RadioChannelsCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link RadioChannelsCursorAdapter}.
     *
     * @param context The context
     * @param cursor  The cursor from which to get the data.
     */
    public RadioChannelsCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.fragment_radio_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if(cursor.getPosition()%2==1) {
            view.setBackgroundColor(context.getResources().getColor(R.color.background_odd));
        }
        else {
            view.setBackgroundColor(context.getResources().getColor(R.color.background_even));
        }

        // Find individual views that we want to modify in the list item layout
        TextView idTextView     = view.findViewById(R.id.id_radio);
        TextView nameTextView   = view.findViewById(R.id.tv_radio_name);
        TextView descTextView   = view.findViewById(R.id.content_radio);

        // Find the columns of pet attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(RDB_Entry.COLUMN_NAME);
        int idColumnIndex   = cursor.getColumnIndex(RDB_Entry._ID);
        int descColumnIndex = cursor.getColumnIndex(RDB_Entry.COLUMN_DESCRIPTION);

        // Read the attributes from the Cursor for the current Radio Channel
        int channelId   = cursor.getInt(idColumnIndex);
        String petName  = cursor.getString(nameColumnIndex);
        String petBreed = cursor.getString(descColumnIndex);

//        // If the favorite is empty string or null, then use some default text,
//        // so the TextView isn't blank.
//        if (TextUtils.isEmpty(favorite)) {
//            petBreed = context.getString(R.string.favorite_show);
//        }

        // Update the TextViews with the attributes for the current Radio Channel
        idTextView.setText(String.valueOf(channelId));
        nameTextView.setText(petName);
        descTextView.setText(petBreed);
    }
}


