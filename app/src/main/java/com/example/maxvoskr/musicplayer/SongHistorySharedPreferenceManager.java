package com.example.maxvoskr.musicplayer;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by evor on 2/4/2018.
 */

public class SongHistorySharedPreferenceManager {

    static final String LOCATION = "LOC";
    static final String TIME_MS = "T_MS";
    static final String DAY_OF_WEEK = "DOW";
    static final String TIME_OF_DAY = "TOD";
    static final String LIKE_DISLIKE = "LD";

    final SharedPreferences SongData;
    final SharedPreferences.Editor SongDataEditor;
    SongHistorySharedPreferenceManager(Context contextOfApplication)
    {
        SongData = contextOfApplication.getSharedPreferences("SongData", MODE_PRIVATE);
        SongDataEditor = SongData.edit();
    }

    void writeData(Song song) {
        if (song == null){
            throw new NullPointerException("Cannot write an uninitialized song");
        }
        //SongDataEditor.putString(song.getName()+LOCATION, song.getLastLocation());
        //SongDataEditor.putString(song.getName()+TIME_MS, Long.toString(song.getTimeMS()));
        //SongDataEditor.putString(song.getName()+DAY_OF_WEEK, Integer.toString(song.getDayOfWeek()));
        //SongDataEditor.putString(song.getName()+TIME_OF_DAY, Integer.toString(song.getTimeOfDay()));
        SongDataEditor.putString(song.getName()+LIKE_DISLIKE, Integer.toString(song.getLikeDislike()));
        SongDataEditor.apply();
    }

    boolean updateData(Song songObj)
    {
        if(songObj == null){
            throw new NullPointerException("Cannot update a uninitialized song");
        }
        try{
          //  songObj.setLastLocation(SongData.getString(songObj.getName()+LOCATION, songObj.getLastLocation()));
          //  songObj.setTimeMS(Long.parseLong(SongData.getString(songObj.getName()+TIME_MS, Long.toString(songObj.getTimeMS()))));
           // songObj.setDayOfWeek(Integer.parseInt(SongData.getString(songObj.getName()+DAY_OF_WEEK, Integer.toString(songObj.getDayOfWeek()))));
            //songObj.setTimeOfDay(Integer.parseInt(SongData.getString(songObj.getName()+TIME_OF_DAY, Integer.toString(songObj.getTimeOfDay()))));
            songObj.setLikeDislike(Integer.parseInt(SongData.getString(songObj.getName()+LIKE_DISLIKE, Integer.toString(songObj.getLikeDislike()))));
        }
        catch (Exception e){
            System.out.println("Unable to retrieve last play information");
            return false;
        }
        return true;
    }
}
