package com.example.maxvoskr.musicplayer;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Hashtable;

/*****
 * dev: Adi
 */

public class LoadingActivity extends AppCompatActivity {

    MusicArrayList musicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laoding);

        musicList = new MusicArrayList();

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        ArrayList<String> rawFileNames = new ArrayList<>();
        ArrayList<Integer> songCodes = new ArrayList<>();

        Field[] fields=R.raw.class.getFields();

        for(Field field : fields){
            try {
                rawFileNames.add(field.getName());
                songCodes.add(field.getInt(null));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        Hashtable<String, Integer> nameToCode = new Hashtable<String, Integer>();
        for(int count = 0; count < fields.length; count++){
            nameToCode.put(rawFileNames.get(count), songCodes.get(count));
        }

        for(String songName : rawFileNames) {
            String rawString = "R.raw." + songName;
            retriever.setDataSource(rawString);
            musicList.musicList.add(new Song(retriever.extractMetadata(7), retriever.extractMetadata(1),
                    retriever.extractMetadata(13), nameToCode.get(rawString)));
        }


        final Intent mainActivityIntent  = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
    }
}
