package com.example.maxvoskr.musicplayer;

import java.util.ArrayList;

/**
 * Created by mdavi on 3/8/2018.
 */

public class SongFile extends Song {
    private String song;
    private String url;


    SongFile(String name, String album, String artist, String song){
        super();
        this.nameOfSong = name;
        this.nameOfAlbum = album;
        this.nameOfArtist = artist;
        this.song = song;
    }

    SongFile(String lLocation, long timeMS, int dayOfWeek, int timeOfDay,
            int likeDislike, String name, String album, String artist, String song){
        super();
        this.lastLocation = lLocation;
        this.timeMS = timeMS;
        this.dayOfWeek = dayOfWeek;
        this.timeOfDay = timeOfDay;
        this.likeDislike = likeDislike;
        this.nameOfSong = name;
        this.nameOfAlbum = album;
        this.nameOfArtist = artist;
        this.song = song;
    }

    SongFile(String lLocation, long timeMS, int dayOfWeek, int timeOfDay,
            int likeDislike, String name, String album, String song){
        super();
        this.lastLocation = lLocation;
        this.timeMS = timeMS;
        this.dayOfWeek = dayOfWeek;
        this.timeOfDay = timeOfDay;
        this.likeDislike = likeDislike;
        this.nameOfSong = name;
        this.nameOfAlbum = album;
        this.song = song;
    }

    SongFile(ArrayList<String> locations, String lLocation, long timeMS, int dayOfWeek, int timeOfDay,
             int likeDislike, String name, String album, String song){
        super();
        this.lastLocation = lLocation;
        this.timeMS = timeMS;
        this.dayOfWeek = dayOfWeek;
        this.timeOfDay = timeOfDay;
        this.likeDislike = likeDislike;
        this.nameOfSong = name;
        this.nameOfAlbum = album;
        this.song = song;
        this.locations = locations;
    }

    //reference to song

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getUrl() {
        if(url != null)
            return url;
        else
            return "";
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
