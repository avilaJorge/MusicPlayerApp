package com.example.maxvoskr.musicplayer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by maxvoskr on 2/10/18.
 */

public class MusicArrayList {

    public static ArrayList<Song> musicList;
    public static ArrayList<Album> albumList;
    public static Set<Album> albumSet;

    public MusicArrayList() {
        musicList = new ArrayList<Song>();
        albumList = new ArrayList<Album>();
        albumSet = new HashSet<Album>();
    }
}
