package com.example.maxvoskr.musicplayer;

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

    SongFile(String location, long timeMS, int dayOfWeek, int timeOfDay,
            int likeDislike, String name, String album, String artist, String song){
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

    SongFile(String location, long timeMS, int dayOfWeek, int timeOfDay,
            int likeDislike, String name, String album, String song){
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

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
