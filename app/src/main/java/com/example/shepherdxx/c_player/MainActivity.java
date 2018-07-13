package com.example.shepherdxx.c_player;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.shepherdxx.c_player.data.PopUpToast;
import com.example.shepherdxx.c_player.player.Player_Background;
import com.example.shepherdxx.c_player.radio.Fragment_Radio;
import com.example.shepherdxx.c_player.settings.MySettingsActivity;
import com.example.shepherdxx.c_player.settings.VolumeDialog;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.example.shepherdxx.c_player.player.Player_Background.soundVolume;

public class MainActivity
        extends AppCompatActivity
        implements VolumeDialog.VolumeDialogListener
{

    SharedPreferences sharedPreferences;
    PopUpToast toast;
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toast = new PopUpToast(this);
        sharedPreferences = getDefaultSharedPreferences(this);
        boolean b = sharedPreferences.getBoolean("req_perm", false);
        if (!b) {
            cardStateCheck();
            CheckPermission();
        }

        Fragment exchangeFragment = Fragment_Radio.newInstance();
        replacer(exchangeFragment);
    }

    //Смена фрагментов
    void replacer(Fragment exchangeFragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, exchangeFragment);
//        //Добавление в стак отмены
//        if (fragmentId.size() != 0)
//            fragmentTransaction.addToBackStack(null);
//        fragmentId.add(checkedFragmentId);
//        Log.i(Log_tag, fragmentId.toString());
        // Commit the transaction
        fragmentTransaction.commit();

    }

    boolean PERMISSION_GRANTED;

    private void CheckPermission() {
        if (Build.VERSION.SDK_INT >= 23  ) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
            } else {
                PERMISSION_GRANTED = false;
                Log.i("CheckPermission","В доступе отказано прям ппц");
                sharedPreferences.edit()
                        .putBoolean("req_perm", PERMISSION_GRANTED)
                        .apply();
            }
        } else {
            PERMISSION_GRANTED = true;
            sharedPreferences.edit()
                    .putBoolean("req_perm", PERMISSION_GRANTED)
                    .apply();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PERMISSION_GRANTED = true;
                    sharedPreferences.edit()
                            .putBoolean("req_perm", PERMISSION_GRANTED)
                            .apply();
                } else {
                    Log.i("onRequestPermissions","В доступе отказано");
                    CheckPermission();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private boolean cardStateCheck(){
        Log.i("Memory Card ", "cardStateCheck");
        boolean cardState = false;
        String state = Environment.getExternalStorageState();
        switch (state){
        case (Environment.MEDIA_BAD_REMOVAL):
            cardState = false;
            Log.i("Memory Card " , " was removed before it was unmounted");
        break;
            case (Environment.MEDIA_CHECKING):
                cardState = true;
                Log.i("Memory Card " , " is present and being disk-checked");
                break;
            case (Environment.MEDIA_MOUNTED):
                cardState = true;
                Log.i("Memory Card " , " is present and mounted with read/write access");
                break;
            case (Environment.MEDIA_MOUNTED_READ_ONLY):
                cardState = true;
                Log.i("Memory Card " , " is present and mounted with readonly access");
                break;
            case (Environment.MEDIA_NOFS):
                cardState = false;
                Log.i("Memory Card " , " is present but is blank or using unsupported file system");
                break;
            case (Environment.MEDIA_REMOVED):
                cardState = false;
                Log.i("Memory Card " , " is not present");
                break;
            case (Environment.MEDIA_SHARED):
                cardState = false;
                Log.i("Memory Card " , " is present but shared via USB mass storage");
                break;
            case (Environment.MEDIA_UNMOUNTABLE):
                cardState = false;
                Log.i("Memory Card " , " is present but cannot be mounted");
                break;
            case (Environment.MEDIA_UNMOUNTED):
                cardState = false;
                Log.i("Memory Card " , " is present but not mounted");
                break;
        }
        return cardState;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        Log.i("MainActivity", "onCreateOptionsMenu");
        return true;
    }

    String key_volume;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // When the home button is pressed, take the user back to the VisualizerActivity
        switch (id){
            case (R.id.pref):
                Intent startSettingsActivity = new Intent(MainActivity.this, MySettingsActivity.class);
                startActivity(startSettingsActivity);
                break;
            case R.id.volume_menu:
                key_volume=getResources().getString(R.string.key_apps_volume);
                VolumeDialog vD= new VolumeDialog();
                Bundle vBundle=new Bundle();
                int mSoundVolume =sharedPreferences.getInt(key_volume, 60);
                vBundle.putInt("mSoundVolume",mSoundVolume);
                vD.setArguments(vBundle);
                vD.show(getFragmentManager(),"VolumeDialog");
                Toast.makeText(this,"Volume",Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, int volumeValue) {
        sharedPreferences.edit().putInt(key_volume, volumeValue).apply();
        soundVolume=volumeValue;
        Player_Background.sInstance.setVolume();
    }
}
