package com.example.shepherdxx.c_player.player;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.shepherdxx.c_player.data.Constants;
import com.example.shepherdxx.c_player.data.PopUpToast;
import com.example.shepherdxx.c_player.radio.Contract.RDB_Entry;
import com.example.shepherdxx.c_player.radio.R_DbHelper;

import java.io.File;
import java.util.ArrayList;


public class PlayList {

    private Context mContext;
    private String logTag=PlayList.class.getSimpleName();

    public PlayList(Context context) {
        mContext = context;
    }

    public PlayListInfo createPlaylist(int id) {
        PlayListInfo current= null;
        switch (id){
            case Constants.PLAYLIST_RADIO:
                current=PlayListInfo.Radio();
                current.audioTracks=plCreate(id);
                Log.i(logTag, "create " + current.audioTracks.toString());
                break;
//            case Constants.PLAYLIST_All_Audio:
//                current=PlayListInfo.All();
//                current.plType = Constants.MP_SD_U;
//                current.audioTracks=plCreate(id);
//                break;
//            case Constants.PLAYLIST_Cache:
//                current=PlayListInfo.Cache();
//                current.plType = Constants.MP_SD_U;
//                current.audioTracks=plCreate(id);
//                Log.i(logTag, "create " + current.audioTracks.size());
//                break;
//            default:
//                ArrayList<Long> audioIds= getAudioID(id);
//                current=getPlayListInfo(id);
//                current.plType = Constants.MP_SD_U;
//                current.audioIds = audioIds;
//                current.audioTracks=plCreate(id,audioIds);
//                break;
        }
        return current;
    }

    private ArrayList<MyTrackInfo> plCreate(int id) {
        return plCreate(id, null);
    }

    //Создаем плейлист
    private ArrayList<MyTrackInfo> plCreate(int id,ArrayList<Long> audioIds) {
        ArrayList<MyTrackInfo> cur=null;
        Log.i("plCreate", "плейлист подготовка");
//        sharedPreferences=getDefaultSharedPreferences(mContext);
//        b= sharedPreferences.getBoolean("req_perm", false);
//        if (b){
            switch (id) {
                case Constants.PLAYLIST_RADIO:
                    cur = RadioList();
                    break;
//                case Constants.PLAYLIST_Cache:
//                    cur = loadTracks(id, MyCachePath.getAbsolutePath(),audioIds);
//                    break;
//                default:
//                    cur = loadTracks(id,null,audioIds);
//                    break;
            }
            if (cur!=null)Log.i("PlayListTrue create", " " + cur.size());
            return cur;
//        Log.i("PlayListTrue create", "need to get Permission");
//        return null;
    }
//
//    private MyTrackInfo cursorTrack(Cursor cursor,int playlistId){
//        MyTrackInfo songSD=null;
//        String artist, title, name, album, url;
//        long duration,audioId;
//        url = cursor.getString(0);
//        artist = cursor.getString(1);
//        album = cursor.getString(2);
//        name = cursor.getString(3);
//        title = cursor.getString(4);
//        duration = cursor.getLong(5);
//        audioId = cursor.getLong(6);
//        String[] proj = {url, artist, album, name, title};
//        if (duration!=0){
//            songSD = new MyTrackInfo(proj,duration,audioId);
//            songSD.setPlaylistId(playlistId);}
//        return songSD;
//    }
//
//    private ArrayList<MyTrackInfo> loadTracks(int playlistId, String cUri, ArrayList<Long> audioIds) {
//        ArrayList<MyTrackInfo> rows=new ArrayList<>();
//        String table = "TRACK";
//        final String[] projection = MyTrackInfo.FILLED_PROJECTION;
//        String selection= MediaStore.Audio.Media.IS_MUSIC + "!=0";
//        if (audioIds != null) {
//            int i;
//            for (i = 0; i < audioIds.size(); i++)
//                selection = new StringBuilder()
//                        .append(selection)
//                        .append(" AND ")
//                        .append(MediaStore.Audio.Media._ID)
//                        .append("= ")
//                        .append(audioIds.get(i))
//                        .toString();
//        }
//        String sortBy = MediaStore.Audio.Media.ARTIST;
//
//        QueryTask queryTask=new QueryTask(table, projection, selection, null, sortBy);
//        Cursor cursor=queryTask.runMyQuery(mContext);
//        if (cursor != null) {
//            MyTrackInfo songSD;
//            while (cursor.moveToNext()) {
//                songSD = cursorTrack(cursor,playlistId);
//                if (songSD != null) {File f = new File(songSD.getFileName());
//                    if (cUri == null) rows.add(songSD);
//                    else
//                    if (f.exists() && f.getAbsolutePath().contains(cUri)) {
//                        rows.add(songSD);
////                    Log.i(logTag, "MusicScroll " + url + File.pathSeparator + name + File.pathSeparator + album);
//                    }
//                }
//            }cursor.close();
//        }
//        return rows;
//    }

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




    private MyTrackInfo song(String mR, String mD, String mU, int id) {
        MyTrackInfo song = new MyTrackInfo(mR,
                mD,
                mU);
        song.setPlaylistId(id);
        return song;
    }

    //создание плейлиста радио
    private ArrayList<MyTrackInfo> RadioList() {
        ArrayList<MyTrackInfo> rows = new ArrayList<>();
            R_DbHelper dbHelper = new R_DbHelper(mContext);
            SQLiteDatabase mDb = dbHelper.getReadableDatabase();
            String[] projection = {
                    RDB_Entry.COLUMN_NAME,
                    RDB_Entry.COLUMN_URL,
                    RDB_Entry.COLUMN_DESCRIPTION
            };
            Cursor cursor = mDb.query(
                    RDB_Entry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null
                );
            if (cursor == null || cursor.getCount() < 1) {
                return null;
            }
            if (cursor.moveToFirst()){
                while (!cursor.isAfterLast()) {
                    int addressColumnIndex = cursor.getColumnIndex(RDB_Entry.COLUMN_URL);
                    int nameColumnIndex    = cursor.getColumnIndex(RDB_Entry.COLUMN_NAME);
                    int descColumnIndex    = cursor.getColumnIndex(RDB_Entry.COLUMN_DESCRIPTION);
                    String radioAddress = "http://" + cursor.getString(addressColumnIndex);
                    String radioTitle   = cursor.getString(nameColumnIndex);
                    String description  = cursor.getString(descColumnIndex);
                    rows.add(
                            song(   radioTitle,
                                    description,
                                    radioAddress,
                                    Constants.PLAYLIST_RADIO));
                    try {
                        cursor.moveToNext();
                    }
                    catch(Exception e){
                        e.printStackTrace();
                        Log.e("PlayList","Cursor is out of range");
                    }
                }
                Log.i("Radio", "плейлист создан");
                cursor.close();
            } else Log.i("Radio", "плейлист уже есть");
        return rows;
    }
}
