package com.example.shepherdxx.c_player.data;

import android.net.Uri;
import android.os.Environment;

import java.io.File;

public class DataConstants {
    /**
     * Data for {@Link Contract#CONTENT_URI}
     */
    public static final String CONTENT_AUTHORITY = "com.shepherdxx.celestialmp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_RADIO_CHANNELS = "channel_list";

    public static final File MyCachePath = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_MUSIC + File.separator+"MyCache"+ File.separator);

    public static final File MusicPath= Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_MUSIC+ File.separator);
}
