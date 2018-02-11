package com.example.maxvoskr.musicplayer;

import android.content.Context;

/**
 * Created by Tim on 2/10/2018.
 */

public class CurrentDataObj {

    private DateService dateService;
    private LocationService locationService;

    public CurrentDataObj(DateService dateService, LocationService locationService){
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

    /*
    public void updateData(Song song) {
        song.setLocation(locationService.getLocationName());
        song.setDayOfWeek(dateService.getCurrentDayOfWeek());
        song.setTimeMS(dateService.getCurrentTime());
        song.setTimeOfDay(dateService.getCurrentTimeOfDay());
    }*/

}
