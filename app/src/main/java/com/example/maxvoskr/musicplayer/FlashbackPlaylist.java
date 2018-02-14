package com.example.maxvoskr.musicplayer;


import java.util.ArrayList;

/**
 * Created by mdavi on 2/7/2018.
 */

public class FlashbackPlaylist {
    private ArrayList<Song> listOfSongs;
    FlashbackPlaylist(ArrayList<Song> listOfSongs){
        this.listOfSongs = listOfSongs;
        for(Song s : listOfSongs){
            s.unsetPlayed();
        }
    }

    /* sets the weights of all songs based on the current location/time/etc */
    public void setCurrentWeights(CurrentDataObj dataObj){
        for(Song s : listOfSongs){
            if (!s.beenPlayed())
                s.findWeight(dataObj);
        }
    }
    /*
     * getsNextSong for flashback mode (trigger on song end)
     * input(dataObj that contains the user location and time info)
     * output nextSong obj
     */
    public Song getNextSong(){
        boolean repeatFlag = false;
        Song next = null;
        int maxWeight = 0;
        for(Song s : listOfSongs){
            if(s.getWeight() != 0 && s.getLikeDislike()!=-1) repeatFlag = true;
            if(s.beenPlayed() || s.getLikeDislike()==-1 || s.getWeight() == 0)continue;
            if (s.getWeight()>=maxWeight){
                if(s.getWeight()>maxWeight) {
                    maxWeight = s.getWeight();
                    next = s;
                }
                else if (s.getLikeDislike()>next.getLikeDislike()) next = s;
                else if (s.getTimeMS()>next.getTimeMS()) next = s;
            }
        }
        if (next == null && repeatFlag == true){
            for(Song s : listOfSongs){
                s.unsetPlayed();
            }
            return this.getNextSong();
        }
        else
        return next;
    }
}
