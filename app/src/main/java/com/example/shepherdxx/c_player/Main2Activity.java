package com.example.shepherdxx.c_player;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.shepherdxx.c_player.data.PopUpToast;
import com.example.shepherdxx.c_player.radio.Fragment_Radio;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class Main2Activity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    PopUpToast toast;
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        toast = new PopUpToast(this);
        sharedPreferences = getDefaultSharedPreferences(this);
        boolean b = sharedPreferences.getBoolean("req_perm", false);
        if (!b) CheckPermission();


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
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
            } else {
                PERMISSION_GRANTED = false;
                toast.setMessage("В доступе отказано прям ппц");
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
                    toast.setMessage("В доступе отказано");
                    CheckPermission();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }


}
