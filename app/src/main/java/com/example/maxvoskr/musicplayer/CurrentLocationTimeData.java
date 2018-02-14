package com.example.maxvoskr.musicplayer;

import android.content.Context;

/**
 * Created by Tim on 2/10/2018.
 */

public class CurrentLocationTimeData {

    private DateService dateService;
    private LocationService locationService;

    private String tempLocation;
    private long tempTimeMS;
    private int tempDayOfWeek;
    private int tempTimeOfDay;

    public CurrentLocationTimeData(DateService dateService, LocationService locationService){
        this.dateService = dateService;
        this.locationService = locationService;
    }

    public String getLocation(){
        return locationService.getLocationName();
    }

    public int getDayOfWeek(){
        return dateService.getCurrentDayOfWeek();
    }

    public int getTimeOfDay(){
        return dateService.getCurrentTimeOfDay();
    }

    public long getTimeMS() {
        return dateService.getCurrentTime();
    }

    //Use at start of song
    public void updateTempData(){
        tempLocation = getLocation();
        tempTimeMS = getTimeMS();
        tempDayOfWeek = getDayOfWeek();
        tempTimeOfDay = getTimeOfDay();
    }

    //Use if song ends
    public void updateSongUsingTemp(Song song) {
        song.setLocation(tempLocation);
        song.setDayOfWeek(tempDayOfWeek);
        song.setTimeMS(tempTimeMS);
        song.setTimeOfDay(tempTimeOfDay);
    }

}
