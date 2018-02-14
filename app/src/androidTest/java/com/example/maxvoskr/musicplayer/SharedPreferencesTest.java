package com.example.maxvoskr.musicplayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 * Tests that modify the shared preferences.
 */
@SmallTest
public class SharedPreferencesTest {
    private static final String PREFS_NAME = "SongData";
    private static final String KEY_PREF = "KEY_PREF";
    private SharedPreferences sharedPreferences;
    private SongHistorySharedPreferenceManager songHistorySharedPreferenceManager;
    private Song song1;
    private Song song2;
    private Context context;

    @Before
    public void before() {
        context = InstrumentationRegistry.getTargetContext();

        song1 = new Song("Song Title","Album Title","Artist",0);
        song1.setTimeMS(0);
        song1.setDayOfWeek(0);
        song1.setTimeOfDay(0);
        song1.setLikeDislike(0);
        song1.setLocation("");

        song2 = new Song("Song Title","Album Title","Artist",0);
        song2.setTimeMS(0);
        song2.setDayOfWeek(0);
        song2.setTimeOfDay(0);
        song2.setLikeDislike(0);
        song2.setLocation("");

        songHistorySharedPreferenceManager = new SongHistorySharedPreferenceManager(context);
    }

    @Test
    public void testRetrieveLocation() throws Exception {
        song1.setLocation("Mars");
        songHistorySharedPreferenceManager.writeData(song1);
        songHistorySharedPreferenceManager.updateData(song2);

        assertEquals(song1.getLocation(), song2.getLocation());
    }
    @Test
    public void testRetrieveTimeMS() throws Exception {
        song1.setLocation("Mars");
        songHistorySharedPreferenceManager.writeData(song1);
        songHistorySharedPreferenceManager.updateData(song2);

        assertEquals(song1.getLocation(), song2.getLocation());
    }
    @Test
    public void testRetrieveDayOfWeek() throws Exception {
        song1.setDayOfWeek(3);
        songHistorySharedPreferenceManager.writeData(song1);
        songHistorySharedPreferenceManager.updateData(song2);

        assertEquals(song1.getLocation(), song2.getLocation());
    }
    @Test
    public void testRetrieveTimeOfDay() throws Exception {
        song1.setTimeOfDay(2);
        songHistorySharedPreferenceManager.writeData(song1);
        songHistorySharedPreferenceManager.updateData(song2);

        assertEquals(song1.getLocation(), song2.getLocation());
    }
    @Test
    public void testRetrieveLikeDislike() throws Exception {
        song1.setLikeDislike(-1);
        songHistorySharedPreferenceManager.writeData(song1);
        songHistorySharedPreferenceManager.updateData(song2);

        assertEquals(song1.getLocation(), song2.getLocation());
    }
    @Test
    public void testWriteNullSong() throws Exception {
        try{
            songHistorySharedPreferenceManager.writeData(null);
            fail("Writing a null song should throw exception");
        }
        catch (NullPointerException e){
        }
    }
    @Test
    public void testRetrieveNullSong() throws Exception {
        try{
            Song song3;
            songHistorySharedPreferenceManager.updateData(null);
            fail("Retrieving a null song should throw exception");
        }
        catch (NullPointerException e){
        }
    }

    @After
    public void after() {
        //sharedPreferences.edit().putString(KEY_PREF, null).apply();
    }
}