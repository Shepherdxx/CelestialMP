package com.example.shepherdxx.c_player.radio;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.example.shepherdxx.c_player.radio.Contract.RDB_Entry.SERVER;

/**
 * Created by Shepherdxx on 26.01.2018.
 */

public class BaseChannels {

    public String url = "zaycevfm.cdnvideo.ru/ZaycevFM_relax_256.mp3";

    public static void insertBaseData (SQLiteDatabase database){
        if(database == null){
            return;
        }

        //create a list of base channels
        List<ContentValues> list = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, "Kiss FM");
        cv.put(Contract.RDB_Entry.COLUMN_URL, "online-kissfm.tavrmedia.ua/KissFM");
        cv.put(Contract.RDB_Entry.COLUMN_IMAGE_URL, "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1");
        cv.put(Contract.RDB_Entry.COLUMN_FAVOURITE, "0");
        cv.put(Contract.RDB_Entry.COLUMN_SOURCE, SERVER);
        list.add(cv);

        cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, "Европа плюс");
        cv.put(Contract.RDB_Entry.COLUMN_URL, "ep256.hostingradio.ru:8052/europaplus256.mp3");
        cv.put(Contract.RDB_Entry.COLUMN_IMAGE_URL, "http://online-red.com/images/radio-logo/europa-plus.png");
        cv.put(Contract.RDB_Entry.COLUMN_FAVOURITE, "0");
        cv.put(Contract.RDB_Entry.COLUMN_SOURCE, SERVER);
        list.add(cv);

        cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, "Радио Шансон");
        cv.put(Contract.RDB_Entry.COLUMN_URL, "chanson.hostingradio.ru:8041/chanson256.mp3");
        cv.put(Contract.RDB_Entry.COLUMN_IMAGE_URL, "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1");
        cv.put(Contract.RDB_Entry.COLUMN_FAVOURITE, "0");
        cv.put(Contract.RDB_Entry.COLUMN_SOURCE, SERVER);
        list.add(cv);

        cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, "Русский хит");
        cv.put(Contract.RDB_Entry.COLUMN_URL, "s9.imgradio.pro/RusHit48");
        cv.put(Contract.RDB_Entry.COLUMN_IMAGE_URL, "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1");
        cv.put(Contract.RDB_Entry.COLUMN_FAVOURITE, "0");
        cv.put(Contract.RDB_Entry.COLUMN_SOURCE, SERVER);
        list.add(cv);

        cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, "Зайцев.FM Club");
        cv.put(Contract.RDB_Entry.COLUMN_URL, "zaycevfm.cdnvideo.ru/ZaycevFM_electronic_256.mp3");
        cv.put(Contract.RDB_Entry.COLUMN_IMAGE_URL, "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1");
        cv.put(Contract.RDB_Entry.COLUMN_FAVOURITE, "0");
        cv.put(Contract.RDB_Entry.COLUMN_SOURCE, SERVER);
        list.add(cv);

        cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, "Зайцев.FM DISCO");
        cv.put(Contract.RDB_Entry.COLUMN_URL, "zaycevfm.cdnvideo.ru/ZaycevFM_disco_256.mp3");
        cv.put(Contract.RDB_Entry.COLUMN_IMAGE_URL, "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1");
        cv.put(Contract.RDB_Entry.COLUMN_FAVOURITE, "0");
        cv.put(Contract.RDB_Entry.COLUMN_SOURCE, SERVER);
        list.add(cv);

        cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, "Зайцев.FM Поп-музыка");
        cv.put(Contract.RDB_Entry.COLUMN_URL, "zaycevfm.cdnvideo.ru/ZaycevFM_pop_256.mp3");
        cv.put(Contract.RDB_Entry.COLUMN_IMAGE_URL, "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1");
        cv.put(Contract.RDB_Entry.COLUMN_FAVOURITE, "0");
        cv.put(Contract.RDB_Entry.COLUMN_SOURCE, SERVER);
        list.add(cv);

        cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, "Хит FM Dance");
        cv.put(Contract.RDB_Entry.COLUMN_URL, "icecast.radiohitfm.cdnvideo.ru/st33.mp3");
        cv.put(Contract.RDB_Entry.COLUMN_IMAGE_URL, "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1");
        cv.put(Contract.RDB_Entry.COLUMN_FAVOURITE, "0");
        cv.put(Contract.RDB_Entry.COLUMN_SOURCE, SERVER);
        list.add(cv);

        cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, "DFM Динамит");
        cv.put(Contract.RDB_Entry.COLUMN_URL, "icecast.radiodfm.cdnvideo.ru/st16.mp3");
        cv.put(Contract.RDB_Entry.COLUMN_IMAGE_URL, "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1");
        cv.put(Contract.RDB_Entry.COLUMN_FAVOURITE, "0");
        cv.put(Contract.RDB_Entry.COLUMN_SOURCE, SERVER);
        list.add(cv);

        cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, "Radio Enigma");
        cv.put(Contract.RDB_Entry.COLUMN_URL, "195.242.219.208:8200/enigma");
        cv.put(Contract.RDB_Entry.COLUMN_IMAGE_URL, "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1");
        cv.put(Contract.RDB_Entry.COLUMN_FAVOURITE, "0");
        cv.put(Contract.RDB_Entry.COLUMN_SOURCE, SERVER);
        list.add(cv);

        cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, "Comedy radio");
        cv.put(Contract.RDB_Entry.COLUMN_URL, "ic7.101.ru:8000/v11_1");
        cv.put(Contract.RDB_Entry.COLUMN_IMAGE_URL, "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1");
        cv.put(Contract.RDB_Entry.COLUMN_FAVOURITE, "0");
        cv.put(Contract.RDB_Entry.COLUMN_SOURCE, SERVER);
        list.add(cv);

        cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, "Радио Фантастики");
        cv.put(Contract.RDB_Entry.COLUMN_URL, "fantasyradioru.no-ip.biz:8002/live");
        cv.put(Contract.RDB_Entry.COLUMN_IMAGE_URL, "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1");
        cv.put(Contract.RDB_Entry.COLUMN_FAVOURITE, "0");
        cv.put(Contract.RDB_Entry.COLUMN_SOURCE, SERVER);
        list.add(cv);

        cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, "WarGaming.FM ");
        cv.put(Contract.RDB_Entry.COLUMN_URL, "sv.wargaming.fm:8051/48");
        cv.put(Contract.RDB_Entry.COLUMN_IMAGE_URL, "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1");
        cv.put(Contract.RDB_Entry.COLUMN_FAVOURITE, "0");
        cv.put(Contract.RDB_Entry.COLUMN_SOURCE, SERVER);
        list.add(cv);

        cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, "Relax FM");
        cv.put(Contract.RDB_Entry.COLUMN_URL, "ic2.101.ru:8000/v13_1?setst=-1");
        cv.put(Contract.RDB_Entry.COLUMN_IMAGE_URL, "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1");
        cv.put(Contract.RDB_Entry.COLUMN_FAVOURITE, "0");
        cv.put(Contract.RDB_Entry.COLUMN_SOURCE, SERVER);
        list.add(cv);

        cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, "104.2FM NRJ");
        cv.put(Contract.RDB_Entry.COLUMN_URL, "http://ic3.101.ru:8000/v1_1");
        //  дублируется по адресу "ic7.101.ru:8000/v1_1?"
        cv.put(Contract.RDB_Entry.COLUMN_IMAGE_URL, "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1");
        cv.put(Contract.RDB_Entry.COLUMN_FAVOURITE, "0");
        cv.put(Contract.RDB_Entry.COLUMN_SOURCE, SERVER);
        list.add(cv);

        cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, "Авторадио");
        cv.put(Contract.RDB_Entry.COLUMN_URL, "http://ic3.101.ru:8000/v3_1");
        cv.put(Contract.RDB_Entry.COLUMN_IMAGE_URL, "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1");
        cv.put(Contract.RDB_Entry.COLUMN_FAVOURITE, "0");
        cv.put(Contract.RDB_Entry.COLUMN_SOURCE, SERVER);
        list.add(cv);

        cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, "98.8FM Романтика");
        cv.put(Contract.RDB_Entry.COLUMN_URL, "http://ic3.101.ru:8000/v4_1");
        cv.put(Contract.RDB_Entry.COLUMN_IMAGE_URL, "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1");
        cv.put(Contract.RDB_Entry.COLUMN_FAVOURITE, "0");
        cv.put(Contract.RDB_Entry.COLUMN_SOURCE, SERVER);
        list.add(cv);

        cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, "Юмор FM");
        cv.put(Contract.RDB_Entry.COLUMN_URL, "http://ic3.101.ru:8000/v5_1");
        //  дублируется по адресу ic2.101.ru:8000/v5_1
        cv.put(Contract.RDB_Entry.COLUMN_IMAGE_URL, "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1");
        cv.put(Contract.RDB_Entry.COLUMN_FAVOURITE, "0");
        cv.put(Contract.RDB_Entry.COLUMN_SOURCE, SERVER);
        list.add(cv);

        cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, "Рок радио");
        cv.put(Contract.RDB_Entry.COLUMN_URL, "air2.radiorecord.ru:805/rock_320");
        cv.put(Contract.RDB_Entry.COLUMN_IMAGE_URL, "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1");
        cv.put(Contract.RDB_Entry.COLUMN_FAVOURITE, "0");
        cv.put(Contract.RDB_Entry.COLUMN_SOURCE, SERVER);
        list.add(cv);

        cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, "Супердискотека 90-х");
        cv.put(Contract.RDB_Entry.COLUMN_URL, "air2.radiorecord.ru:805/sd90_320");
        cv.put(Contract.RDB_Entry.COLUMN_IMAGE_URL, "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1");
        cv.put(Contract.RDB_Entry.COLUMN_FAVOURITE, "0");
        cv.put(Contract.RDB_Entry.COLUMN_SOURCE, SERVER);
        list.add(cv);

        cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, "Авторитетное радио (Красноярск 102.8)");
        cv.put(Contract.RDB_Entry.COLUMN_URL, "84.22.142.130:8000/arstream");
        cv.put(Contract.RDB_Entry.COLUMN_IMAGE_URL, "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1");
        cv.put(Contract.RDB_Entry.COLUMN_FAVOURITE, "0");
        cv.put(Contract.RDB_Entry.COLUMN_SOURCE, SERVER);
        list.add(cv);

        cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, "psyradio.fm");
        cv.put(Contract.RDB_Entry.COLUMN_URL, "http://81.88.36.42:8010/");
        cv.put(Contract.RDB_Entry.COLUMN_IMAGE_URL, "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1");
        cv.put(Contract.RDB_Entry.COLUMN_FAVOURITE, "0");
        cv.put(Contract.RDB_Entry.COLUMN_SOURCE, SERVER);
        list.add(cv);


        String[] adi = {"http://us3.internet-radio.com:8007/"};

        //insert all guests in one transaction
        try {
            database.beginTransaction();
            //clear the table first
            database.delete (Contract.RDB_Entry.TABLE_NAME,null,null);
            //go through the list and add one by one
            for(ContentValues c:list){
                database.insert(Contract.RDB_Entry.TABLE_NAME, null, c);
            }
            database.setTransactionSuccessful();
        } catch (SQLException e) {
            //too bad :(
        } finally {
            database.endTransaction();
        }

    }
}
