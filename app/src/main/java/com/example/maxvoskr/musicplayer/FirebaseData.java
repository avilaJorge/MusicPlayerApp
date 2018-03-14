package com.example.maxvoskr.musicplayer;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by maxvoskr on 3/13/18.
 */

public class FirebaseData {

    //private DatabaseReference mDatabase;
    //private DatabaseReference myRef;

    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;

    static final String LOCATION = "LOC";
    static final String TIME_MS = "T_MS";
    static final String DAY_OF_WEEK = "DOW";
    static final String TIME_OF_DAY = "TOD";
    static final String LIKE_DISLIKE = "LD";

    FirebaseData() {
        FirebaseOptions options = new FirebaseOptions.Builder().
                setApplicationId("1:630586042148:android:c745a97ea2457fdc").
                setDatabaseUrl("https://musicplayer-c8dfe.firebaseio.com/").build();

        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReferenceFromUrl("https://musicplayer-c8dfe.firebaseio.com/");

        //mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    void writeNewSong(Song song) {
        System.out.println("--------------------firebase Write-------------------");

        String song_id = myRef.push().getKey();
        myRef.setValue("hello");

        if (song == null){
            throw new NullPointerException("Cannot write an uninitialized song");
        }

        if (song.getLocation() != null) {
            myRef.child("songs").child(song_id).child("locations").child(song.getLocation()).setValue(true);
        }


       // mDatabase.child("songs").child(song_id).child("time").setValue(song.getTimeMS());
        myRef.child("songs").child(song_id).child("name").setValue(song.getName());
        myRef.child("songs").child(song_id).child("album").setValue(song.getAlbum());
        myRef.child("songs").child(song_id).child("artist").setValue(song.getArtist());

    }

    /*boolean updateData(Song songObj)
    {
        if(songObj == null){
            throw new NullPointerException("Cannot update a uninitialized song");
        }
        try{
            songObj.setLocation(SongData.getString(songObj.getName()+LOCATION, songObj.getLocation()));
            songObj.setTimeMS(Long.parseLong(SongData.getString(songObj.getName()+TIME_MS, Long.toString(songObj.getTimeMS()))));
            songObj.setDayOfWeek(Integer.parseInt(SongData.getString(songObj.getName()+DAY_OF_WEEK, Integer.toString(songObj.getDayOfWeek()))));
            songObj.setTimeOfDay(Integer.parseInt(SongData.getString(songObj.getName()+TIME_OF_DAY, Integer.toString(songObj.getTimeOfDay()))));
            songObj.setLikeDislike(Integer.parseInt(SongData.getString(songObj.getName()+LIKE_DISLIKE, Integer.toString(songObj.getLikeDislike()))));
        }
        catch (Exception e){
            System.out.println("Unable to retrieve last play information");
            return false;
        }
        return true;
    }*/
}
