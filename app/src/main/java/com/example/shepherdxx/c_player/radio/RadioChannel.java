package com.example.shepherdxx.c_player.radio;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.example.shepherdxx.c_player.radio.Contract.RDB_Entry.DEFAULT;
import static com.example.shepherdxx.c_player.radio.Contract.RDB_Entry.SERVER;

/**
 * Created by Shepherdxx on 26.01.2018.
 */

public class RadioChannel {

    private static ContentValues valuesRadioChannel(String name,String url, String image, int fav, int source){
        ContentValues cv = new ContentValues();
        cv.put(Contract.RDB_Entry.COLUMN_NAME, name);
        cv.put(Contract.RDB_Entry.COLUMN_URL, url);
        cv.put(Contract.RDB_Entry.COLUMN_IMAGE_URL, image);
        cv.put(Contract.RDB_Entry.COLUMN_FAVOURITE, fav);
        cv.put(Contract.RDB_Entry.COLUMN_SOURCE, source);
        return cv;
    }

    private static ContentValues valuesDefaultRadioChannel(String name,String url, String image){
        return valuesRadioChannel(name,url,image,DEFAULT,SERVER);
    }


    public static void insertBaseData (SQLiteDatabase database){
        if(database == null){
            return;
        }
        //create a list of base channels
        List<ContentValues> list = new ArrayList<ContentValues>();

        list.add(
                valuesDefaultRadioChannel(
                    "Kiss FM",
                    "online-kissfm.tavrmedia.ua/KissFM",
                    "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1")
        );

        list.add(
                valuesDefaultRadioChannel(
                        "Европа плюс",
                        "ep256.hostingradio.ru:8052/europaplus256.mp3",
                        "http://online-red.com/images/radio-logo/europa-plus.png")
        );

        list.add(
                valuesDefaultRadioChannel(
                        "Радио Шансон",
                        "chanson.hostingradio.ru:8041/chanson256.mp3",
                        "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1")
        );

        list.add(
                valuesDefaultRadioChannel(
                        "Русский хит",
                        "s9.imgradio.pro/RusHit48",
                        "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1")
        );

        list.add(
                valuesDefaultRadioChannel(
                        "Зайцев.FM Club",
                        "zaycevfm.cdnvideo.ru/ZaycevFM_electronic_256.mp3",
                        "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1")
        );

        list.add(
                valuesDefaultRadioChannel(
                        "Зайцев.FM DISCO",
                        "zaycevfm.cdnvideo.ru/ZaycevFM_disco_256.mp3",
                        "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1")
        );

        list.add(
                valuesDefaultRadioChannel(
                        "Зайцев.FM Поп-музыка",
                        "zaycevfm.cdnvideo.ru/ZaycevFM_pop_256.mp3",
                        "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1")
        );

        list.add(
                valuesDefaultRadioChannel(
                        "Зайцев.FM Relax",
                        "zaycevfm.cdnvideo.ru/ZaycevFM_relax_256.mp3",
                        "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1")
        );

        list.add(
                valuesDefaultRadioChannel(
                        "Хит FM Dance",
                        "icecast.radiohitfm.cdnvideo.ru/st33.mp3",
                        "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1")
        );

        list.add(
                valuesDefaultRadioChannel(
                        "DFM Динамит",
                        "icecast.radiodfm.cdnvideo.ru/st16.mp3",
                        "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1")
        );

        list.add(
                valuesDefaultRadioChannel(
                        "Radio Enigma",
                        "195.242.219.208:8200/enigma",
                        "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1")
        );

        list.add(
                valuesDefaultRadioChannel(
                        "Comedy radio",
                        "ic7.101.ru:8000/v11_1",
                        "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1")
        );

        list.add(
                valuesDefaultRadioChannel(
                        "Радио Фантастики",
                        "fantasyradioru.no-ip.biz:8002/live",
                        "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1")
        );

        list.add(
                valuesDefaultRadioChannel(
                        "WarGaming.FM ",
                        "sv.wargaming.fm:8051/48",
                        "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1")
        );

        list.add(
                valuesDefaultRadioChannel(
                        "Relax FM",
                        "ic2.101.ru:8000/v13_1?setst=-1",
                        "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1")
        );

        //  дублируется по адресу "ic7.101.ru:8000/v1_1?"
        list.add(
                valuesDefaultRadioChannel(
                        "104.2FM NRJ",
                        "http://ic3.101.ru:8000/v1_1",
                        "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1")
        );

        list.add(
                valuesDefaultRadioChannel(
                        "Авторадио",
                        "http://ic3.101.ru:8000/v3_1",
                        "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1")
        );

        list.add(
                valuesDefaultRadioChannel(
                        "98.8FM Романтика",
                        "http://ic3.101.ru:8000/v4_1",
                        "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1")
        );

        //  дублируется по адресу ic2.101.ru:8000/v5_1
        list.add(
                valuesDefaultRadioChannel(
                        "Юмор FM",
                        "http://ic3.101.ru:8000/v5_1",
                        "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1")
        );

        list.add(
                valuesDefaultRadioChannel(
                        "Рок радио",
                        "air2.radiorecord.ru:805/rock_320",
                        "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1")
        );

        list.add(
                valuesDefaultRadioChannel(
                        "Супердискотека 90-х",
                        "air2.radiorecord.ru:805/sd90_320",
                        "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1")
        );

        list.add(
                valuesDefaultRadioChannel(
                        "Авторитетное радио (Красноярск 102.8)",
                        "84.22.142.130:8000/arstream",
                        "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1")
        );

        list.add(
                valuesDefaultRadioChannel(
                        "psyradio.fm",
                        "81.88.36.42:8010/",
                        "https://pp.userapi.com/c624316/v624316949/2a162/3L89a7ph_RE.jpg?ava=1")
        );


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
            e.printStackTrace();
            //too bad :(
        } finally {
            database.endTransaction();
        }

    }
}
