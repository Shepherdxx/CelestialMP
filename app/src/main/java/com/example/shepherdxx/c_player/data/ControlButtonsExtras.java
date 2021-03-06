package com.example.shepherdxx.c_player.data;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.shepherdxx.c_player.R;
import com.example.shepherdxx.c_player.player.MyTrackInfo;
import com.example.shepherdxx.c_player.player.Player_Background;
import com.example.shepherdxx.c_player.player.PreService;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static com.example.shepherdxx.c_player.data.Constants.MP_EMPTY;
import static com.example.shepherdxx.c_player.data.Constants.MP_ERROR;
import static com.example.shepherdxx.c_player.data.Constants.MP_PAUSE;
import static com.example.shepherdxx.c_player.data.Constants.MP_PLAY;
import static com.example.shepherdxx.c_player.data.Constants.MP_PREPARE;
import static com.example.shepherdxx.c_player.data.Constants.MP_PREPARE_RADIO;
import static com.example.shepherdxx.c_player.data.Constants.MP_RADIO;
import static com.example.shepherdxx.c_player.data.Constants.MP_SD_U;
import static com.example.shepherdxx.c_player.data.Constants.MP_STARTED;
import static com.example.shepherdxx.c_player.data.Constants.MP_STOPED;
import static com.example.shepherdxx.c_player.data.Constants.PLAYLIST_RADIO;
import static com.example.shepherdxx.c_player.data.PlayerActions.ACTION_FORWARD;
import static com.example.shepherdxx.c_player.data.PlayerActions.ACTION_NEXT_SONG;
import static com.example.shepherdxx.c_player.data.PlayerActions.ACTION_PREV_SONG;
import static com.example.shepherdxx.c_player.data.PlayerActions.ACTION_TOGGLE_PLAYBACK;
import static com.example.shepherdxx.c_player.data.PlayerActions.ACTION_TOWARD;
import static com.example.shepherdxx.c_player.player.Player_Background.get;
import static com.example.shepherdxx.c_player.player.Player_Background.hasInstance;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Created by Shepherdxx on 26.09.2017.
 */

