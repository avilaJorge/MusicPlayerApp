package com.example.maxvoskr.musicplayer;

import java.util.ArrayList;

/**
 * Created by AdityaS on 2/13/18.
 */

public class Album {

    public String albumName;
    //public MusicArrayList musicList;
    public ArrayList<Song> musicList;
    public String artist;

    public Album(String albumName, String artist) {
        this.albumName = albumName;
        //this.musicList = musicList;
        this.musicList = new ArrayList<Song>();
        this.artist = artist;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public ArrayList<Song> getMusicList() {
        return musicList;
    }

    public void setMusicList(ArrayList<Song> musicList) {
        this.musicList = musicList;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void addSong(Song song) {
        this.musicList.add(song);
    }
}
