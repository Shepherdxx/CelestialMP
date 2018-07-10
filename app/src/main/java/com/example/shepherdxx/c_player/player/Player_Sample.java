package com.example.shepherdxx.c_player.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by Shepherdxx on 16.07.2017.
 */

public class Player_Sample
        extends MediaPlayer {


    public static String LOG_TAG = Player_Sample.class.getSimpleName();
    private int position;
    String DATA_SD = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
            + File.separator;

    public Player_Sample() {
    }

    public static Player_Sample newPlayer() {
        return new Player_Sample();
    }

    public Player_Sample MP_RAW_Version(Player_Sample mPlayer, Context context, String Data, OnPreparedListener listener) {
        Uri uri = Uri.parse(String.format("android.resource://%s/%s", context.getPackageName(), Data));
        Log.i(LOG_TAG, "prepare Raw " + uri);
        try {
            mPlayer.setDataSource(context, uri);
            mPlayer.setOnPreparedListener(listener);
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mPlayer;
    }

    public Player_Sample SD_Version(Player_Sample mPlayer, String Data, OnPreparedListener listener) {
        Data = Data + DATA_SD;
        return SD_U_Version(mPlayer, Data, listener);
    }

    public Player_Sample SD_U_Version(Player_Sample mPlayer, String Data, OnPreparedListener listener) {
        Log.i(LOG_TAG, "prepare " + File.pathSeparator + Data);
        try {
            mPlayer.setDataSource(Data);
            mPlayer.setOnPreparedListener(listener);
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (listener != null) {
        }

        return mPlayer;
    }

    public Player_Sample http_Version(Player_Sample mPlayer, String Data, OnPreparedListener listener) {
        Log.i(LOG_TAG, "prepare " + File.pathSeparator + Data);
        try {
            mPlayer.setDataSource(Data);
            mPlayer.setOnPreparedListener(listener);
            Log.i(LOG_TAG, "prepareAsync");
            mPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mPlayer;
    }

    public Player_Sample radio_Version(Player_Sample mPlayer, String Data, OnPreparedListener listener) {
        return http_Version(mPlayer, Data, listener);
    }


}
