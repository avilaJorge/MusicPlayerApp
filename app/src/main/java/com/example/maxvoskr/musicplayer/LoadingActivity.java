package com.example.maxvoskr.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/*****
 * dev: Adi
 */

public class LoadingActivity extends AppCompatActivity {

    public static CurrentLocationTimeData currentLocationTimeData;
    private Set<String> albumNamesSet = new HashSet<String>();
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
        sharedPref = new SongHistorySharedPreferenceManager(context);
        songFactory = new SongFactory(getResources());

        final String SOME_ACTION = "android.intent.action.DOWNLOAD_COMPLETE";
        IntentFilter intentFilter = new IntentFilter(SOME_ACTION);
        Downloader mReceiver = new Downloader(context, getResources());
        context.registerReceiver(mReceiver, intentFilter);
        //context.registerReceiver(mReceiver);


        loadSongsFromMusicFolder();

        loadSongsFromResRaw();

        prepareAlbumList();


        // Is there an issue here? we start the CurrentLocationTimeData after we would have switched to MainActivity

        final Intent mainActivityIntent  = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);

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



    private void loadSongsFromMusicFolder() {
        File musicDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File[] songFiles = musicDir.listFiles();

        for(File songFile : songFiles) {
            Song song = songFactory.makeSongFromPath(songFile.getPath());

            musicList.musicList.add(song);
            albumNamesSet.add(song.getAlbum());

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

            musicList.musicList.add(song);
            albumNamesSet.add(song.getAlbum());

            sharedPref.updateData(song);
        }


        /* old method code
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

            Song song = new SongRes(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE), retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM),
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST), nameToCode.get(songName));

            this.albumNamesSet.add(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
            sharedPref.updateData(song);
            musicList.musicList.add(song);
        } */
    }

    private void prepareAlbumList() {
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
    }


}
