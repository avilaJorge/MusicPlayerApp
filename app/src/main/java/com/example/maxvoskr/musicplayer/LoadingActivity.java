package com.example.maxvoskr.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

/*****
 * dev: Adi
 */

public class LoadingActivity extends AppCompatActivity {

    public static CurrentLocationTimeData currentLocationTimeData;
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


        firebaseObject = new FirebaseData();

        firebaseObject.updateSongList();

        loadSongsFromMusicFolder();

        loadSongsFromResRaw();

        importSongsToApp();

<<<<<<< HEAD

        // TODO: remove this, only for testing until FB is up
        SongFile song = new SongFile("Back On The Road Again", "Music for TV and Film Vol. 1", "Scott Holmes", "");
        song.setUrl("https://freemusicarchive.org/music/download/a8ea4c3229d571ec76ef3a6eb867b840db7b1b17");
        MusicArrayList.insertFBSong(song);


        SharedPreferences sharedPref = getSharedPreferences("MyPages", Context.MODE_PRIVATE);
        String page = sharedPref.getString("LastPage", "DEFAULT");

        Class toRun = GoogleSignInActivity.class;
        try{
            toRun = Class.forName(page);
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        final Intent ActivityPage = new Intent(this, toRun);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms

                startActivity(ActivityPage);
            }
        }, 5000);


        currentLocationTimeData = new CurrentLocationTimeData(this);

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

<<<<<<< HEAD
        for (Song song : musicList.musicList) {
=======
        for (Song song : musicList.localMusicList) {

>>>>>>> origin/dev_branch
            sharedPref.writeData(song);
        }

        super.onDestroy();
    }


}
