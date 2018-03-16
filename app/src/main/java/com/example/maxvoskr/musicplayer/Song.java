package com.example.maxvoskr.musicplayer;

import java.util.ArrayList;

/**
 * Created by mdavi on 3/8/2018.
 */

class Song {
    protected String lastLocation;
    protected long timeMS;
    protected int dayOfWeek;
    protected int timeOfDay;
    protected int likeDislike;
    protected String nameOfSong;
    protected String nameOfAlbum;
    protected String nameOfArtist;
    protected boolean played;
    protected boolean playedByFriend;
    protected int weight;
    protected String songID;

    protected ArrayList<String> locations;

    Song() {
        this.locations = new ArrayList<String>();
        this.weight = 0;
        this.played = false;
    }

    public boolean isPlayedByFriend() {
        return playedByFriend;
    }

    public void setPlayedByFriend(boolean playedByFriend) {
        this.playedByFriend = playedByFriend;
    }

    public String getLastLocation(){
        return lastLocation;
    }

    public String getSongID(){
        return songID;
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

    public void setSongID(String songID) { this.songID = songID; }

    public boolean beenPlayed(){return played;}

    public int getWeight() {
        return weight;
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

    public void setLastLocation(String location) {
        this.lastLocation = location;
    }

    public void setAlbum(String nameOfAlbum) {
        this.nameOfAlbum = nameOfAlbum;
    }

    public void setName(String nameOfSong) {
        this.nameOfSong = nameOfSong;
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

    public ArrayList<String> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<String> locations) {
        this.locations = locations;
    }

    public void addLocation(String location){
        this.lastLocation = location;
        this.locations.add(location);
    }

    /* not SRP
    public void findWeight(CurrentLocationTimeData dataObj) {
        weight = 0;
        if (timeMS == 0) return;
        else weight ++;
        if (dataObj.getLocation() == lastLocation || dataObj.getLocation().isEmpty()) weight++;
        if (dataObj.getDayOfWeek() == dayOfWeek) weight++;
        if (dataObj.getTimeOfDay() == timeOfDay) weight++;
        //Like Dislike Breaks Ties Only
        //if (likeDislike == 1) weight++;
    }
    */

    public void setWeight(int weight){
        this.weight = weight;
    }

    public String getUrl(){ return "";}
}
