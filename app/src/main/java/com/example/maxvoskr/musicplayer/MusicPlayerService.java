package com.example.maxvoskr.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MusicPlayerService extends Service {

    public MusicPlayerService() {
    }

    private final IBinder binder = new MusicPlayerBinder();

    public class MusicPlayerBinder extends Binder {
        MusicPlayerService getMusicPlayerService() { return MusicPlayerService.this; }
    }

    @Override
    public IBinder onBind(Intent intent) { return binder; }

    @Override
    public void onCreate() {
        //Code for setting up the MusicPlayer object
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Will need to implement this if multiple activities will be binding to this service.
        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }


    @Override
    public void onDestroy() {

    }
}
