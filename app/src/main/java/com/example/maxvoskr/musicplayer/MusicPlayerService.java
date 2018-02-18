package com.example.maxvoskr.musicplayer;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.maxvoskr.musicplayer.LoadingActivity.currentLocationTimeData;

public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener {

    private final int SONG_MODE = 0;
    private final int ALBUM_MODE = 1;
    private final int FLASHBACK_MODE = 2;
    private int mode = SONG_MODE;

    private Context context;
    Callbacks activity;

    int songIndex;
    private boolean paused = false;
    private boolean playerReleased = true;
    private MediaPlayer mediaPlayer;
    private ArrayList<Song> songs;
    private FlashbackPlaylist flashbackPlaylist;

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
        mediaPlayer.release();
        playerReleased = true;
        Log.d("ClearFromRecentService", "Service Destroyed");
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearFromRecentService", "END");
        mediaPlayer.release();
        playerReleased = true;
        stopSelf();
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


        Toast.makeText(context, "In playSong", Toast.LENGTH_SHORT).show();
        if (!playerReleased && paused) {
            paused = false;
            mediaPlayer.start();
            return;
        }
        if (mediaPlayer != null && !playerReleased) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            playerReleased = true;
        }

        if (mode == FLASHBACK_MODE && songs.isEmpty()) {
            Toast.makeText(context, "Starting flashback mode over", Toast.LENGTH_SHORT).show();
            flashbackPlaylist = new FlashbackPlaylist(MusicArrayList.musicList);
            flashbackPlaylist.setCurrentWeights(LoadingActivity.currentLocationTimeData);
            Song next = flashbackPlaylist.getNextSong();
            if(next != null) {
                songs.add(next);
            }
            songIndex = 0;
        }

        if (songs != null && songIndex < songs.size()) {
            mediaPlayer = MediaPlayer.create(context, songs.get(songIndex).getSong());
            playerReleased = false;
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {

                    currentLocationTimeData.updateSongUsingTemp(songs.get(songIndex));
                    if (mode == ALBUM_MODE) {
                        if (++songIndex <= songs.size() && !songs.isEmpty()) {
                            activity.updateUI(getCurrentSong());
                            playSong();
                        }
                    } else if (mode == FLASHBACK_MODE) {
                        flashbackPlaylist.setCurrentWeights(LoadingActivity.currentLocationTimeData);
                        Song next = flashbackPlaylist.getNextSong();
                        if(next != null) {
                            songs.set(0, next);
                            songIndex = 0;
                            activity.updateUI(getCurrentSong());
                            playSong();
                        }
                    } else {
                        mediaPlayer.release();
                        playerReleased = true;
                    }

                    Toast.makeText(context, "In onCompletion", Toast.LENGTH_SHORT).show();
                }
            });
            // TODO: Erase this before demo.
            double time = mediaPlayer.getDuration() * 0.9;
            mediaPlayer.seekTo((int) time);
        }
    }

    /* Pause the song */
    public void pause() {
        if(!playerReleased && mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                paused = true;
                mediaPlayer.pause();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void skip() {
        if(!playerReleased && mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.reset();
                if(mode == ALBUM_MODE) {
                    if (++songIndex <= songs.size() && !songs.isEmpty() &&
                            songs.get(songIndex).getLikeDislike() != -1) {
                        activity.updateUI(getCurrentSong());
                        playSong();
                    }
                    else
                    {
                        stop();
                    }
                } else if(mode == FLASHBACK_MODE) {
                    flashbackPlaylist.setCurrentWeights(LoadingActivity.currentLocationTimeData);
                    Song next = flashbackPlaylist.getNextSong();
                    if(next != null) {
                        songs.set(0, next);
                        songIndex = 0;
                        activity.updateUI(getCurrentSong());
                        playSong();
                    }
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void previous() {
        if(!playerReleased && mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.reset();
                if(mode == ALBUM_MODE) {
                    if (--songIndex >= 0 && !songs.isEmpty() &&
                                songs.get(songIndex).getLikeDislike() != -1) {
                        activity.updateUI(getCurrentSong());
                        playSong();

                    }
                    else
                    {
                        stop();
                    }
                }
            }
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

    public Song getCurrentSong()
    {
        if(songs != null && !songs.isEmpty())
            return songs.get(songIndex);
        else
            return null;
    }

    /* Restart song */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void reset() {
        if(!playerReleased && mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.reset();
            playSong();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void stop() {
        if(!playerReleased && mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.reset();
        }
    }


    public void registerClient(Activity activity) {
       this.activity = (Callbacks)activity;
    }

    // callbacks interface for communicating with activities
    public interface Callbacks {
        public void updateUI(Song nextSong);
    }
}
