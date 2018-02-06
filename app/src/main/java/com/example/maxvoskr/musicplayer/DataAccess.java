package com.example.maxvoskr.musicplayer;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by evor on 2/4/2018.
 */

public class DataAccess {

    static final String LOCATION = "LOC";
    static final String TIME_MS = "T_MS";
    static final String DAY_OF_WEEK = "DOW";
    static final String TIME_OF_DAY = "TOD";
    static final String LIKE_DISLIKE = "LD";

    final SharedPreferences SongData;
    final SharedPreferences.Editor SongDataEditor;
    DataAccess(Context contextOfApplication)
    {
        SongData = contextOfApplication.getSharedPreferences("SongData", MODE_PRIVATE);
        SongDataEditor = SongData.edit();
    }

    void writeData(Song song)
    {
        SongDataEditor.putString(song.getNameOfSong()+LOCATION, song.getLocation());
        SongDataEditor.putString(song.getNameOfSong()+TIME_MS, Long.toString(song.getTimeMS()));
        SongDataEditor.putString(song.getNameOfSong()+DAY_OF_WEEK, Integer.toString(song.getDayOfWeek()));
        SongDataEditor.putString(song.getNameOfSong()+TIME_OF_DAY, Integer.toString(song.getTimeOfDay()));
        SongDataEditor.putString(song.getNameOfSong()+LIKE_DISLIKE, Integer.toString(song.getLikeDislike()));
        SongDataEditor.apply();
    }

    boolean updateData(Song songObj)
    {
        try{
            songObj.setLocation(SongData.getString(songObj.getNameOfSong()+LOCATION, null));
            songObj.setTimeMS(Long.parseLong(SongData.getString(songObj.getNameOfSong()+TIME_MS, null)));
            songObj.setDayOfWeek(Integer.parseInt(SongData.getString(songObj.getNameOfSong()+DAY_OF_WEEK, null)));
            songObj.setTimeOfDay(Integer.parseInt(SongData.getString(songObj.getNameOfSong()+TIME_OF_DAY, null)));
            songObj.setLikeDislike(Integer.parseInt(SongData.getString(songObj.getNameOfSong()+LIKE_DISLIKE, null)));
        }
        catch (Exception e){
            System.out.println("Unable to retrieve last play information");
            return false;
        }
        return true;
    }
}
