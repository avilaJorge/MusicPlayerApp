/**
 * Created by mdavi on 2/4/2018.
 */

package com.example.maxvoskr.musicplayer;

public class Song {
    private String location;
    private long timeMS;
    private int dayOfWeek;
    private int timeOfDay;
    private int likeDislike;
    private String nameOfSong;
    private String nameOfAlbum;
    private String nameOfArtist;
    private int reference;


    Song(String name, String album, String artist, int reference){
        this.nameOfSong = name;
        this.nameOfAlbum = album;
        this.nameOfArtist = artist;
        this.reference = reference;
    }

    Song(String location, long timeMS, int dayOfWeek, int timeOfDay,
        int likeDislike, String name, String album, String artist, int reference){
        this.location = location;
        this.timeMS = timeMS;
        this.dayOfWeek = dayOfWeek;
        this.timeOfDay = timeOfDay;
        this.likeDislike = likeDislike;
        this.nameOfSong = name;
        this.nameOfAlbum = album;
        this.nameOfArtist = artist;
        this.reference = reference;
        }

    Song(String location, long timeMS, int dayOfWeek, int timeOfDay,
         int likeDislike, String name, String album, int reference){
        this.location = location;
        this.timeMS = timeMS;
        this.dayOfWeek = dayOfWeek;
        this.timeOfDay = timeOfDay;
        this.likeDislike = likeDislike;
        this.nameOfSong = name;
        this.nameOfAlbum = album;
        this.reference = reference;
    }

    public String getLocation(){
        return location;
    }
    public long getTimeMS(){
        return timeMS;
    }
    public int getDayOfWeek(){
        return dayOfWeek;
    }

    public int getTimeOfDay() {
        return timeOfDay;
    }

    public int getLikeDislike() {
        return likeDislike;
    }

    public String getNameOfSong() {
        return nameOfSong;
    }

    public String getNameOfAlbum() {
        return nameOfAlbum;
    }

    public String getNameOfArtist() {
        return nameOfArtist;
    }

    public int getReference() {
        return reference;
    }

    public void setNameOfArtist(String nameOfArtist) {
        this.nameOfArtist = nameOfArtist;
    }

    public void setDayOfWeek(int dayOfWeek) {
        if (dayOfWeek >= 0 && dayOfWeek<7)
            this.dayOfWeek = dayOfWeek;
        else throw new IllegalArgumentException();

    }

    public void setLikeDislike(int likeDislike) {
        if (likeDislike >= -1 && likeDislike<=1)
            this.likeDislike = likeDislike;
        else
            throw new IllegalArgumentException();
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setNameOfAlbum(String nameOfAlbum) {
        this.nameOfAlbum = nameOfAlbum;
    }

    public void setNameOfSong(String nameOfSong) {
        this.nameOfSong = nameOfSong;
    }

    public void setReference(int reference) {
        this.reference = reference;
    }

    public void setTimeMS(long timeMS) {
        this.timeMS = timeMS;
    }

    public void setTimeOfDay(int timeOfDay) {
        if (timeOfDay<=2 && timeOfDay>=0)
            this.timeOfDay = timeOfDay;
        else
            throw new IllegalArgumentException();
    }
}
