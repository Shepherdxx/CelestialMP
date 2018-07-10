package com.example.shepherdxx.c_player.player;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.shepherdxx.c_player.data.Constants;
import com.example.shepherdxx.c_player.data.PopUpToast;

import java.io.File;
import java.util.ArrayList;

import static com.example.shepherdxx.c_player.data.Constants.URI_RADIO_AFTER_BASE;
import static com.example.shepherdxx.c_player.data.Constants.URI_RADIO_BASE;

public class PlayList {

    Context mContext;

    public PlayList(Context context) {
        mContext = context;
    }


    public PlayListInfo createPlaylist(long id) {
        return null;
    }


    private ArrayList<Long> getAudioID(int playlistId) {
        ArrayList<Long> ids = new ArrayList<>();
        try {
            QueryTask queryTask = QueryTask.queryTask();
            Cursor cursor = queryTask.playlistSummary(mContext, playlistId);
            if (cursor != null) {
                cursor.moveToFirst();
                for (int r = 0; r < cursor.getCount(); r++, cursor.moveToNext()) {
                    long id = cursor.getLong(0);
                    Log.i("getAudioID", "playlistID" + File.pathSeparator + id);
                    id = cursor.getLong(2);
                    Log.i("getAudioID", "macroPListId" + File.pathSeparator + id);
                    ids.add(id);
                    id = cursor.getLong(3);
                    Log.i("getAudioID", "macroPListId" + File.pathSeparator + id);
                }
                cursor.close();
            }
            return ids;
        } catch (Exception e) {
            e.printStackTrace();
            new PopUpToast(mContext).setMessage("Поймали ошибку");
            return ids;
        }
    }


    ArrayList<MyTrackInfo> RadioList = new ArrayList<>();

    public ArrayList<MyTrackInfo> RadioList() {
        RadioPlayListCreation();
        return RadioList;
    }

    private MyTrackInfo song(String mR, String mD, String mU, int id) {
        MyTrackInfo song = new MyTrackInfo(mR,
                mD,
                mU);
        song.setPlaylistId(id);
        return song;
    }

    int index;
    private String mUri;
    private ArrayList<MyTrackInfo> rows = new ArrayList<>();
    String[] adi = {"http://us3.internet-radio.com:8007/",
            "http://air2.radiorecord.ru:805/rock_320",
            "http://84.22.142.130:8000/arstream?4&28",
            "http://81.88.36.42:8010/"};

    //создание плейлиста
    private void RadioPlayListCreation() {
        if (rows.isEmpty()) {
            for (index = 1; index <= 5; index++) {
                mUri = URI_RADIO_BASE + String.valueOf(index) + URI_RADIO_AFTER_BASE;
                Log.i("Radio ", String.valueOf(index) + " " + String.valueOf(mUri));
                rows.add(
                        song("Radio " + String.valueOf(index),
                                mUri,
                                mUri,
                                Constants.PLAYLIST_RADIO)
                );
            }
            addMoreTo(rows);
            RadioList = rows;
            Log.i("Radio", "плейлист создан");
        } else Log.i("Radio", "плейлист уже есть");
    }

    private void addMoreTo(ArrayList<MyTrackInfo> PTI) {
        for (int i = 0; i < adi.length; i++) {
            mUri = adi[i];
            Log.i("Radio ", String.valueOf(index + i) + " " + String.valueOf(mUri));
            PTI.add(
                    song("Radio " + String.valueOf(index + i),
                            mUri,
                            mUri,
                            Constants.PLAYLIST_RADIO));
        }
    }
}
