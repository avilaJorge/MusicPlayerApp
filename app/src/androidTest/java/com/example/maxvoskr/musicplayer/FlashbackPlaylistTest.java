package com.example.maxvoskr.musicplayer;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by mdavi on 2/10/2018.
 */
/**
 * Tests that check the FlashbackPlaylist Class.
 */

public class FlashbackPlaylistTest {

    //create 5 song objects with different weights
    private Song song1;
    private Song song2;
    private Song song3;
    private Song song4;
    private Song song5;
    private ArrayList<Song> list;

    //create a CurrentLocationTimeData to set weights of all song objects and verify correctness
    // private CurrentLocationTimeData dataObj;
    @Before
    public void before(){

        song1 = new Song("Place 1", 100, 1, 1,0, "Song Title 1", "Album Title 1", "Artist 1", 0);


        song2 = new Song("Place 2", 200, 3, 2,1, "Song Title 2", "Album Title 1", "Artist 1", 0);


        song3 = new Song("Place 3", 500, 2, 0,0,"Song Title 3", "Album Title 2", "Artist 2", 0);


        song4 = new Song("Place 4", 700, 6, 1,0,"Song Title 4", "Album Title 3", "Artist 3", 0);


        song5 = new Song("Place 5", 200, 0, 2,0,"Song Title 5", "Album Title 4", "Artist 4", 0);



        list = new ArrayList<Song>();
        list.add(song1);
        list.add(song2);
        list.add(song3);
        list.add(song4);
        list.add(song5);
    }

    @Test
    public void TestGetNextSong(){
        song1.setWeight(2);
        song2.setWeight(3);
        song3.setWeight(1);
        song4.setWeight(4);
        song5.setWeight(6);
        FlashbackPlaylist flashback = new FlashbackPlaylist(list);
        Assert.assertEquals(song5, flashback.getNextSong());
    }
    @Test
    public void TestGetNextSongBreakTieByTime(){
        song1.setWeight(2);
        song2.setWeight(3);
        song3.setWeight(1);
        song4.setWeight(6);
        song5.setWeight(6);
        FlashbackPlaylist flashback = new FlashbackPlaylist(list);
        Assert.assertEquals(song4, flashback.getNextSong());
    }
    @Test
    public void TestGetNextSongNoNext(){
        song1.setWeight(0);
        song2.setWeight(0);
        song3.setWeight(0);
        song4.setWeight(0);
        song5.setWeight(0);
        FlashbackPlaylist flashback = new FlashbackPlaylist(list);
        Assert.assertEquals(null, flashback.getNextSong());
    }

    @Test
    public void TestGetNextSongRepeatList(){
        song1.setWeight(5);
        song1.setPlayed();
        song2.setWeight(6);
        song2.setPlayed();
        song3.setWeight(2);
        song3.setPlayed();
        song4.setWeight(3);
        song4.setPlayed();
        song5.setWeight(1);
        song5.setPlayed();
        FlashbackPlaylist flashback = new FlashbackPlaylist(list);
        Assert.assertEquals(song2, flashback.getNextSong());
    }
    @Test
    public void TestGetNextSongSomePlayed(){
        song1.setWeight(5);
        song2.setPlayed();
        song2.setWeight(6);
        song2.setPlayed();
        song3.setWeight(2);
        song4.setWeight(3);
        song5.setWeight(1);
        FlashbackPlaylist flashback = new FlashbackPlaylist(list);
        Assert.assertEquals(song4, flashback.getNextSong());
    }

}