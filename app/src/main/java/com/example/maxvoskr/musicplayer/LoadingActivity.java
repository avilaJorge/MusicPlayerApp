package com.example.maxvoskr.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/*****
 * dev: Adi
 */

public class LoadingActivity extends AppCompatActivity {

    public static CurrentLocationTimeData currentLocationTimeData;
    private Set<String> albumNamesSet = new HashSet<String>();
    MusicArrayList musicList;
    Context context;
    SongHistorySharedPreferenceManager sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        context = getApplicationContext();

        System.out.println("Hi Max");
        musicList = new MusicArrayList();
        sharedPref = new SongHistorySharedPreferenceManager(context);

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        ArrayList<String> rawFileNames = new ArrayList<>();
        ArrayList<Integer> songCodes = new ArrayList<>();

        Field[] fields=R.raw.class.getFields();

        for(Field field : fields){
            try {
                rawFileNames.add(field.getName());
                songCodes.add(field.getInt(field));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        Hashtable<String, Integer> nameToCode = new Hashtable<String, Integer>();
        for(int count = 0; count < fields.length; count++){
            nameToCode.put(rawFileNames.get(count), songCodes.get(count));
        }

        int i = 0;
        for(String songName : rawFileNames) {

            Resources res = getResources();
            AssetFileDescriptor afd = res.openRawResourceFd(songCodes.get(i++));
            retriever.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());

            Song song = new Song(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE), retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM),
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST), nameToCode.get(songName));

            this.albumNamesSet.add(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
            //musicList.albumSet.add(new Album(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM),
            //        retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST)));
            sharedPref.updateData(song);
            musicList.musicList.add(song);
        }

        for (String albumName : albumNamesSet) {
            System.out.println(albumName);
            musicList.albumList.add(new Album(albumName, "Max"));
        }


        for (Album album : musicList.albumList) {

            for (Song song : musicList.musicList) {

                if (song.getAlbum().equals(album.getAlbumName())) {
                    album.setArtist(song.getArtist());
                    album.addSong(song);
                }
            }
        }

        final Intent mainActivityIntent  = new Intent(this, MainActivity.class);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms

                startActivity(mainActivityIntent);
            }
        }, 5000);


        currentLocationTimeData = new CurrentLocationTimeData(this);
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
