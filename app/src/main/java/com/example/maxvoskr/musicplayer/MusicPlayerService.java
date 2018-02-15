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

    private Context context;
    public LocationService locationService;
    public DateService dateService;
    private boolean locBound = false;
    private boolean dateBound = false;
    public static CurrentLocationTimeData currentLocationTimeData;
    Callbacks activity;
    private MediaPlayer mediaPlayer;
    private ArrayList<Song> songs;
    private FlashbackPlaylist flashbackPlaylist;
    private boolean fbMode;
    int songIndex;

    public MusicPlayerService() {}

    private ServiceConnection locConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            LocationService.LocationBinder locationBinder = (LocationService.LocationBinder) binder;
            locationService = locationBinder.getLocationService();
            locBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            locBound = false;
        }
    };
    private ServiceConnection dateConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            DateService.DateBinder dateBinder = (DateService.DateBinder) iBinder;
            dateService = dateBinder.getDateService();
            dateBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) { dateBound = false; }
    };

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
        flashbackPlaylist = new FlashbackPlaylist(MusicArrayList.musicList);
        Intent locIntent = new Intent(this, LocationService.class);
        bindService(locIntent, locConnection, Context.BIND_AUTO_CREATE);
        Intent dateIntent = new Intent(this, DateService.class);
        bindService(dateIntent, dateConnection, Context.BIND_AUTO_CREATE);
        currentLocationTimeData = new CurrentLocationTimeData(dateService, locationService);
        //if(!songs.isEmpty()) {
        //    mediaPlayer = MediaPlayer.create(context, songs.get(songIndex).getSong());
        //}
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Will need to implement this if multiple activities will be binding to this service.
        return START_STICKY;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        initMusicPlayer();
        mp.start();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locBound) {
            unbindService(locConnection);
            locBound = false;
        }
        if(dateBound) {
            unbindService(dateConnection);
            dateBound = false;
        }
        mediaPlayer.release();
    }

    private void initMusicPlayer() {
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setOnPreparedListener(this);
    }

    /* Set playlist for MusicPlayer to play through */
    public void setList(ArrayList<Song> playlist) {
        songs = playlist;
        songIndex = 0;
    }

    /* Play current song */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void playSong() {
        if(songs != null && mediaPlayer != null && songIndex != songs.size() && !songs.isEmpty()) {
            mediaPlayer.reset();
            loadMedia(songs.get(songIndex++).getSong());
            mediaPlayer.start();
        }
    }

    /* Pause the song */
    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void setFlashbackMode(boolean on) {
        fbMode = on;
    }

    /* Restart song */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void reset() {
        if(mediaPlayer != null && songIndex != songs.size() && !songs.isEmpty()) {
            mediaPlayer.reset();
            loadMedia(songs.get(songIndex).getSong());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadMedia(int resourceId) {
        if(mediaPlayer == null && !songs.isEmpty() && songIndex != songs.size()) {
            mediaPlayer = MediaPlayer.create(context, songs.get(songIndex).getSong());
            initMusicPlayer();
        }

        // TODO: Feel like this should go in onCreate but this is what the lab writeup does.
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(!fbMode) {
                    mediaPlayer.reset();
                    if (++songIndex != songs.size() && !songs.isEmpty()) {
                        loadMedia(songs.get(songIndex).getSong());
                    }
                } else {
                    loadMedia(flashbackPlaylist.getNextSong().getSong());
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

    public void registerClient(Activity activity) {
       this.activity = (Callbacks)activity;
    }

    // callbacks interface for communicating with activities
    public interface Callbacks {
        public void updateUI();
    }
}
