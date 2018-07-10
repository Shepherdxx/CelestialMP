package com.example.shepherdxx.c_player.player;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.shepherdxx.c_player.R;
import com.example.shepherdxx.c_player.data.Constants;
import com.example.shepherdxx.c_player.data.PlayerActions;
import com.example.shepherdxx.c_player.data.PopUpToast;

import java.io.File;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.example.shepherdxx.c_player.data.Constants.BUNDLE;
import static com.example.shepherdxx.c_player.data.Constants.DEFAULT_R_M;
import static com.example.shepherdxx.c_player.data.Constants.MP_EMPTY;
import static com.example.shepherdxx.c_player.data.Constants.MP_ERROR;
import static com.example.shepherdxx.c_player.data.Constants.MP_HTTP;
import static com.example.shepherdxx.c_player.data.Constants.MP_PAUSE;
import static com.example.shepherdxx.c_player.data.Constants.MP_PLAY;
import static com.example.shepherdxx.c_player.data.Constants.MP_PREPARE;
import static com.example.shepherdxx.c_player.data.Constants.MP_PREPARE_RADIO;
import static com.example.shepherdxx.c_player.data.Constants.MP_RADIO;
import static com.example.shepherdxx.c_player.data.Constants.MP_RAW;
import static com.example.shepherdxx.c_player.data.Constants.MP_SD;
import static com.example.shepherdxx.c_player.data.Constants.MP_SD_U;
import static com.example.shepherdxx.c_player.data.Constants.MP_STARTED;
import static com.example.shepherdxx.c_player.data.Constants.MP_STOPED;


