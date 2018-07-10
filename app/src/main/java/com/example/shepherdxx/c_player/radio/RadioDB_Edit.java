package com.example.shepherdxx.c_player.radio;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.shepherdxx.c_player.R;
import com.example.shepherdxx.c_player.data.PopUpToast;
import com.example.shepherdxx.c_player.player.PreService;
import com.example.shepherdxx.c_player.radio.Contract.RDB_Entry;


/**
 * Created by Shepherdxx on 05.01.2018.
 */

public class RadioDB_Edit
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    PopUpToast toast;
    Uri mCurrentDataUri = null;
    EditText mAddressEditText;
    EditText mNameEditText;
    EditText mDescriptionEditText;
    Spinner spinner;
    ImageButton play;
    private int mValue;
    /**
     * Identifier for the data loader
     */
    private static final int EXISTING_DATA_LOADER = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_edit);
        //Создаю контейнер для всплывающих сообщений
        toast = new PopUpToast(getBaseContext());

        Intent intent=getIntent();
        mCurrentDataUri = intent.getData();

        if (mCurrentDataUri == null) {
            setTitle(getString(R.string.editor_activity_title_add_channel));
            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            invalidateOptionsMenu();

        } else {
            setTitle(getString(R.string.editor_activity_title_edit_file));
            // Initialize a loader to read the data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_DATA_LOADER, null, this);
        }

        findView();
    }


    private void findView(){
        mDescriptionEditText = findViewById(R.id.et_radio_desc);
        mAddressEditText     = findViewById(R.id.et_radio_address);
        mNameEditText        = findViewById(R.id.et_radio_name);
        spinner              = findViewById(R.id.et_radio_spinner);
        play                 = findViewById(R.id.ib_radio_preload);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addressString = mAddressEditText.getText().toString().trim();
                if (addressString.isEmpty()) toast.setMessage("Введите адресс");
                else {String Data = "http://"+addressString;
                    startService(
                            PreService.startSampleBGService(getBaseContext(),Data));}
            }
        });
        setupSpinner();
        mDescriptionEditText.setOnTouchListener(mTouchListener);
        mAddressEditText.setOnTouchListener(mTouchListener);
        mNameEditText.setOnTouchListener(mTouchListener);
        spinner.setOnTouchListener(mTouchListener);

    }

    /**
     * Get user input from editor and save new RadioChanel into database.
     */
    private void saveRadioChanel() {
        // Чтение данных, удаение ненужных пробелов
        String description      = mDescriptionEditText.getText().toString().trim();
        String addressString    = mAddressEditText.getText().toString().trim();
        String nameString       = mNameEditText.getText().toString().trim();


        if (mCurrentDataUri == null &&
                TextUtils.isEmpty(nameString) &&
                TextUtils.isEmpty(description) &&
                TextUtils.isEmpty(addressString)) {

            confirmation();

        } else {
            if (TextUtils.isEmpty(addressString)) {
                toast.setMessage("Заполните адресс");
            }
        }
        //Todo Добавить проверку на наличие в БД такого пути
        // Создаем объект ContentValues и  вкладываем наши значения.
        ContentValues values = new ContentValues();
        values.put(RDB_Entry.COLUMN_URL, addressString);
        if (TextUtils.isEmpty(nameString)) nameString = addressString;
        values.put(RDB_Entry.COLUMN_NAME, nameString);
        values.put(RDB_Entry.COLUMN_FAVOURITE, mValue);
        values.put(RDB_Entry.COLUMN_DESCRIPTION, description);

        // Show a toast message depending on whether or not the insertion was successful

        if (mCurrentDataUri==null){
            Uri newUri = getContentResolver().insert(RDB_Entry.CONTENT_URI, values);
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Log.e("saveRadioChanel", getString(R.string.editor_insert_radio_failed) + values.toString());
                toast.setMessage(getString(R.string.editor_insert_radio_failed));
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Log.e("saveRadioChanel", values.toString());
                toast.setMessage(getString(R.string.editor_insert_radio_successful));
            }
        } else {
            //Данные существуют, их надо менять
            int rowsAffected = getContentResolver().update(mCurrentDataUri, values, null, null);
            if (rowsAffected == 0) {
                //данные не поменялись
                toast.setMessage(getString(R.string.editor_update_data_failed));
            } else {
                //изменения произошли
                toast.setMessage(getString(R.string.editor_update_data_successful));
            }
        }
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter favSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_spinner_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        favSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        spinner.setAdapter(favSpinnerAdapter);

        // Set the integer mSelected to the constant values
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.favorite_hide))) {
                        mValue = RDB_Entry.NOPE;
                    } else if (selection.equals(getString(R.string.favorite_star))) {
                        mValue = RDB_Entry.YEP;
                    } else {
                        mValue = RDB_Entry.DEFAULT;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mValue = RDB_Entry.DEFAULT;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new channel, hide the "Delete" menu item.
        if (mCurrentDataUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save channel to database
                saveRadioChanel();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the data hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mDataHasChanged) {
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                confirmation();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    // OnTouchListener that listens for any user touches on a View, implying that they are modifying
    // the view, and we change the mPetHasChanged boolean to true.

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mDataHasChanged = true;
            return false;
        }
    };

    private void confirmation() {
        // Create a click listener to handle the user confirming that
        // changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, navigate to parent activity.
                        NavUtils.navigateUpFromSameTask(RadioDB_Edit.this);
                    }
                };
        // Show a dialog that notifies the user they have unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the channel.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    boolean mDataHasChanged=false;
    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mDataHasChanged) {
            super.onBackPressed();
            return;
        }
        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        confirmation();
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the channel.
                deleteChannel();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the channel.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the channel in the database.
     */
    private void deleteChannel() {
        // Only perform the delete if this is an existing channel.
        if (mCurrentDataUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentDataUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                toast.setMessage(getString(R.string.editor_delete_radio_failed));
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                toast.setMessage(getString(R.string.editor_delete_radio_successful));
            }
        }
        // Close the activity
        finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                RDB_Entry._ID,
                RDB_Entry.COLUMN_URL,
                RDB_Entry.COLUMN_NAME,
                RDB_Entry.COLUMN_DESCRIPTION,
                RDB_Entry.COLUMN_FAVOURITE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(
                this,   // Parent activity context
                mCurrentDataUri,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order

    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int addressColumnIndex = cursor.getColumnIndex(RDB_Entry.COLUMN_URL);
            int nameColumnIndex    = cursor.getColumnIndex(RDB_Entry.COLUMN_NAME);
            int descColumnIndex = cursor.getColumnIndex(RDB_Entry.COLUMN_DESCRIPTION);
            int mValueColumnIndex = cursor.getColumnIndex(RDB_Entry.COLUMN_FAVOURITE);
            // Extract out the value from the Cursor for the given column index
            String addressString = cursor.getString(addressColumnIndex);
            String nameString    = cursor.getString(nameColumnIndex);
            String description = cursor.getString(descColumnIndex);
            int mValue = cursor.getInt(mValueColumnIndex);

            // Update the views on the screen with the values from the database
            mDescriptionEditText .setText (description);
            mAddressEditText     .setText (addressString);
            mNameEditText        .setText (nameString);

            // Gender is a dropdown spinner, so map the constant value from the database
            // into one of the dropdown options (0 , 1, 2 ).
            // Then call setSelection() so that option is displayed on screen as the current selection.
            switch (mValue) {
                case RDB_Entry.YEP:
                    spinner.setSelection(RDB_Entry.YEP);
                    break;
                case RDB_Entry.NOPE:
                    spinner.setSelection(RDB_Entry.NOPE);
                    break;
                default:
                    spinner.setSelection(RDB_Entry.DEFAULT);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mNameEditText.setText("");
        mAddressEditText.setText("");
        mDescriptionEditText.setText("");
        spinner.setSelection(RDB_Entry.DEFAULT); // Select "DEFAULT"

    }

    //Todo Добавить будильник,изменение позициий
    }



