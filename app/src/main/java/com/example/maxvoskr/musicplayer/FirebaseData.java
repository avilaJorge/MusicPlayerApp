package com.example.maxvoskr.musicplayer;

import android.util.Log;

import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

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
    }

    void updateSongList() {
        Query q = myRef.child("songs");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot data : children) {
                    String name = data.child("name").getValue(String.class);
                    String album = data.child("album").getValue(String.class);
                    String artist = data.child("artist").getValue(String.class);
                    String url = data.child("url").getValue(String.class);
                    String songId = data.toString();
                    songId = songId.substring(songId.indexOf("key = "), songId.indexOf(","));
                    songId = songId.substring(6);

                    Log.d("FireBase Loading Songs", "SongId was: " + songId);

                    SongFile song = new SongFile(name, album, artist, "");
                    song.setUrl(url);
                    song.setSongID(songId);
                    MusicArrayList.insertFBSong(song);
                    getLastPlayed(song);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    void writeNewSong(Song song) {

        String song_id = myRef.push().getKey();

        if (song == null){
            throw new NullPointerException("Cannot write an uninitialized song");
        }

        if (song.getLocations() != null) {
            //TODO: Check
            for(String location : song.getLocations())
                myRef.child("songs").child(song_id).child("locations").child(location).setValue(true);
        }

       // mDatabase.child("songs").child(song_id).child("time").setValue(song.getTimeMS());
        myRef.child("songs").child(song_id).child("name").setValue(song.getName());
        myRef.child("songs").child(song_id).child("album").setValue(song.getAlbum());
        myRef.child("songs").child(song_id).child("artist").setValue(song.getArtist());
        myRef.child("songs").child(song_id).child("url").setValue(song.getUrl());

    }

    void addUser(FirebaseUser user) {
        myRef.child("users").child(user.getUid()).child("email").setValue(user.getEmail());
    }

    void addUserSong(Song songObj) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        myRef.child("users").child(user.getUid()).child("lastSong").setValue(songObj.getSongID());
    }

    void updateSongData(Song songObj)
    {
        if(songObj == null){
            throw new NullPointerException("Cannot update a uninitialized song");
        }
        try{
            myRef.child("songs").child(songObj.getSongID()).child("name").setValue(songObj.getName());
            myRef.child("songs").child(songObj.getSongID()).child("album").setValue(songObj.getAlbum());
            myRef.child("songs").child(songObj.getSongID()).child("artist").setValue(songObj.getArtist());
            myRef.child("songs").child(songObj.getSongID()).child("time").setValue(songObj.getTimeMS());

            //TODO: Check
            for(String location : songObj.getLocations())
                myRef.child("songs").child(songObj.getSongID()).child("locations").child(location).setValue(true);

        }
        catch (Exception e){
            System.out.println("Unable to retrieve last play information");
        }
    }

    public void updateLastPlayed(Song songObj) {
        if(songObj == null){
            return;
        }
        try {
            myRef.child("songs").child(songObj.getSongID()).child("LastPlayed").child("Time").setValue(songObj.getTimeMS());
            myRef.child("songs").child(songObj.getSongID()).child("LastPlayed").child("Location").setValue(songObj.getLastLocation());
            myRef.child("songs").child(songObj.getSongID()).child("LastPlayed").child("User").setValue("Insert Name Here");

            //TODO: Check
        //    for(String location : songObj.getLocations())
            //       myRef.child("songs").child(songObj.getSongID()).child("locations").child(location).setValue(true);
        }
        catch (Exception e){
            System.out.println("Unable to retrieve last play information");
        }
    }


    public void getLastPlayed(Song song) {
        if(song == null)
            return;
        else {
            Query q = myRef.child("songs");
            q.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("getLastPlayed", "start");
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for(DataSnapshot data : children) {
                        String name = data.child("name").getValue(String.class);
                        String album = data.child("album").getValue(String.class);
                        String artist = data.child("artist").getValue(String.class);
                        //TODO: Username (2 pts)

                        Song localSong = null;

                        for(Song song : MusicArrayList.localMusicList) {
                            Log.d("getLastPlayed", "Song: " + song.getName() + " " + song.getAlbum() + " " + song.getArtist());
                            Log.d("getLastPlayed", "song: " + name + " " + album + " " + artist);
                            if(song.getName().equals(name) && song.getAlbum().equals(album) && song.getArtist().equals(artist)) {
                                Log.d("getLastPlayed", "found!");
                                localSong = song;
                            }
                        }

                        Log.d("getLastPlayed", "null check");
                        if(localSong != null) {

                            Iterable<DataSnapshot> childrenLastPlayed = dataSnapshot.child(localSong.getSongID()).getChildren();
                            for(DataSnapshot lastData : childrenLastPlayed) {
                                //String allData = lastData.toString();

                                if (lastData.child("LastPlayed").exists()) {
                                    Long timeMS = lastData.child("LastPlayed").child("Time").getValue(Long.class);
                                    localSong.setTimeMS(timeMS);
                                    localSong.setLastLocation(data.child("LastPlayed").child("Location").getValue(String.class));

                                    Date songDate = new Date(localSong.getTimeMS());
                                    String minutes = Integer.toString(songDate.getMinutes());
                                    if (songDate.getMinutes() < 10)
                                        minutes = "0" + songDate.getMinutes();

                                    String AM_PM = "am";
                                    int hour = songDate.getHours();
                                    if (hour > 12) {
                                        hour -= 12;
                                        AM_PM = "pm";
                                    }

                                    Log.d("getLastPlayed", "Song: " + localSong.getName() + " was updated to time: " + (hour + ":" + minutes + " " + AM_PM));
                                }
                            }
                        }
                        Log.d("getLastPlayed", "end_innner_loop");
                    }
                    Log.d("getLastPlayed", "end");
                }



                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}

