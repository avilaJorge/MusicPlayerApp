package com.example.maxvoskr.musicplayer;

import java.util.List;

/**
 * Created by AdityaS on 2/13/18.
 */

public class Album {

    public String albumName;
    public static List<Song> musicList;
    public String artist;

    public Album(String albumName, List<Song> musicList, String artist) {
        this.albumName = albumName;
        this.musicList = musicList;
        this.artist = artist;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public List<Song> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<Song> musicList) {
        this.musicList = musicList;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
