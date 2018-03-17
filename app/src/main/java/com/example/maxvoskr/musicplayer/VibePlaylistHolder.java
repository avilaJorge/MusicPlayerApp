package com.example.maxvoskr.musicplayer;

import android.content.Context;

/**
 * Created by mdavi on 3/16/2018.
 */


public class VibePlaylistHolder {
    public static VibePlaylistManager playlistManager;
    public static void setPlaylistManager(VibePlaylistManager playlistManager) {
        VibePlaylistHolder.playlistManager = playlistManager;
    }
}
