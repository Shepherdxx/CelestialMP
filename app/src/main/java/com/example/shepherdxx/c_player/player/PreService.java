package com.example.shepherdxx.c_player.player;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.shepherdxx.c_player.Main2Activity;

import static com.example.shepherdxx.c_player.data.PlayerActions.ACTION_NEXT_SONG;
import static com.example.shepherdxx.c_player.data.PlayerActions.ACTION_PLAY;
import static com.example.shepherdxx.c_player.data.PlayerActions.ACTION_PREV_SONG;
import static com.example.shepherdxx.c_player.data.PlayerActions.ACTION_RESUME;
import static com.example.shepherdxx.c_player.data.PlayerActions.ACTION_SAMPLE_PLAY;
import static com.example.shepherdxx.c_player.data.PlayerActions.ACTION_START;
import static com.example.shepherdxx.c_player.data.PlayerActions.ACTION_TOGGLE_PLAYBACK;
import static com.example.shepherdxx.c_player.player.Player_Background.hasInstance;

public class PreService extends Activity {
    final static int INTENT_FLAGS = Intent.FLAG_ACTIVITY_NEW_TASK
            | Intent.FLAG_ACTIVITY_CLEAR_TASK
            | Intent.FLAG_ACTIVITY_TASK_ON_HOME;

    /**
     * Returns an intent pointing to this activity.
     *
     * @param context the context to use.
     * @param action  the action to set.
     * @return an intent for this activity.
     */
    public static Intent getIntent(Context context, String action) {
        Intent intent = new Intent(context, PreService.class)
                .setFlags(INTENT_FLAGS)
                .setAction(action);
        return intent;
    }

    public static Intent startSampleBGService(Context context, String data) {
        Intent intent = controlBGService(context, ACTION_SAMPLE_PLAY);
        Bundle b = new Bundle();
        b.putString("Sample_data", data);
        intent.putExtra("Bundle", b);
        Log.i("PreService", "startSampleBGService");
        return intent;
    }

    public static Intent startBGService(Context context, int id, int pos) {
        Intent intent = controlBGService(context, ACTION_PLAY);
        Bundle b = new Bundle();
        b.putInt("Playlist", id);
        b.putInt("MPData", pos);
        intent.putExtra("Bundle", b);
        Log.i("PreService#1", "Playlist" + id +
                "MPData" + pos
        );
        return intent;
    }

    public static Intent controlBGService(Context context, String action) {
        Intent intent = new Intent(context, Player_Background.class);
        intent.setAction(action);
        return intent;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String action = getIntent().getAction();
        if (action != null)
            switch (action) {
                case ACTION_PLAY:
                    Intent intent = new Intent(this, Main2Activity.class);
                    if (hasInstance()) {
                        intent.setAction(ACTION_RESUME);
                    } else intent.setAction(ACTION_START);
                    Log.i("PreService#1", intent.getAction());
                    startActivity(intent);
                    break;
                case ACTION_TOGGLE_PLAYBACK:
                case ACTION_NEXT_SONG:
                case ACTION_PREV_SONG:
                    startService(controlBGService(this, action));
                    break;
//                case MP_BG_Service.ACTION_PAUSE:
//                case MP_BG_Service.ACTION_TOGGLE_PLAYBACK_DELAYED:
//                case MP_BG_Service.ACTION_RANDOM_MIX_AUTOPLAY:
//                case MP_BG_Service.ACTION_NEXT_SONG_DELAYED:
//                case MP_BG_Service.ACTION_NEXT_SONG_AUTOPLAY:
//                case MP_BG_Service.ACTION_PREVIOUS_SONG_AUTOPLAY:
//                case MP_BG_Service.ACTION_CYCLE_SHUFFLE:
//                case MP_BG_Service.ACTION_CYCLE_REPEAT:
                default:
                    throw new IllegalArgumentException("No such action: " + action);
            }
        Log.i("PreService", "get action " + action);
        finish();
    }
}
