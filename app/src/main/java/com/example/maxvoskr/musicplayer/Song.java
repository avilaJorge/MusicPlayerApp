package com.example.maxvoskr.musicplayer;

/**
 * Created by mdavi on 3/8/2018.
 */

class Song {
    protected String location;
    protected long timeMS;
    protected int dayOfWeek;
    protected int timeOfDay;
    protected int likeDislike;
    protected String nameOfSong;
    protected String nameOfAlbum;
    protected String nameOfArtist;
    protected boolean played;
    protected int weight;
    protected String songID;

    Song() {
        this.weight = 0;
        this.played = false;
    }

    public String getLocation(){
        return location;
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

    public void setLocation(String location) {
        this.location = location;
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

    public void findWeight(CurrentLocationTimeData dataObj) {
        weight = 0;
        if (timeMS == 0) return;
        else weight ++;
        if (dataObj.getLocation() == location || dataObj.getLocation().isEmpty()) weight++;
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