public class ControlButtonsExtras implements
        Runnable
        , View.OnClickListener {

    private View view;
    private SeekBar seekBar;
    private Handler myHandler;
    private TextView sTime;
    private TextView fTime;
    private TextView rTitle;
    private Activity activity;
    private ControlPanelButtonListener listener;
    private String Log_Tag = ControlButtonsExtras.class.getSimpleName();
    private ImageButton btPlay, btFw, btTw, btNext, btPrev;
    private LinearLayout LL;
    private PopUpToast toast;
    private long finalTime;
    private boolean visibilityCheck = true;

    public static ControlButtonsExtras setUpdateSongTime(Activity activity,
                                                         final Handler myHandler,
                                                         final View view,
                                                         final ControlPanelButtonListener listener) {
        return new ControlButtonsExtras(activity, myHandler, view, listener);
    }

    private ControlButtonsExtras(Activity activity,
                                 Handler myHandler,
                                 View outerView,
                                 ControlPanelButtonListener listener) {
        this.activity = activity;
        this.myHandler = myHandler;
        this.view = outerView;
        this.listener = listener;
        setBroadcastReceiver();
        findView();
        updateUi();
    }


    private Player_Background serviceOn = null;

    private MyTrackInfo currentTrack() {
        MyTrackInfo track = null;
        if (hasInstance()) {
            Player_Background service = get(activity);
            track = service.getTrackInfo();
            serviceOn = service;
        }
        return track;
    }

    private int state() {
        if (serviceOn != null) return serviceOn.MPState;
        return MP_PLAY;
    }

    private int MP_Type() {
        int type, id = MP_EMPTY;
        if (currentTrack() != null) {
            id = currentTrack().getPlaylistId();
            Log.i(Log_Tag, "PlaylistId" + File.pathSeparator + id);
        }
        switch (id) {
            case PLAYLIST_RADIO:
                type = MP_RADIO;
                break;
            case MP_EMPTY:
                type = MP_EMPTY;
                break;
            default:
                type = MP_SD_U;
                break;
        }
        Log.i(Log_Tag, "MP_Type" + File.pathSeparator + type);
        return type;
    }

    private long curTime() {
        if (serviceOn != null) return serviceOn.getCurTime();
        return 0;
    }

    private boolean layoutEx() {
        return (LL != null);
    }

    @Override
    public void run() {
        updateUi();
        if (Build.VERSION.SDK_INT >= 21) {
            if (!activity.requestVisibleBehind(true)) myHandler.removeCallbacks(this);
            else myHandler.postDelayed(this, 500);
        } else {
            if (activity.isFinishing()) myHandler.removeCallbacks(this);
            myHandler.postDelayed(this, 500);
        }
    }

    @Override
    public void onClick(View v) {
        String action;
        if (currentTrack() != null) {
            switch (v.getId()) {
                case R.id.fr_pausePlay:
                    action = ACTION_TOGGLE_PLAYBACK;
                    break;
                case R.id.fr_forward:
                    action = ACTION_FORWARD;
                    break;
                case R.id.fr_toward:
                    action = ACTION_TOWARD;
                    break;
                case R.id.fr_next_song:
                    action = ACTION_NEXT_SONG;
                    break;
                case R.id.fr_back_song:
                    action = ACTION_PREV_SONG;
                    break;
                default:
                    action = ACTION_TOGGLE_PLAYBACK;
            }
            activity.startService(
                    PreService.controlBGService(activity, action));
        } else toastMessage("Нечего воспроизводить");
    }

    /**
     * Отображает продолжительность и название песни
     */
    public void loadRadio() {
        String SongTitle = activity.getResources().getString(R.string.action_loading);
        if (currentTrack() != null) {
            SongTitle += " " + currentTrack().getTitle();
            if (rTitle != null) rTitle.setText(SongTitle);
            if (listener != null) listener.getTrackName(SongTitle);
            Log.i(Log_Tag, "SongTitle  " + File.pathSeparator + SongTitle);
        }
    }

    public void Title() {
        Title(null);
    }

    /**
     * Отображает продолжительность и название песни
     */
    public void Title(String SongTitle) {
        long Duration = 0;
        if (currentTrack() != null) {
            SongTitle = currentTrack().getTitle();
            Duration = currentTrack().getDuration();
            finalTime = Duration;
            setTime(fTime, finalTime);
            setTime(sTime, curTime());
            if (seekBar != null) seekBar.setMax((int) finalTime);
        }
        if (rTitle != null) rTitle.setText(SongTitle);
        if (listener != null) listener.getTrackName(SongTitle);
        Log.i(Log_Tag, "SongTitle  " + File.pathSeparator + SongTitle);
        Log.i(Log_Tag, "Title Time " + File.pathSeparator + Duration);
    }

    /**
     * ButtonCheckEvent - меняет вид кнопки в зависимости
     * от состояние проигрывателя
     */
    private void ButtonCheckEvent() {
        try {
            if (state() != MP_PAUSE) {
                btPlay.setImageResource(R.drawable.ic_pause_circle_outline_black_48dp);
                if (listener != null) listener.toolBarButton(true);
            } else {
                btPlay.setImageResource(R.drawable.ic_play_circle_outline_black_48dp);
                if (listener != null) listener.toolBarButton(false);
            }
        } catch (
                NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Поведение ползунка
     */
    private void seekBarListen(SeekBar seekBar) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (serviceOn != null) serviceOn.setCurTime(seekBar.getProgress());
            }
        });
    }

    /**
     * Изменяет состояние ползунка бара
     */
    private void seekBarProgress() {
        if (seekBar != null) {
            long startTime = curTime();
            setTime(sTime, startTime);
            seekBar.setProgress((int) startTime);
        }
    }

    private void setTime(TextView v, long time) {
        long min = MILLISECONDS.toMinutes(time);
        long sec = MILLISECONDS.toSeconds(time) -
                TimeUnit.MINUTES.toSeconds(MILLISECONDS.toMinutes(time));
        if (v != null) v.setText(String.format("%d:%d", min, sec));
//        Log.i("Controls", "setTime" + String.format("%d:%d", min, sec));
    }

    private void stop() {
        myHandler.removeCallbacks(this);
        ButtonCheckEvent();
    }

    private void setBroadcastReceiver() {
        BroadcastReceiver MP_start = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    String action = intent.getAction();
                    if (action != null)
                        switch (action) {
                            case MP_PREPARE_RADIO:
                                Log.i(Log_Tag, "BroadcastReceiver received MP_PREPARE_RADIO");
                                visibilityCheck = true;
                                run();
                                loadRadio();
                                break;
                            case MP_STARTED:
                                Log.i(Log_Tag, "BroadcastReceiver received MP_STARTED");
                                visibilityCheck = true;
                                run();
                                Title();
                                break;
                            case MP_STOPED:
                                Log.i(Log_Tag, "BroadcastReceiver received MP_STOPED");
                                stop();
                                break;
                            case MP_PREPARE:
                                visibilityCheck = true;
                                updateUi();
                                Title();
                                break;
                            case MP_ERROR:
                                visibilityCheck = true;
                                Title("Choose a song");
                                updateUi();
                                break;
                        }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        };
        //on Play
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MP_PREPARE_RADIO);
        intentFilter.addAction(MP_PREPARE);
        intentFilter.addAction(MP_STARTED);
        intentFilter.addAction(MP_STOPED);
        intentFilter.addAction(MP_ERROR);
        LocalBroadcastManager.getInstance(activity).registerReceiver(MP_start, intentFilter);
    }

    private void findView() {
        //Они будут на всех панелях
        LL = view.findViewById(R.id.fr_control_layout);

        btNext = view.findViewById(R.id.fr_next_song);
        btPrev = view.findViewById(R.id.fr_back_song);
        btPlay = view.findViewById(R.id.fr_pausePlay);

        btPlay.setOnClickListener(this);
        btNext.setOnClickListener(this);
        btPrev.setOnClickListener(this);

        //проигрватель треков
        seekBar = view.findViewById(R.id.fr_sb_seekBar);

        btFw = view.findViewById(R.id.fr_forward);
        btTw = view.findViewById(R.id.fr_toward);

        btFw.setOnClickListener(this);
        btTw.setOnClickListener(this);

        sTime = view.findViewById(R.id.fr_st_time);
        fTime = view.findViewById(R.id.fr_tl_time);

        rTitle = view.findViewById(R.id.radio_title);
    }

    private void updateUi() {
        if (visibilityCheck) {
            int checkId = MP_Type();
            switch (checkId) {
                case MP_RADIO:
                    if (layoutEx()) {
                        LL.setVisibility(View.VISIBLE);

                        btFw.setVisibility(View.GONE);
                        btTw.setVisibility(View.GONE);
                        sTime.setVisibility(View.GONE);
                        fTime.setVisibility(View.GONE);

                        if (seekBar != null) {
                            seekBar.setVisibility(View.GONE);
                            seekBar = null;
                        }

                        rTitle.setVisibility(View.VISIBLE);
                    }
                    break;
                case MP_SD_U:
                    if (layoutEx()) {
                        LL.setVisibility(View.VISIBLE);

                        btFw.setVisibility(View.VISIBLE);
                        btTw.setVisibility(View.VISIBLE);
                        sTime.setVisibility(View.VISIBLE);
                        fTime.setVisibility(View.VISIBLE);

                        if (seekBar == null) seekBar = view.findViewById(R.id.fr_sb_seekBar);
                        seekBar.setVisibility(View.VISIBLE);
                        seekBarListen(seekBar);

                        rTitle.setVisibility(View.GONE);
                        Log.i(Log_Tag, "LL_Visibility == VISIBLE");
                    }
                    break;

                default:
                    if (layoutEx()) {
                        LL.setVisibility(View.GONE);
                        Log.i(Log_Tag, "LL_Visibility == GONE");
                    }
                    break;
            }
            visibilityCheck = false;
            Log.i(Log_Tag, "visibilityCheck complete");
        }
        long startTime = curTime();
        ButtonCheckEvent();
        if (finalTime > startTime) seekBarProgress();
    }

    /**
     * Всплывающие сообщения
     */
    private void toastMessage(String cToast) {
        toast = new PopUpToast(activity.getBaseContext());
        toast.setMessage(cToast);
    }
}
