package com.example.maxvoskr.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

/*****
 * dev: Adi
 */

public class LoadingActivity extends AppCompatActivity {

    public static CurrentLocationTimeData currentLocationTimeData;
    public static SharedPreferences lastActivitySharedPref;
    public static FriendsNames friendsNames = new FriendsNames();
    public static String userName = "";
    //public static VibePlaylistManager playlistManager;
    public static final String SONG_LIST_STRING = "Song_List";
    public static final String ALBUM_MODE_STRING = "Album_Mode";
    public static final String VIBE_MODE_STRING = "Vibe_Mode";
    private final int ALBUM_MODE = 1;
    private final int VIBE_MODE = 2;
    public static boolean userDefinedTime;
    private ArrayList<Song> songList;
    private MusicArrayList musicList;
    private Context context;
    private SongHistorySharedPreferenceManager sharedPref;
    private SongFactory songFactory;
    private FirebaseData firebaseObject = new FirebaseData();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        context = getApplicationContext();

        musicList = new MusicArrayList();
        songList = new ArrayList<Song>();
        sharedPref = new SongHistorySharedPreferenceManager(context);
        songFactory = new SongFactory(getResources());

        final String SOME_ACTION = "android.intent.action.DOWNLOAD_COMPLETE";
        IntentFilter intentFilter = new IntentFilter(SOME_ACTION);
        Downloader mReceiver = new Downloader(context, getResources());
        context.registerReceiver(mReceiver, intentFilter);
        //context.registerReceiver(mReceiver);

        final Intent signInActivity = new Intent(this, GoogleSignInActivity.class);
        final Intent songListIntent = new Intent(this, MainActivity.class);
        final Intent songPlayerIntent = new Intent(this, SongPlayerScreen.class);
        final Intent albumListIntent = new Intent(this, AlbumListActivity.class);

        currentLocationTimeData = new CurrentLocationTimeData(this);

        firebaseObject = new FirebaseData();
        firebaseObject.updateSongList();

        final LoadingActivity thisActivity = this;

        final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                for(Song song : MusicArrayList.allMusicTable.values())
                    Log.d("Loading", "HashMap after FB: " + song.getName() + " " + song.getSongID());

                for(Song song : MusicArrayList.localMusicList)
                    Log.d("Loading", "localList after FB: " + song.getName() + " " + song.getSongID());

                loadSongsFromMusicFolder();

                loadSongsFromResRaw();

                importSongsToApp();


                for(Song song : MusicArrayList.allMusicTable.values())
                    Log.d("Loading", "HashMap after Import: " + song.getName() + " " + song.getSongID());

                for(Song song : MusicArrayList.localMusicList)
                    Log.d("Loading", "localList after Import: " + song.getName() + " " + song.getSongID());

                //playlistManager = new VibePlaylistManager(MusicArrayList.allMusicList, new Downloader(context, getResources()));
            }
        }, 3000);


        lastActivitySharedPref = getSharedPreferences("LastActivity", Context.MODE_PRIVATE);
        final String activity = lastActivitySharedPref.getString("Activity_Name", "");

        switch(activity) {
            case SONG_LIST_STRING:
                startActivity(signInActivity);
                break;
            case ALBUM_MODE_STRING:
                startActivity(signInActivity);
                break;
            case VIBE_MODE_STRING:
                startActivity(signInActivity);
                break;
            default:
                startActivity(signInActivity);
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                Intent activityIntent;
                switch(activity) {
                    case SONG_LIST_STRING:
                        //startActivity(signInActivity);
                        songListIntent.putExtra("Position", -1);
                        startActivity(songListIntent);
                        break;
                    case ALBUM_MODE_STRING:
                        //startActivity(signInActivity);
                        startActivity(albumListIntent);
                        break;
                    case VIBE_MODE_STRING:
                        //startActivity(signInActivity);
                        songPlayerIntent.putExtra("playerMode", VIBE_MODE);
                        startActivity(songPlayerIntent);
                        break;
                    default:
                        startActivity(signInActivity);
                }

            }
        }, 5000);
    }

    private void loadSongsFromMusicFolder() {
        File musicDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File[] songFiles = musicDir.listFiles();

        for(File songFile : songFiles) {
            if(!songFile.getPath().contains(".zip")) {
                Song song = songFactory.makeSongFromPath(songFile.getPath());

                songList.add(song);

                sharedPref.updateData(song);
            }
        }
    }

    private void loadSongsFromResRaw() {
        ArrayList<Integer> songCodes = new ArrayList<>();
        Field[] fields=R.raw.class.getFields();
        for(Field field : fields){
            try {
                songCodes.add(field.getInt(field));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        for(Integer resID : songCodes) {
            Song song = songFactory.makeSongFromResID(resID);

            songList.add(song);

            sharedPref.updateData(song);
        }
    }

    private void importSongsToApp() {
        for(Song song : songList) {
            musicList.insertLocalSong(song);
        }
    }

    @Override
    protected void onDestroy() {
        currentLocationTimeData.unBindServices();

        for (Song song : musicList.localMusicList) {

            sharedPref.writeData(song);
        }

        super.onDestroy();
    }


}
