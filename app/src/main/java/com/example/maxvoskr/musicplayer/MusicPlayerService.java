package com.example.maxvoskr.musicplayer;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener {

    private final int SONG_MODE = 0;
    private final int ALBUM_MODE = 1;
    private final int FLASHBACK_MODE = 2;
    private int mode = SONG_MODE;

    private Context context;
    Callbacks activity;

    int songIndex;
    private boolean paused = false;
    private MediaPlayer mediaPlayer;
    private ArrayList<Song> songs;
    private FlashbackPlaylist flashbackPlaylist;
    public static CurrentLocationTimeData currentLocationTimeData;

    public MusicPlayerService() {}


    private final IBinder binder = new MusicPlayerBinder();

    public class MusicPlayerBinder extends Binder {
        MusicPlayerService getMusicPlayerService() { return MusicPlayerService.this; }
    }

    @Override
    public IBinder onBind(Intent intent) { return binder; }

    @Override
    public void onCreate() {
        super.onCreate();
        songIndex = 0;
        context = getApplicationContext();
        currentLocationTimeData = new CurrentLocationTimeData(context);
        flashbackPlaylist = new FlashbackPlaylist(MusicArrayList.musicList);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Will need to implement this if multiple activities will be binding to this service.
        return START_STICKY;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Toast.makeText(context, "In onPrepared", Toast.LENGTH_SHORT).show();
        mp.start();
        currentLocationTimeData.updateTempData();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }

    private void initMusicPlayer() {
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
    }

    /* Set playlist for MusicPlayer to play through */
    public void setList(ArrayList<Song> playlist) {
        songs = playlist;
        songIndex = 0;
    }

    /* Play current song */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void playSong() {
        if(paused) {
            paused = false;
            mediaPlayer.start();
            return;
        }
        if(mediaPlayer != null) {
            if(mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }

        mediaPlayer = MediaPlayer.create(context, songs.get(songIndex).getSong());
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                currentLocationTimeData.updateSongUsingTemp(songs.get(songIndex));
                if(mode == ALBUM_MODE) {
                    if (++songIndex >= songs.size() && !songs.isEmpty()) {
                        playSong();
                    }
                } else if (mode == FLASHBACK_MODE) {
                    songs = new ArrayList<Song>(1);
                    songs.set(0, flashbackPlaylist.getNextSong());
                    songIndex = 0;
                    playSong();
                } else {
                    mediaPlayer.release();
                }
                activity.updateUI();
                Toast.makeText(context, "In onCompletion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* Pause the song */
    public void pause() {
        if (mediaPlayer.isPlaying()) {
            paused = true;
            mediaPlayer.pause();
        }
    }

    /* Set the mode of the MusicPlayerService according to the following constants.
           private final int SONG_MODE = 0;
           private final int ALBUM_MODE = 1;
           private final int FLASHBACK_MODE = 2;
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    /* Restart song */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void reset() {
        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.reset();
            playSong();
        }
    }

    public void registerClient(Activity activity) {
       this.activity = (Callbacks)activity;
    }

    // callbacks interface for communicating with activities
    public interface Callbacks {
        public void updateUI();
    }
}
