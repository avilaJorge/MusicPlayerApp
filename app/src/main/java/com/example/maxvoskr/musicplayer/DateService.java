package com.example.maxvoskr.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class DateService extends Service {

    private final static int MIN_MORNING = 5;
    private final static int MIN_AFTERNOON = 11;
    private final static int MIN_EVENING = 17;
    private final static int MORNING = 0;
    private final static int AFTERNOON = 1;
    private final static int EVENING = 2;
    private static int day_Of_Week = -1;
    private static int hour_Of_Day = -1;
    private SimpleTimeZone timezone;
    private Date currentTime;
    private Calendar calendar;

    public DateService() {
    }

    public class DateBinder extends Binder {
        DateService getDateService() {return DateService.this;}
    }
    private final IBinder binder = new DateBinder();
    @Override
    public IBinder onBind(Intent intent) {return binder;}

    @Override
    public void onCreate() {
        // get the supported ids for GMT-08:00 (Pacific Standard Time)
        String[] ids = TimeZone.getAvailableIDs(-8 * 60 * 60 * 1000);
        // create a Pacific Standard Time time zone
        SimpleTimeZone timeZone = new SimpleTimeZone(-8 * 60 * 60 * 1000, ids[0]);
        // set up rules for Daylight Saving Time
        timezone.setStartRule(Calendar.APRIL, 1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
        timezone.setEndRule(Calendar.OCTOBER, -1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
        calendar = new GregorianCalendar(timezone);
    }

    public long getCurrentTime() {
        currentTime = new Date();
        return currentTime.getTime();
    }

    public int getCurrentTimeOfDay() {
        updateTime();
        return getTimeOfDay(Calendar.HOUR_OF_DAY);
    }

    public int getCurrentDayOfWeek() {
        if(day_Of_Week != Calendar.DAY_OF_WEEK) {
            updateTime();
            return (day_Of_Week = Calendar.DAY_OF_WEEK);
        }
        return Calendar.DAY_OF_WEEK;
    }

    private void updateTime() {
        currentTime = new Date();
        calendar.setTime(currentTime);
    }

    private int getTimeOfDay(int hourOfDay) {
        if(hourOfDay < MIN_MORNING) {
            return EVENING;
        } else if(hourOfDay < MIN_AFTERNOON) {
            return MORNING;
        } else if(hourOfDay < MIN_EVENING){
            return AFTERNOON;
        } else return EVENING;
    }
}
