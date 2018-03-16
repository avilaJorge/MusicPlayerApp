package com.example.maxvoskr.musicplayer;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by maxvoskr on 2/10/18.
 */

public class MusicArrayList {

    public static ArrayList<Song> localMusicList;
    public static ArrayList<Song> allMusicList;
    public static HashMap<String, Song> allMusicTable;
    public static ArrayList<Album> albumList;

    public MusicArrayList() {
        localMusicList = new ArrayList<Song>();
        albumList = new ArrayList<Album>();
        allMusicList = new ArrayList<Song>();
        allMusicTable = new HashMap<String, Song>();
    }

    public static void insertLocalSong(Song song) {
        if(allMusicTable.get(getSongHash(song)) == null) {
            Log.d("Importing Songs", "Song: " + song.getName() + "was not found in hashTable");
            allMusicList.add(song);
            String key = getSongHash(song);
            allMusicTable.put(key, song);
        }
        else {
            Log.d("Importing Songs", "Song: " + song.getName() + "was found hashTable");
            Song fbSong = allMusicTable.get(getSongHash(song));
            ((SongFile) song).setUrl(((SongFile) fbSong).getUrl());
            song.setSongID(fbSong.getSongID());
        }



        if(localMusicList.indexOf(song) == -1) {
            localMusicList.add(song);
        }




        boolean added = false;
        for(Album album : albumList) {
            if(album.getAlbumName().equals(song.getAlbum())){
                if(album.getMusicList().indexOf(song) == -1) {
                    album.addSong(song);
                }
                added = true;
                break;
            }
        }

        if(!added) {
            Album album = new Album(song.getAlbum(), song.getArtist());
            album.addSong(song);
            albumList.add(album);
        }
    }

    public static void insertFBSong(Song song) {
        if(allMusicList.indexOf(song) == -1) {
            allMusicList.add(song);
            String key = song.getName() + song.getAlbum() + song.getArtist();
            allMusicTable.put(key, song);
        }
    }

    public static String getSongHash(Song song) {
        return song.getName() + song.getAlbum() + song.getArtist();
    }
}
