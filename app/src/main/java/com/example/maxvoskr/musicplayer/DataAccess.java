package com.example.maxvoskr.musicplayer;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by evor on 2/4/2018.
 */

public class DataAccess {

    final SharedPreferences SongData;
    final SharedPreferences.Editor SongDataEditor;
    DataAccess(Context contextOfApplication)
    {
        SongData = contextOfApplication.getSharedPreferences("SongData", MODE_PRIVATE);
        SongDataEditor = contextOfApplication.getSharedPreferences("SongData", MODE_PRIVATE).edit();
    }

    void writeData(Song song)
    {
        Set<String> data = new LinkedHashSet();
        data.add(song.getLocation());
        data.add(Long.toString(song.getTimeMS()));
        data.add(Integer.toString(song.getDayOfWeek()));
        data.add(Integer.toString(song.getTimeOfDay()));
        data.add(Integer.toString(song.getLikeDislike()));

        SongDataEditor.putStringSet(song.getNameOfSong(), data);
        SongDataEditor.apply();
    }

    boolean updateData(Song songObj)
    {
        Set<String> data = SongData.getStringSet(songObj.getNameOfSong(), null);
        String[] songData = data.toArray(new String[data.size()]);
        try{
            songObj.setLocation(songData[0]);
            //songObj.setTimeMS(Long.parseLong(songData[1]));
            //songObj.setDayOfWeek(Integer.parseInt(songData[2]));
            //songObj.setTimeOfDay(Integer.parseInt(songData[3]));
            //songObj.setLikeDislike(Integer.parseInt(songData[4]));
        }
        catch (Exception e){
            System.out.println("Unable to retrieve last play information");
            return false;
        }
        return true;
    }
}
