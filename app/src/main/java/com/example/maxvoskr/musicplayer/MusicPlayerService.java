package com.example.maxvoskr.musicplayer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
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
    private final int VIBE_MODE = 2;
    private int mode = SONG_MODE;

    private Context context;
    Callbacks activity;

    int songIndex;
    private boolean paused = false;
    private boolean playerReleased = true;
    private MediaPlayer mediaPlayer;
    private ArrayList<Song> songs;
    private VibePlaylistManager vibeModePlaylist;

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
        //TODO: line below crashes after closing app
        //vibeModePlaylist = new VibeModePlaylist(MusicArrayList.localMusicList);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Will need to implement this if multiple activities will be binding to this service.
        return START_STICKY;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d("log", "In On Prepared");
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
        if(mediaPlayer != null) {
            //TODO: crash here
            mediaPlayer.release();
            playerReleased = true;
            stopSelf();
        }
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


        Log.d("log", "Service now connected");
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

        if (mode == VIBE_MODE && songs.isEmpty()) {
            Toast.makeText(context, "Starting vibe mode over", Toast.LENGTH_SHORT).show();
            vibeModePlaylist = VibePlaylistHolder.playlistManager;
            vibeModePlaylist.setCurrentWeights(LoadingActivity.currentLocationTimeData);
            Toast.makeText(context,"Getting Next Song",Toast.LENGTH_SHORT).show();
            Song next = vibeModePlaylist.getNextSong(NetworkConnect.haveNetworkConnection(context));
            if(next != null) {
                songs.add(next);
            }
            else{
                Toast.makeText(context,"Getting Next Song 140",Toast.LENGTH_SHORT).show();
            }
            songIndex = 0;
        }

        if (songs != null && songIndex < songs.size()) {
            if (songs.get(songIndex).getClass() == SongRes.class)
                mediaPlayer = MediaPlayer.create(context, ((SongRes)(songs.get(songIndex))).getSong());
            else
                mediaPlayer = MediaPlayer.create(context, Uri.parse(((SongFile)(songs.get(songIndex))).getSong()));

            playerReleased = false;
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    Toast.makeText(context, "On completion", Toast.LENGTH_SHORT).show();
                    currentLocationTimeData.updateSongUsingTemp(songs.get(songIndex));
                    FirebaseData firebaseData = new FirebaseData();
                    firebaseData.updateLastPlayed(songs.get(songIndex));
                    songs.get(songIndex).setPlayed();
                    Toast.makeText(context, "Song ID: " + songs.get(songIndex).getSongID(), Toast.LENGTH_SHORT).show();



                    if (mode == ALBUM_MODE) {
                        if (++songIndex <= songs.size() && !songs.isEmpty()) {
                            activity.updateUI(getCurrentSong());
                            playSong();
                        }
                    } else if (mode == VIBE_MODE) {
                        vibeModePlaylist.setCurrentWeights(LoadingActivity.currentLocationTimeData);
                        Song next = vibeModePlaylist.getNextSong(NetworkConnect.haveNetworkConnection(context));
                        if(next != null) {
                            songs.set(0, next);
                            songIndex = 0;
                            activity.updateUI(getCurrentSong());
                            playSong();
                        }
                        else{
                            Toast.makeText(context,"Getting Next Song 177",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mediaPlayer.release();
                        playerReleased = true;
                    }

                    Log.d("log", "in on completion");
                }
            });
            // TODO: Erase this before demo.
            double time = mediaPlayer.getDuration() * 0.95;
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

    @TargetApi(24)
    public void skip() {
        if(!playerReleased && mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.reset();
                if(mode == ALBUM_MODE) {
                    if (++songIndex < songs.size() && !songs.isEmpty() &&
                            songs.get(songIndex).getLikeDislike() != -1) {
                        activity.updateUI(getCurrentSong());
                        playSong();
                    }
                    else
                    {
                        stop();
                    }
                } else if(mode == VIBE_MODE) {
                    vibeModePlaylist.setCurrentWeights(LoadingActivity.currentLocationTimeData);
                    Song next = vibeModePlaylist.getNextSong(NetworkConnect.haveNetworkConnection(context));
                    if(next != null) {
                        songs.set(0, next);
                        songIndex = 0;
                        activity.updateUI(getCurrentSong());
                        playSong();
                    }
                    else{
                        Toast.makeText(context,"Getting Next Song 228",Toast.LENGTH_SHORT).show();
                    }
                }
                else if (mode == SONG_MODE)
                {
                    stop();
                }
                else
                {
                    stop();
                }
            }
        }
    }

    @TargetApi(24)
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
           private final int VIBE_MODE = 2;
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
    @TargetApi(24)
    public void reset() {
        if(!playerReleased && mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.reset();
            playSong();
        }
    }

    @TargetApi(24)
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
