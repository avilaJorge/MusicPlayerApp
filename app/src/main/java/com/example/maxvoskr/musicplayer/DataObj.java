package com.example.maxvoskr.musicplayer;

import android.content.Context;

/**
 * Created by Tim on 2/10/2018.
 */

public class DataObj {

    private DateService dateService;
    private LocationService locationService;
    private static String locationName;
    private static int dayOfWeek;
    private static int timeOfDay;
    private static long currentTime;

    public DataObj(DateService dateService, LocationService locationService){
        this.dateService = dateService;
        this.locationService = locationService;
    }

    public void updateData() {
        locationName = locationService.getLocationName();
        dayOfWeek = dateService.getCurrentDayOfWeek();
        timeOfDay = dateService.getCurrentTimeOfDay();
        currentTime = dateService.getCurrentTime();
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
}
