package com.example.maxvoskr.musicplayer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;

public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener {

    private Context context;
    private MediaPlayer mediaPlayer;
    private ArrayList<Song> songs;
    private int Player_Mode;
    int songIndex;

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
        context = getBaseContext();
        if(!songs.isEmpty()) {
            mediaPlayer = MediaPlayer.create(context, songs.get(songIndex).getSong());
            initMusicPlayer();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Will need to implement this if multiple activities will be binding to this service.
        return START_STICKY;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
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

    public void initMusicPlayer() {
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setOnPreparedListener(this);
    }

    public void setList(ArrayList<Song> playlist) {
        songs = playlist;
        songIndex = 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void playSong() {
        if(mediaPlayer != null && songIndex != songs.size() && !songs.isEmpty()) {
            mediaPlayer.reset();
            loadMedia(songs.get(songIndex).getSong());
            mediaPlayer.start();
        }
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void reset() {
        if(mediaPlayer != null && songIndex != songs.size() && !songs.isEmpty()) {
            mediaPlayer.reset();
            loadMedia(songs.get(songIndex).getSong());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void loadMedia(int resourceId) {
        if(mediaPlayer == null && !songs.isEmpty() && songIndex != songs.size()) {
            mediaPlayer = MediaPlayer.create(context, songs.get(songIndex).getSong());
            initMusicPlayer();
        }

        // TODO: Feel like this should go in onCreate but this is what the lab writeup does.
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.reset();
                if(++songIndex != songs.size() && !songs.isEmpty()) {
                    loadMedia(songs.get(songIndex).getSong());
                }
            }
        });

        AssetFileDescriptor assetFileDescriptor = this.getResources().openRawResourceFd(resourceId);
        try {
            mediaPlayer.setDataSource(assetFileDescriptor);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            System.out.println(e.toString());
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
    }
}
