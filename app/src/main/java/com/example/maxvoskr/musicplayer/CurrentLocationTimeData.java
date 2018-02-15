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

    private String location;
    private int dayOfWeek;
    private int timeOfDay;
    private long timeMS;

    //Tested Using GPX
    public CurrentLocationTimeData(DateService dateService, LocationService locationService){
        this.dateService = dateService;
        this.locationService = locationService;
    }

    //Used for tests involving CurrentLocationTimeData
    public CurrentLocationTimeData(String location, int dayOfWeek, int timeOfDay, long timeMS){
        this.location = location;
        this.dayOfWeek = dayOfWeek;
        this.timeOfDay = timeOfDay;
        this.timeMS = timeMS;
    }

    public String getLocation(){
        return locationService.getLocationName();
    }

    public int getDayOfWeek(){
        if (dateService != null)
        dayOfWeek = dateService.getCurrentDayOfWeek();
        return dayOfWeek;
    }

    public int getTimeOfDay(){
        if (dateService != null)
        timeOfDay = dateService.getCurrentTimeOfDay();
        return timeOfDay;
    }

    public long getTimeMS() {
        if (dateService != null)
        timeMS = dateService.getCurrentTime();
        return timeMS;
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
