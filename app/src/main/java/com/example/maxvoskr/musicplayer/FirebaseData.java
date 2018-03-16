package com.example.maxvoskr.musicplayer;

import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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

                    SongFile song = new SongFile(name, album, artist, "");
                    song.setUrl(url);
                    MusicArrayList.insertFBSong(song);
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
}
