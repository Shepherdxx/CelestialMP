package com.example.shepherdxx.c_player.player;

import com.example.shepherdxx.c_player.data.Constants;

import java.util.ArrayList;

import static com.example.shepherdxx.c_player.data.Constants.MP_RADIO;
import static com.example.shepherdxx.c_player.data.Constants.MP_SD_U;
import static com.example.shepherdxx.c_player.data.Constants._PLAYLIST_All_Audio;
import static com.example.shepherdxx.c_player.data.Constants._PLAYLIST_Cache;
import static com.example.shepherdxx.c_player.data.Constants._PLAYLIST_RADIO;

/**
 * Created by Shepherdxx on 08.11.2017.
 */

public class PlayListInfo {

    // название	id плейлиста	лист id треков	кол-во песен	общ. пр-ность	картинка	Любимый?
    // для сокращений  playlist - pl_


    /**
     * ID of this playlist to manipulate
     */
    public long pl_Id;
    /**
     * Name of this playlist (used for the toast message)
     */
    public String pl_name;
    /**
     * Populate playlist using this audioIds
     */
    public ArrayList<MyTrackInfo> audioTracks;
    public ArrayList<Long> audioIds;

    public int size() {
        return audioTracks.size();
    }

    public long duration() {
        long d = 0;
        if (getPl_Id() != Constants.PLAYLIST_RADIO) {
            for (int i = 0; i < audioTracks.size(); i++) {
                d = d + audioTracks.get(i).getDuration();
            }
        }
        return d;
    }

    public PlayListInfo(long playlistId, String name, int type) {
        this.pl_Id = playlistId;
        this.pl_name = name;
        this.plType = type;
    }

    public PlayListInfo(long playlistId, String name) {
        this.pl_Id = playlistId;
        this.pl_name = name;
        this.plType = MP_SD_U;
    }

    public long getPl_Id() {
        return pl_Id;
    }

    public int plType;


    public long getPlaylistId() {
        return pl_Id;
    }

    public String getName() {
        return pl_name;
    }

    public static PlayListInfo All() {
        return new PlayListInfo(Constants.PLAYLIST_All_Audio, _PLAYLIST_All_Audio , MP_SD_U);
    }

    public static PlayListInfo Cache() {
        return new PlayListInfo(Constants.PLAYLIST_Cache, _PLAYLIST_Cache , MP_SD_U);
    }

    public static PlayListInfo Radio() {
        return new PlayListInfo(Constants.PLAYLIST_RADIO, _PLAYLIST_RADIO, MP_RADIO);
    }
}
