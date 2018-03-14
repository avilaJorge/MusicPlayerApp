/**
 * Created by mdavi on 2/4/2018.
 */

package com.example.maxvoskr.musicplayer;

public class SongRes extends Song {
    private int song;


    SongRes(String name, String album, String artist, int song){
        super();
        this.nameOfSong = name;
        this.nameOfAlbum = album;
        this.nameOfArtist = artist;
        this.song = song;
    }

    SongRes(String location, long timeMS, int dayOfWeek, int timeOfDay,
            int likeDislike, String name, String album, String artist, int song){
        super();
        this.location = location;
        this.timeMS = timeMS;
        this.dayOfWeek = dayOfWeek;
        this.timeOfDay = timeOfDay;
        this.likeDislike = likeDislike;
        this.nameOfSong = name;
        this.nameOfAlbum = album;
        this.nameOfArtist = artist;
        this.song = song;
    }

    SongRes(String location, long timeMS, int dayOfWeek, int timeOfDay,
            int likeDislike, String name, String album, int song){
        super();
        this.location = location;
        this.timeMS = timeMS;
        this.dayOfWeek = dayOfWeek;
        this.timeOfDay = timeOfDay;
        this.likeDislike = likeDislike;
        this.nameOfSong = name;
        this.nameOfAlbum = album;
        this.song = song;
    }

    //reference to song
  
    public int getSong() {
        return song;
    }

    public void setSong(int song) {
        this.song = song;
    }


}
