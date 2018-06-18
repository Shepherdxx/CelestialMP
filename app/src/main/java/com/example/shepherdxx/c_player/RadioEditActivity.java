package com.example.shepherdxx.c_player;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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

import com.example.shepherdxx.c_player.data.Contract.RDB_Entry;

public class RadioEditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    PopUpToast toast;
    Uri currentDataUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_edit);
        //Создаю контейнер для всплывающих сообщений
        toast = new PopUpToast(getBaseContext());
        findView();

        Intent intent=getIntent();
        Uri dataUri=intent.getData();
        currentDataUri=dataUri;
        if (dataUri==null){
            setTitle(getString(R.string.editor_activity_title_add_channel));
        }else{
            setTitle(getString(R.string.editor_activity_title_edit_file));
        }

    }
    EditText mAddressEditText;
    EditText mNameEditText;
    EditText mDescriptionEditText;
    Spinner spinner;
    ImageButton play;
    private int mValue;

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
//                    startService(
//                            PreService.startSampleBGService(getBaseContext(),Data));
                }
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
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String description      = mDescriptionEditText.getText().toString().trim();
        String addressString    = mAddressEditText.getText().toString().trim();
        String nameString       = mNameEditText.getText().toString().trim();

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(RDB_Entry.COLUMN_URL, addressString);
        values.put(RDB_Entry.COLUMN_NAME, nameString);
        values.put(RDB_Entry.COLUMN_FAVOURITE, mValue);
        values.put(RDB_Entry.COLUMN_DESCRIPTION, description);

        // Show a toast message depending on whether or not the insertion was successful
        if (currentDataUri == null) {
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
            int rowsAffected = getContentResolver().update(currentDataUri, values, null, null);
            if (rowsAffected == 0) {
                toast.setMessage(getString(R.string.editor_update_data_failed));
            } else {
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

    Uri mCurrentDataUri=null;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        if (mCurrentDataUri == null) {
            // This is a new pet, so change the app bar to say "Add a Pet"
            setTitle(getString(R.string.editor_activity_title_add_channel));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a pet that hasn't been created yet.)
            invalidateOptionsMenu();
        } else { }
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
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
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the data hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mDataHasChanged) {
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(RadioEditActivity.this);
                            }
                        };
                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to database
                saveRadioChanel();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
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
                // and continue editing the pet.
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
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deletePet();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
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
     * Perform the deletion of the pet in the database.
     */
    private void deletePet() {
        // TODO: Implement this method
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                RDB_Entry._ID,
                RDB_Entry.COLUMN_NAME,
                RDB_Entry.COLUMN_DESCRIPTION,
                RDB_Entry.COLUMN_FAVOURITE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(
                this,   // Parent activity context
                currentDataUri,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int addressColumnIndex = cursor.getColumnIndex(RDB_Entry.COLUMN_URL);
            int nameColumnIndex    = cursor.getColumnIndex(RDB_Entry.COLUMN_NAME);
            int descripColumnIndex = cursor.getColumnIndex(RDB_Entry.COLUMN_DESCRIPTION);
            int mValueColumnIndex = cursor.getColumnIndex(RDB_Entry.COLUMN_FAVOURITE);

            // Extract out the value from the Cursor for the given column index
            String addressString = cursor.getString(addressColumnIndex);
            String nameString    = cursor.getString(nameColumnIndex);
            String description   = cursor.getString(descripColumnIndex);
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
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
