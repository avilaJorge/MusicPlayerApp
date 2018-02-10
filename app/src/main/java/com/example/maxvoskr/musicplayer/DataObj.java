package com.example.maxvoskr.musicplayer;

import android.content.Context;

/**
 * Created by Tim on 2/10/2018.
 */

public class DataObj {

    private DateService dateService;
    private LocationService locationService;

    public DataObj(){
        dateService = new DateService();
        locationService = new LocationService();
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
