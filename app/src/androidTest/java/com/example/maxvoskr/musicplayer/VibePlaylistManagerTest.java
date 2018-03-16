package com.example.maxvoskr.musicplayer;

import android.app.Application;
import android.support.test.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by mdavi on 3/15/2018.
 */

public class VibePlaylistManagerTest {
    private CurrentLocationTimeData dataObj;
    private SongFile song1;
    private SongFile song2;
    private SongFile song3;
    private SongFile song4;
    private SongFile song5;
    private VibePlaylistManager vibe;
    private Downloader downloader;
    private ArrayList<Song> songs;

    //create a CurrentLocationTimeData to set weights of all song objects and verify correctness
    // private CurrentLocationTimeData dataObj;
    @Before
    public void before() {

        song1 = new SongFile("Place 1", 100, 1, 1, 0, "Song Title 1", "Album Title 1", "Artist 1", "");

        song2 = new SongFile("Place 2", 200, 3, 2, 1, "Song Title 2", "Album Title 1", "Artist 1", "");

        song3 = new SongFile("", 0, 0, 0, 0, "Song Title 3", "Album Title 2", "Artist 2", "");

        song4 = new SongFile("Place 4", 700, 6, 1, 0, "Song Title 4", "Album Title 3", "Artist 3", "");


        song5 = new SongFile("Place 5", 200, 0, 2, 0, "Song Title 5", "Album Title 4", "Artist 4", "");

        songs = new ArrayList<Song>();
        songs.add(song1);
        songs.add(song2);
        songs.add(song3);
        songs.add(song4);
        songs.add(song5);
        //NOT USED
        downloader = new Downloader(InstrumentationRegistry.getTargetContext(),InstrumentationRegistry.getTargetContext().getResources());

        vibe = new VibePlaylistManager(songs,downloader);
    }
    @Test
    public void TestLocationSetsWeightCorrectly(){
        dataObj = new CurrentLocationTimeData("Place 4",4,0,604802000);
        song1.addLocation("Place 2");
        song1.addLocation("Place 5");
        song4.addLocation("Place 4");
        vibe.setCurrentWeights(dataObj);
        //Played Before No location match
        Assert.assertEquals(song1.getWeight(), 1);
        //Never Played
        Assert.assertEquals(song3.getWeight(),0);
        //Location Only Matches (Played Before)
        Assert.assertEquals(song4.getWeight(),2);
    }
    @Test
    public void TestTimeWithinLastWeek(){
        dataObj = new CurrentLocationTimeData("Place 6",0,0,604800300);
        vibe.setCurrentWeights(dataObj);
        //Played Before No Match
        Assert.assertEquals(song1.getWeight(), 1);
        //Never Played
        Assert.assertEquals(song3.getWeight(),0);
        //DayOfWeek Only Matches (Played Before)
        Assert.assertEquals(song4.getWeight(),2);
    }
    @Test
    public void TestPlayedByFriendSetCorrectly(){
        dataObj = new CurrentLocationTimeData("Place 6",4,2,604802000);
        song2.setPlayedByFriend(true);
        vibe.setCurrentWeights(dataObj);
        //Played Before No Match
        Assert.assertEquals(song1.getWeight(), 1);
        //Never Played
        Assert.assertEquals(song3.getWeight(),0);
        //TimeOfDay Only Matches (Played Before)
        Assert.assertEquals(song2.getWeight(),2);
    }
    @Test
    public void TestAllMatch(){
        dataObj = new CurrentLocationTimeData("Place 2",3,2,604800100);
        song2.addLocation("Place 2");
        song2.setPlayedByFriend(true);
        vibe.setCurrentWeights(dataObj);
        Assert.assertEquals(song2.getWeight(),4);
    }

    @Test
    public void TestGetNextSong(){
        dataObj = new CurrentLocationTimeData("Place 2",3,2,604800100);
        song2.addLocation("Place 2");
        song2.setPlayedByFriend(true);
        song2.setSong("a");
        vibe.setCurrentWeights(dataObj);
        Song next = vibe.getNextSong(false);
        Assert.assertEquals(next,song2);
    }

    @Test
    public void TestGetUpcomingSongs(){
        dataObj = new CurrentLocationTimeData("Place 2",3,2,604800100);
        song2.addLocation("Place 2");
        song2.setPlayedByFriend(true);
        song1.setPlayed();
        song4.setPlayedByFriend(true);
        vibe.setCurrentWeights(dataObj);
        ArrayList<Song> upcoming = vibe.getListOfUpcomingSongs();
        Assert.assertEquals(upcoming.get(0),song2);
        Assert.assertEquals(upcoming.get(1),song4);
        Assert.assertEquals(upcoming.get(2),song5);
    }

}
