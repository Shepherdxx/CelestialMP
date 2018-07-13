package com.example.shepherdxx.c_player.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.support.v7.preference.Preference.OnPreferenceClickListener;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import com.example.shepherdxx.c_player.R;

import java.io.File;
import java.util.Set;


/**
 * Created by Shepherdxx on 25.06.2017.
 */

public class SettingsFragment
        extends PreferenceFragmentCompat
        implements
        OnPreferenceClickListener
{

    String key_volume;
//    SeekBarPreference dialog;
    SharedPreferences sharedPreferences;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            sharedPreferences=context.getSharedPreferences(context.getPackageName() + "_preferences",
                    Context.MODE_PRIVATE);
            Log.i("setup_sPref",sharedPreferences.getAll().toString());
        }catch (Exception e){e.printStackTrace();}

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.some_settings);
        key_volume=getResources().getString(R.string.key_apps_volume);
        setPr();


    MultiSelectListPreference wifiNetwork;
//    wifiNetwork = (MultiSelectListPreference) findPreference(SettingsFragment.repeat_option);
//    wifiNetwork.setPersistent(false);
//    String[] ssids = getWifiNetworks();
//    wifiNetwork.setEntries(ssids);
//    wifiNetwork.setEntryValues(ssids);
//    wifiNetwork.setValues(mSettings.allowedWifiNetworks());
//    wifiNetwork.setOnPreferenceChangeListener(this);
    }


//    @Override
//    public void onDialogPositiveClick(DialogFragment dialog, int volumeValue) {
//        sharedPreferences.edit().putInt(key_volume, volumeValue).apply();
//    }
//
//    @Override
//    public void onDialogNegativeClick(DialogFragment dialog) {
//
//    }


    private void setPr(){
        Set<String> preferenceNames = getPreferenceManager().getSharedPreferences().getAll().keySet();
        for (String prefName : preferenceNames) {
            Log.i("prefName",prefName);
            android.support.v7.preference.Preference preference = findPreference(prefName);
            if (preference!=null)
                preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        }
    }

    @Override
    public boolean onPreferenceClick(android.support.v7.preference.Preference preference) {
        Log.i("onPreferenceClick",preference.getKey() + " was clicked");
        //            dialog.show(getActivity().getFragmentManager(), "volume_dialog");
        return true;
    }

    private static android.support.v7.preference.Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new android.support.v7.preference.Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(android.support.v7.preference.Preference preference, Object value) {
            String stringValue = value.toString();
            Log.i(
                    "OnPreferenceChange",
                    preference.getKey()
                            + " was changed to"
                            + File.pathSeparator
                            + stringValue);
            //ToDo Do i need this?
            return true;
        }
    };
}
