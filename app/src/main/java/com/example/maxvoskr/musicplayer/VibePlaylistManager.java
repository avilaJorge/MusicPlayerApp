package com.example.maxvoskr.musicplayer;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by mdavi on 3/13/2018.
 */


public class VibePlaylistManager {
    private ArrayList<Song> listOfSongs;
    private ArrayList<SongFile> sortedListOfSongs;
    private ArrayList<SongFile> listOfDownloading;
    private Downloader downloader;
    long MS_IN_WEEK = 604800000;
    int curIndex;
    //Constructor accepts list of all songs (Both downloaded and not)
    //Constructor takes Downloader to handle downloads of upcoming songs.
    VibePlaylistManager(ArrayList<Song> listOfSongs, Downloader downloader){
        this.listOfSongs = listOfSongs;
        for(Song s : listOfSongs){
            s.unsetPlayed();
            Log.d("UNSETTING PLAY", s.getName());
        }
        this.downloader = downloader;
        listOfDownloading = new ArrayList<SongFile>();
        curIndex = 0;
    }

    /* sets the weights of all songs based on the current lastLocation/time/etc
     * TODO: connect with vibe player (trigger on current song end after updating songs from firebase
     *
     */
    public void setCurrentWeights(CurrentLocationTimeData dataObj){
        for(Song s : listOfSongs){
            int weight = 0;
            if (s.getTimeMS() == 0 || (s.getClass()==SongRes.class) || s.beenPlayed()) {
                Log.d("SETTING WEIGHT AS ", s.getName() +":" + 0);
                s.setWeight(0);
                continue;
            }
            else
                weight++;
            for (String location : s.getLocations()){
                if (dataObj.getLocation().equals(location) && !location.isEmpty()) {
                    weight++;
                    break;
                }}
            if ((dataObj.getTimeMS()-MS_IN_WEEK) <= s.getTimeMS()) weight++;
            if (s.isPlayedByFriend()) weight++;
            Log.d("SETTING WEIGHT AS ", s.getName() +":" + weight);
            s.setWeight(weight);
        }
    }
    /*
     * TODO: connect getNextSong with vibe mode (trigger on current song end)
     * returns null if there are no songs eligible to play or if waiting on download
     *
     */
    public Song getNextSong(boolean network) {
        sortByWeight();

        Song nextSong = null;
        if (sortedListOfSongs.isEmpty()) return nextSong;

        SongFile next = sortedListOfSongs.get(0);
        if (next.beenPlayed()) {
            resetPlayed();
            sortByWeight();
        }
        //Looks at upcoming 3 songs if none downloaded then user must wait on downloads.
        //Downloads ignored if network is off
        for (int i = 0; i < 2 && i < sortedListOfSongs.size(); i++) {
            next = sortedListOfSongs.get(i);
            Log.d("SONG PATH FOR NEXT SONG",next.getSong());
            if (next.getSong().isEmpty()) {
                if (!listOfDownloading.contains(next) && network) {
                    listOfDownloading.add(next);
                    downloader.download(next);
                }
            } else {
                if (nextSong == null)
                nextSong = next;
            }
        }
        //get next offline track if user is waiting on downloads
        if (nextSong == null){
            for (int i = 0; i < sortedListOfSongs.size(); i++) {
                next = sortedListOfSongs.get(i);
                if (!next.getSong().isEmpty()) {
                    if (nextSong == null)
                        nextSong = next;
                }
            }
        }
        return nextSong;
    }

    //Quicksort songs that have not been played and
    private void sortByWeight (){
        sortedListOfSongs = new ArrayList<SongFile>();
        for(Song s : listOfSongs){
            if (s.getWeight()>0 && (s.getClass() == SongFile.class))
                sortedListOfSongs.add((SongFile) s);
        }
        sortByWeightRecursive(0,sortedListOfSongs.size()-1);
    }

    private void sortByWeightRecursive( int low, int high){
        if (low < high){
            int pivot = (sortedListOfSongs.get(high)).getWeight();
            int i = low - 1;
            for (int j = low; j<high; j++){
                if (sortedListOfSongs.get(j).weight <= pivot){
                    i++;
                    SongFile temp = sortedListOfSongs.get(i);
                    sortedListOfSongs.set(i,sortedListOfSongs.get(j));
                    sortedListOfSongs.set(j,temp);
                }
            }
            SongFile temp = sortedListOfSongs.get(i+1);
            sortedListOfSongs.set(i+1,sortedListOfSongs.get(high));
            sortedListOfSongs.set(high,temp);
            i++;
            sortByWeightRecursive(low,i-1);
            sortByWeightRecursive(i+1,high);
        }
    }

    private void resetPlayed(){
        for (Song s: listOfSongs){
            s.unsetPlayed();
        }
    }

    /*
     * TODO: Get upcoming song list for Vibe Mode (trigger on Song End)
     * (I.E) what to display on vibe GUI list view
     */
    public ArrayList<Song> getListOfUpcomingSongs(){
        sortByWeight();
        ArrayList<Song> upcoming = new ArrayList<Song>();
        for (int i = sortedListOfSongs.size()-1; i>=0 && i > sortedListOfSongs.size()-25; i--)
            upcoming.add(sortedListOfSongs.get(i));
        return upcoming;
    }
}

