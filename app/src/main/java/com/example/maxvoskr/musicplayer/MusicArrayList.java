package com.example.maxvoskr.musicplayer;

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
    //public static Set<Album> albumSet;

    public MusicArrayList() {
        localMusicList = new ArrayList<Song>();
        albumList = new ArrayList<Album>();
      //  albumSet = new HashSet<Album>();
        allMusicList = new ArrayList<Song>();
        allMusicTable = new HashMap<String, Song>();
    }

    public static void insertLocalSong(Song song) {
        if(allMusicList.indexOf(song) == -1) {
            allMusicList.add(song);
            String key = getSongHash(song);
            allMusicTable.put(key, song);

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
