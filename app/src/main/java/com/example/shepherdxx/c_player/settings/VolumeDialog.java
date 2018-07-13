package com.example.shepherdxx.c_player.settings;//package com.example.android.mp_celestial.Settings;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

import com.example.shepherdxx.c_player.R;


public class VolumeDialog
        extends DialogFragment {

    SeekBar mSeekBar;
    int sValue;
    private boolean sValueSet;
    Bundle bundle;
    VolumeDialogListener mListener;
    Activity activity;

    public interface VolumeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog, int volumeValue);
//        void onDialogNegativeClick(DialogFragment dialog);
    }


    // Override the Fragment.onAttach() method to instantiate the VolumeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            this.activity=activity;
            mListener = (VolumeDialogListener) activity;
            bundle=getArguments();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
//
//    /**
//     * Saves the text to the {@link SharedPreferences}.
//     *
//     * @param value The text to save
//     */
//    public void setValue(int value) {
//        // Always persist/notify the first time.
//        final boolean changed = !(sValue==value);
//        if (changed || !sValueSet) {
//            sValue = value;
//            sValueSet = true;
//            persistInt(value);
//            if(changed) {
//                notifyDependencyChange(shouldDisableDependents());
//                notifyChanged();
//            }
//        }
//    }
//
//
//
    /**
     * Gets the text from the {@link SeekBar}.
     *
     * @return The current SeekBar value.
     */
    public int getsValue() {
        return sValue;
    }
//
//    @Override
//    protected void onBindDialogView(View view) {
//        super.onBindDialogView(view);
//
//        EditText editText = mEditText;
//        editText.setText(getText());
//
//        ViewParent oldParent = editText.getParent();
//        if (oldParent != view) {
//            if (oldParent != null) {
//                ((ViewGroup) oldParent).removeView(editText);
//            }
//            onAddEditTextToDialogView(view, editText);
//        }
//    }
//
//    /**
//     * Adds the EditText widget of this preference to the dialog's view.
//     *
//     * @param dialogView The dialog view.
//     */
//    protected void onAddEditTextToDialogView(View dialogView, EditText editText) {
//        ViewGroup container = (ViewGroup) dialogView
//                .findViewById(com.android.internal.R.id.edittext_container);
//        if (container != null) {
//            container.addView(editText, ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT);
//        }
//    }
//
//    @Override
//    protected void onDialogClosed(boolean positiveResult) {
//        super.onDialogClosed(positiveResult);
//
//        if (positiveResult) {
//            String value = mEditText.getText().toString();
//            if (callChangeListener(value)) {
//                setText(value);
//            }
//        }
//    }
//
//
//
//
    void setmSeekBar(View view) {
        sValue =bundle.getInt("mSoundVolume");
        mSeekBar = (SeekBar) view.findViewById(R.id.volume_seek_bar);
        mSeekBar.setMax(100);
        mSeekBar.setProgress(sValue);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sValue =progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_volume, null);
        setmSeekBar(v);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(v)
                .setTitle(R.string.apps_volume)

                .setPositiveButton(R.string.action_save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(VolumeDialog.this, getsValue());
                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        VolumeDialog.this.dismiss();
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}