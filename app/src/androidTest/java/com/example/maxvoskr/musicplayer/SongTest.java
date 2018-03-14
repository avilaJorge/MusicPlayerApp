package com.example.maxvoskr.musicplayer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by mdavi on 2/14/2018.
 */

public class SongTest {

    private CurrentLocationTimeData dataObj;
    private Song song1;
    private Song song2;
    private Song song3;
    private Song song4;
    private Song song5;
    //create a CurrentLocationTimeData to set weights of all song objects and verify correctness
    // private CurrentLocationTimeData dataObj;
    @Before
    public void before(){

        song1 = new SongRes("Place 1", 100, 1, 1,0, "Song Title 1", "Album Title 1", "Artist 1", 0);


        song2 = new SongRes("Place 2", 200, 3, 2,1, "Song Title 2", "Album Title 1", "Artist 1", 0);


        song3 = new SongRes("", 0, 0, 0,0,"Song Title 3", "Album Title 2", "Artist 2", 0);


        song4 = new SongRes("Place 4", 700, 6, 1,0,"Song Title 4", "Album Title 3", "Artist 3", 0);


        song5 = new SongRes("Place 5", 200, 0, 2,0,"Song Title 5", "Album Title 4", "Artist 4", 0);

    }
    @Test
    public void TestLocationSetsWeightCorrectly(){
        dataObj = new CurrentLocationTimeData("Place 4",4,0,1000);
        //Played Before No Match
        song1.findWeight(dataObj);
        Assert.assertEquals(song1.getWeight(), 1);
        //Never Played
        song3.findWeight(dataObj);
        Assert.assertEquals(song3.getWeight(),0);
        //Location Only Matches (Played Before)
        song4.findWeight(dataObj);
        Assert.assertEquals(song4.getWeight(),2);
    }
    @Test
    public void TestDayOfWeekSetsWeightCorrectly(){
        dataObj = new CurrentLocationTimeData("Place 6",0,0,1000);
        //Played Before No Match
        song1.findWeight(dataObj);
        Assert.assertEquals(song1.getWeight(), 1);
        //Never Played
        song3.findWeight(dataObj);
        Assert.assertEquals(song3.getWeight(),0);
        //DayOfWeek Only Matches (Played Before)
        song5.findWeight(dataObj);
        Assert.assertEquals(song5.getWeight(),2);
    }
    @Test
    public void TestTimeOfDaySetsWeightCorrectly(){
        dataObj = new CurrentLocationTimeData("Place 6",4,2,1000);
        //Played Before No Match
        song1.findWeight(dataObj);
        Assert.assertEquals(song1.getWeight(), 1);
        //Never Played
        song3.findWeight(dataObj);
        Assert.assertEquals(song3.getWeight(),0);
        //TimeOfDay Only Matches (Played Before)
        song2.findWeight(dataObj);
        Assert.assertEquals(song2.getWeight(),2);
    }
    @Test
    public void TestAllMatch(){
        dataObj = new CurrentLocationTimeData("Place 2",3,2,1000);
        //Played Before No Match
        song1.findWeight(dataObj);
        Assert.assertEquals(song1.getWeight(), 1);
        //Never Played
        song3.findWeight(dataObj);
        Assert.assertEquals(song3.getWeight(),0);
        //All Matched
        song2.findWeight(dataObj);
        Assert.assertEquals(song2.getWeight(),4);
    }
    @Test
    public void TestUpdate() {
        dataObj = new CurrentLocationTimeData("Place 2",3,2,1000);

        dataObj.updateTempData();
        dataObj.updateSongUsingTemp(song1);
        dataObj.updateSongUsingTemp(song3);
        dataObj.updateSongUsingTemp(song2);

        //Played Before No Match
        song4.findWeight(dataObj);
        Assert.assertEquals(song4.getWeight(), 1);
        //Never Played (Now Updated should match all)
        song3.findWeight(dataObj);
        Assert.assertEquals(song3.getWeight(),4);
        //All Matched Before and After
        song2.findWeight(dataObj);
        Assert.assertEquals(song2.getWeight(),4);


    }


}
