package com.example.maxvoskr.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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


        loadSongsFromMusicFolder();

        loadSongsFromResRaw();

        importSongsToApp();


        // TODO: remove this, only for testing until FB is up
        MusicArrayList.insertFBSong(new SongRes("song1", "album", "artist", 0));



        final Intent mainActivityIntent  = new Intent(this, GoogleSignInActivity.class);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms

                startActivity(mainActivityIntent);
            }
        }, 5000);


        currentLocationTimeData = new CurrentLocationTimeData(this);

        // Is there an issue here? we start the CurrentLocationTimeData after we would have switched to MainActivity

        final Intent mainActivityIntent2  = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent2);
    }



    private void loadSongsFromMusicFolder() {
        File musicDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File[] songFiles = musicDir.listFiles();

        for(File songFile : songFiles) {
            Song song = songFactory.makeSongFromPath(songFile.getPath());

            songList.add(song);

            sharedPref.updateData(song);
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

        for (Song song : musicList.musicList) {

            sharedPref.writeData(song);
        }

        super.onDestroy();
    }


}
