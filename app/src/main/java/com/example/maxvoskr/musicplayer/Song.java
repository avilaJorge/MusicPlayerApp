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
    private int song;
    private boolean played;
    private int weight;


    Song(String name, String album, String artist, int song){
        this.nameOfSong = name;
        this.nameOfAlbum = album;
        this.nameOfArtist = artist;
        this.song = song;
        this.played = false;
        this.weight = 0;
    }

    Song(String location, long timeMS, int dayOfWeek, int timeOfDay,
         int likeDislike, String name, String album, String artist, int song){
        this.location = location;
        this.timeMS = timeMS;
        this.dayOfWeek = dayOfWeek;
        this.timeOfDay = timeOfDay;
        this.likeDislike = likeDislike;
        this.nameOfSong = name;
        this.nameOfAlbum = album;
        this.nameOfArtist = artist;
        this.song = song;
        this.played = false;
        this.weight = 0;
    }

    Song(String location, long timeMS, int dayOfWeek, int timeOfDay,
         int likeDislike, String name, String album, int song){
        this.location = location;
        this.timeMS = timeMS;
        this.dayOfWeek = dayOfWeek;
        this.timeOfDay = timeOfDay;
        this.likeDislike = likeDislike;
        this.nameOfSong = name;
        this.nameOfAlbum = album;
        this.song = song;
        this.played = false;
        this.weight = 0;
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

    public String getName() {
        return nameOfSong;
    }

    public String getAlbum() {
        return nameOfAlbum;
    }

    public String getArtist() {
        return nameOfArtist;
    }

    public boolean beenPlayed(){return played;}

    public int getWeight() {
        return weight;
    }

    //reference to song
  
    public int getSong() {
        return song;
    }

    public void setArtist(String nameOfArtist) {
        this.nameOfArtist = nameOfArtist;
    }

    public void setPlayed() {
        this.played = true;
    }
    public void unsetPlayed(){
        this.played = false;
    }
  
    public void setDayOfWeek(int dayOfWeek) {

        if (dayOfWeek >= 1 && dayOfWeek<8)
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

    public void setAlbum(String nameOfAlbum) {
        this.nameOfAlbum = nameOfAlbum;
    }

    public void setName(String nameOfSong) {
        this.nameOfSong = nameOfSong;
    }

    public void setSong(int song) {
        this.song = song;
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
    public void findWeight(CurrentLocationTimeData dataObj) {
        weight = 0;
        if (timeMS == 0) return;
        else weight ++;
        if (dataObj.getLocation() == location && dataObj.getLocation().isEmpty()) weight++;
        if (dataObj.getDayOfWeek() == dayOfWeek) weight++;
        if (dataObj.getTimeOfDay() == timeOfDay) weight++;
        //Like Dislike Breaks Ties Only
        //if (likeDislike == 1) weight++;
    }

    //ONLY TO BE USED BY TESTERS
    public void setWeight(int weight){
        this.weight = weight;
    }



}