public class Player_Background
        extends Service
        implements MediaPlayer.OnCompletionListener
        , SharedPreferences.OnSharedPreferenceChangeListener
        , AudioManager.OnAudioFocusChangeListener
        , OnPreparedListener {


    String MP_LOG_TAG = "BackgroundService";
    public Player_Sample mPlayer = null;
    public int MPState = MP_EMPTY;
    private int MPType;
    private int mCurPosition, saved_position;
    private long saved_track_time;
    String MPData;
    String SongTitle;

    Context context = this;
    Bundle Bondiana;
    BroadcastReceiver mNoiseReceiver;
    IntentFilter nF;

    SharedPreferences sharedPreferences;
    public static String rMode;
    public static int soundVolume;
    public String REPEAT_MODE;
    public String VOLUME;
    long forwardTime = 5000;
    long backwardTime = 5000;
    String SampleData = "SampleData";
    PlayListInfo playListInfo;
    //Постановка на паузу системой
    boolean tempoPause = false;
    //обозначение AudioManager
    private AudioManager am;

    private String LOG_TAG = Player_Background.class.getSimpleName();

    boolean mStop = false;
    boolean mPause = false;

    //Установка громкости
    public void setVolume() {
        final int MAX_VOLUME = 101;
        final float volume = (float) (1 - (Math.log(MAX_VOLUME - soundVolume) / Math
                .log(MAX_VOLUME)));
        try {
            mPlayer.setVolume(volume, volume);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    //Установка режима повтора
    private void setLoop() {
        if (rMode.equals(getString(R.string.repeat))) mPlayer.setLooping(true);
        else mPlayer.setLooping(false);
    }

    //Действия
    //Остановка проигрывателя
    private void mpStop() {
        startTimeBefore = mPlayer.getCurrentPosition();
        if (mStop) mPlayer.stop();
        if (tempoPause || mPause) mPlayer.pause();
        MPState = getState();
//        updateWidgets();
        sendMyBroadcast(new Intent(MP_STOPED));
    }

    //Запуск проигрывателя
    private void mpStart() {
        mPlayer.start();
        tempoPause = false;
        mStop = false;
        MPState = getState();
//       updateWidgets();
        sendMyBroadcast(new Intent(MP_STARTED));
    }

    //кнопка вперед на 5 сек
    void ForwardScript() {
        long startTime = mPlayer.getCurrentPosition();
        long finalTime = mPlayer.getDuration();
        if ((startTime + forwardTime) <= finalTime) {
            startTime = startTime + forwardTime;
            mPlayer.seekTo((int) startTime);
        } else {
            toastMessage("Cannot jump forward 5 seconds");
        }
    }

    //кнопка назад на 5 сек
    void TowardScript() {
        long startTime = mPlayer.getCurrentPosition();
        if ((startTime - backwardTime) > 0) {
            startTime = startTime - backwardTime;
            mPlayer.seekTo((int) startTime);
        } else {
            toastMessage("Cannot jump backward 5 seconds");
        }
    }

    //Предыдущий трек
    private void prevSong() {
        PlayListInfo playListInfo = curPlaylist();
        boolean b = (mPlayer.isPlaying());
        OnPreparedListener onPreparedListener = deltaCheck(PREV_SONG) & b ? this : null;
        Create(playListInfo, deltaChange(PREV_SONG), onPreparedListener);
    }

    //Следующий  трек
    private void NextSong() {
        PlayListInfo playListInfo = curPlaylist();
        boolean b = (mPlayer.isPlaying());
        OnPreparedListener onPreparedListener = deltaCheck(NEXT_SONG) & b ? this : null;
        Log.i(LOG_TAG, "deltaCheck " + File.pathSeparator + deltaCheck(NEXT_SONG));
        Create(playListInfo, deltaChange(NEXT_SONG), onPreparedListener);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void setBroadcastReceiver() {
        mNoiseReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int state = getState();
                String action = intent.getAction();
                if (action != null) {
                    switch (action) {
                        case AudioManager.ACTION_AUDIO_BECOMING_NOISY:
                            Log.i(LOG_TAG, "onReceive BECOMING_NOISY");
                            //pause the music
                            if (state == MP_PLAY) {
                                Log.i(LOG_TAG, "BECOMING_NOISY активирован");
                                tempoPause = true;
                                onAir();
                                toastMessage("Гарнитура отключена");
                                beNoisy = true;
                            }
                            break;
                        case AudioManager.ACTION_HEADSET_PLUG:
                            int plug = intent.getIntExtra("state", 0);
                            if (plug == 1) {
                                Log.i(LOG_TAG, "onReceive HEADSET_PLUG");
                                if (!mPause && beNoisy) {
                                    Log.i(LOG_TAG, "HEADSET_PLUG активирован");
                                    onAir();
                                    toastMessage("Гарнитура на месте, продолжаем");
                                }
                            }
                            break;
                    }
                }
            }
        };
        //on Play
        nF = new IntentFilter();
        nF.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            nF.addAction(AudioManager.ACTION_HEADSET_PLUG);
        }
        registerReceiver(mNoiseReceiver, nF);
    }


    @Override
    public void onCreate() {
        super.onCreate();

        setup_sPref();
        am = (AudioManager) getSystemService(AUDIO_SERVICE);

        //остановка музыки когда выпал наушник
        setBroadcastReceiver();

//        initWidgets();
        sInstance = this;
        synchronized (sWait) {
            if (mPlayer == null) {
                try {
                    mCurPosition = sharedPreferences.getInt("lastPos", 0);
                    currentPlaylistId = sharedPreferences.getInt("lastId", -1);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            sWait.notifyAll();
        }

    }

    //Запрос на восрроизвдение
    private void reqAudioFocus() {
        // Request audio focus for play back
        int result = am.requestAudioFocus(this,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.i(LOG_TAG, "AUDIOFOCUS_REQUEST_GRANTED");
            mpStart();
            beNoisy = false;
        } else if (result == AudioManager.AUDIOFOCUS_REQUEST_FAILED) {
            tempoPause = true;
            mpStop();
            Log.i(LOG_TAG, "AUDIOFOCUS_REQUEST_FAILED");
            toastMessage("Some Problem");
        }
    }

    //Запрос состояния интернет подключения
    private boolean connectionCheck() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = false;
        try {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork.isConnectedOrConnecting();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (!isConnected) toastMessage(Constants.iERROR);
            Log.i("MP_BG_service", "checkConnection " + isConnected);
        }
        return isConnected;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            boolean IntentCheck = intent.hasExtra(BUNDLE);
            String action;

            if (mPlayer == null) {
                Log.i(MP_LOG_TAG, "mPlayer created");
                mPlayer = new Player_Sample();
            }
            action = intent.getAction();
            if (action != null)
                switch (action) {
                    case PlayerActions.ACTION_PAUSE:
                        if (mPlayer.isPlaying()) onAir();
                        break;
                    case PlayerActions.ACTION_TOGGLE_PLAYBACK:
                        if (MPType == MP_HTTP) {
                            playListInfo = gainPlaylist(saved_pl_id);
                            if (playListInfo == null) {
                                toastMessage("audioTracks not found");
                                break;
                            }
                            Create(playListInfo, saved_position, this);
                            mPlayer.seekTo((int) saved_track_time);
                        }
                        if (isOnAir()) mPause = true;
                        else mPause = false;
                        onAir();
                        break;
                    case PlayerActions.ACTION_FORWARD:
                        ForwardScript();
                        break;
                    case PlayerActions.ACTION_TOWARD:
                        TowardScript();
                        break;
                    case PlayerActions.ACTION_NEXT_SONG:
                        NextSong();
                        break;
                    case PlayerActions.ACTION_PREV_SONG:
                        prevSong();
                        break;
                }
            if (IntentCheck) {
                Bondiana = intent.getBundleExtra(BUNDLE);
                if (action != null)
                    switch (action) {
                        case PlayerActions.ACTION_PLAY:
                            currentPlaylistId = Bondiana.getInt("Playlist");
                            mCurPosition = Bondiana.getInt("MPData");
                            Log.i(LOG_TAG, PlayerActions.ACTION_PLAY + " " + currentPlaylistId + " MPData " + mCurPosition);
                            playListInfo = gainPlaylist(currentPlaylistId);
                            if (playListInfo == null) {
                                toastMessage("audioTracks not found");
                                break;
                            }
                            Create(playListInfo, mCurPosition, this);
                            break;
                        case PlayerActions.ACTION_SAMPLE_PLAY:
                            if (mPlayer != null && MPType != MP_HTTP) {
                                saved_pl_id = currentPlaylistId;
                                saved_position = mCurPosition;
                                saved_track_time = mPlayer.getCurrentPosition();
                                mpStop();
                            }
                            String Data = Bondiana.getString("Sample_data");
                            if (!SampleData.equals(Data)) {
                                SampleData = Data;
                                MPType = MP_HTTP;
                                CreateMp(MP_HTTP, Data, this);
                            } else {
                                if (mPlayer != null) {
                                    if (mPlayer.isPlaying()) mPlayer.pause();
                                    else mPlayer.start();
                                }
                            }
                            break;
                    }
            }
            Log.i(LOG_TAG, "onStartCommand " + action + MPData + " gained");

        }
        return START_NOT_STICKY;
    }


    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // убираем всплывающие сообщения
        toastMessage();

        // закрываем проигрыватель
        releaseMediaPlayer();

//        // Unregister OnPreferenceChangedListener to avoid any memory leaks.
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
    @Override
    public void onAudioFocusChange(int focusChange) {
        Log.i("onAudioFocusChange", String.valueOf(focusChange));
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                Log.i(LOG_TAG, "AUDIO_FOCUS_LOSS_TRANSIENT");
                tempoPause = true;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                Log.i(LOG_TAG, "AUDIO_FOCUS_LOSS_TRANSIENT_CAN_DUCK");
                tempoPause = true;
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.
                break;
            case AudioManager.AUDIOFOCUS_GAIN:
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                Log.i(LOG_TAG, "AUDIO_FOCUS_GAIN");
                if (tempoPause & !mPause) onAir();
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                Log.i(LOG_TAG, "AUDIO_FOCUS_LOSS");
                tempoPause = true;
                if (isOnAir()) onAir();
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback
                break;
        }
    }

    @Override
    public void onLowMemory() {
        stopSelfResult(START_REDELIVER_INTENT);
    }

    private static final int NEXT_SONG = 1;
    private static final int PREV_SONG = -1;

    private int deltaChange(int delta) {
        int sum = mCurPosition + delta;
        if (deltaCheck(delta)) mCurPosition = sum;
        return mCurPosition;
    }

    private boolean deltaCheck(int delta) {
        PlayListInfo playListInfo = curPlaylist();
        int sum = mCurPosition + delta;
        int length = playListInfo.size();
        Log.i(LOG_TAG, "deltaCheck summ" + File.pathSeparator + sum);
        switch (delta) {
            case 1:
                if (sum <= length - 1)
                    return true;
                break;
            case -1:
                if (sum >= 0)
                    return true;
                break;
            default:
                return false;
        }
        return false;
    }

    private void getMPData(PlayListInfo playListInfo, int Position) {
        MPType = MP_EMPTY;
        int length = playListInfo.size();
        if (length != 0) {
            if (Position > length) {
                Position = 0;
                mCurPosition = Position;
            }
            MyTrackInfo pti = playListInfo.audioTracks.get(Position);
            trackInfo = pti;
            MPData = pti.getData();
            SongTitle = pti.getTitle();
            MPType = playListInfo.plType;
            int id = (int) playListInfo.pl_Id;
            if (id != Constants.PLAYLIST_RADIO) {
                sharedPreferences.edit()
                        .putInt("last_MP_SD_U_Id", id)
                        .putInt("last_MP_SD_U_Pos", Position)
                        .apply();
            }
            sharedPreferences.edit()
                    .putInt("lastId", id)
                    .putInt("lastPos", Position)
                    .apply();
            Log.i(LOG_TAG, "getMPData " + length);
            Log.i(LOG_TAG, "getMPData " + String.valueOf(mCurPosition));
        } else toastMessage("Playlist is empty.");
    }

    int currentPlaylistId, saved_pl_id;

    private PlayListInfo gainPlaylist(int id) {
        return new PlayList(context).createPlaylist(id);
    }

    private PlayListInfo curPlaylist() {
        return gainPlaylist(currentPlaylistId);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        NextSong();
        onAir();
    }


    boolean beNoisy = false;
    long startTimeBefore;

    private boolean isOnAir() {
        boolean b = false;
        if (getState() == 1) b = true;
        return b;
    }

    //Старт/Пауза плеера.
    public void onAir() {
        if (mPlayer != null) {
            if (!isOnAir()) {
                reqAudioFocus();
                Log.i(LOG_TAG, "ON_AIR");
            } else {
                mpStop();
                Log.i(LOG_TAG, "OFF_AIR");
            }
        }
    }

    public void sendMyBroadcast(Intent i) {
        Log.i(LOG_TAG, "Broadcasting message");
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }

    //Сброс проигрывателя
    public void releaseMediaPlayer() {
        if (mPlayer != null) {
            // дерегистрируем (выключаем) BroadcastReceiver
            unregisterReceiver(mNoiseReceiver);
            try {
                stopPlayer();
                mPlayer.reset();
                mPlayer.release();
                // Set the media Player back to null.
                mPlayer = null;
                Log.e(MP_LOG_TAG, "releaseMediaPlayer completed");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void stopPlayer() {
        mStop = true;
        mpStop();
    }


    //Всплывающие сообщения
    private PopUpToast toast;

    void toastMessage(String cToast) {
        if (toast == null)
            toast = new PopUpToast(context);
        toast.setMessage(cToast);
    }

    void toastMessage() {
        toast.cancel();
    }


    public void Create(PlayListInfo playListInfo, int Position, OnPreparedListener listener) {
        getMPData(playListInfo, Position);
        int mpType = MPType;
        String Data = MPData;
        CreateMp(mpType, Data, listener);
    }

    public void CreateMp(int mpType, String Data, OnPreparedListener listener) {
        releaseMediaPlayer();
        mPlayer = Player_Sample.newPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        switch (mpType) {
            case MP_RADIO:
                if (connectionCheck()) {
                    mPlayer.radio_Version(mPlayer, Data, listener);
                    sendMyBroadcast(new Intent(MP_PREPARE_RADIO));
//                        if (mCurPosition == 6) {
//                            Intent intent = new Intent(context, LoaderActivity.class);
//                            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                        }
                } else nullSong();
                break;
            case MP_RAW:
                mPlayer.MP_RAW_Version(mPlayer, context, Data, listener);
                sendMyBroadcast(new Intent(MP_PREPARE));
                break;
            case MP_SD:
                mPlayer.SD_Version(mPlayer, Data, listener);
                mPlayer.setOnCompletionListener(this);
                sendMyBroadcast(new Intent(MP_PREPARE));
                break;
            case MP_SD_U:
                mPlayer.SD_U_Version(mPlayer, Data, listener);
                mPlayer.setOnCompletionListener(this);
                sendMyBroadcast(new Intent(MP_PREPARE));
                break;
            case MP_HTTP:
                mPlayer.http_Version(mPlayer, Data, listener);
                break;
            default:
                nullSong();
                break;
        }
        registerReceiver(mNoiseReceiver, nF);
        setVolume();
        setLoop();
    }

    void nullSong() {
        MPState = MP_EMPTY;
        trackInfo = null;
//        updateWidgets();
        sendMyBroadcast(new Intent(MP_ERROR));
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        onAir();
    }


    /**
     * Object used for PlaybackService startup waiting.
     */
    private static final Object[] sWait = new Object[0];
    /**
     * The appplication-wide instance of the PlaybackService.
     */
    public static Player_Background sInstance;

    public static boolean hasInstance() {
        return sInstance != null;
    }

//
//    public Player_Sample getPlayer() {
//        return mPlayer;
//    }
//    public static boolean hasPlayer() {
//        if (hasInstance()) return sInstance.getPlayer()!= null;
//        return false;
//    }

//    private void initWidgets()
//    {
//        AppWidgetManager manager = AppWidgetManager.getInstance(this);
//        SmallWidget.checkEnabled(this, manager);
//
//    }

//    private void updateWidgets()
//    {
//        AppWidgetManager manager = AppWidgetManager.getInstance(this);
//        if (MPState == Constants.MP_EMPTY)
//            SongTitle = context.getResources().getString(R.string.appwidget_text);
//
//        SmallWidget.updateAppWidget(this, manager, SongTitle, MPState);
//    }

    public static Player_Background get(Context context) {

        if (sInstance == null) {
            context.startService(new Intent(context, Player_Background.class));

            while (sInstance == null) {
                try {
                    synchronized (sWait) {
                        sWait.wait();
                    }
                } catch (InterruptedException ignored) {
                }
            }
        }

        return sInstance;
    }

    MyTrackInfo trackInfo = null;

    public MyTrackInfo getTrackInfo() {
        return trackInfo;
    }

    public long getCurTime() {
        return mPlayer.getCurrentPosition();
    }

    public void setCurTime(int time) {
        mPlayer.seekTo(time);
    }

    public int getmCurPosition() {
        return mCurPosition;
    }

    public int getState() {
        int state = MP_PAUSE;
        if (mPlayer.isPlaying()) state = MP_PLAY;
        return state;
    }

    private void setup_sPref() {
        sharedPreferences = getDefaultSharedPreferences(this);
        REPEAT_MODE = getResources().getString(R.string.key_player_repeat_mode);
        rMode = sharedPreferences.getString(REPEAT_MODE, DEFAULT_R_M);
        VOLUME = getResources().getString(R.string.key_apps_volume);
        soundVolume = mSoundVolume(VOLUME);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        Log.i("setup_sPref", sharedPreferences.getAll().toString() + REPEAT_MODE + File.pathSeparator + rMode);

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.i("onSPChanged", "yes" + File.pathSeparator + key);
        String[] keys = new String[]{REPEAT_MODE, VOLUME};
        if (key.equals(keys[0])) {
            rMode = sharedPreferences.getString(key, DEFAULT_R_M);
            setLoop();
            Log.i("onSPChanged", "rMode" + File.pathSeparator + rMode);
        }
        if (key.equals(keys[1])) {
            soundVolume = mSoundVolume(key);
            setVolume();
            Log.i("onSPChanged", "soundVolume" + File.pathSeparator + soundVolume);
        }
    }

    private int mSoundVolume(String key) {
        int Volume = sharedPreferences.getInt(key, 50);
        int value = 50;
        try {
            value = Volume;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            sharedPreferences.edit().putInt(key, value).apply();
        } finally {
            return value;
        }
    }
}
