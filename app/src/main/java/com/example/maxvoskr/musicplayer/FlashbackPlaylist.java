package com.example.maxvoskr.musicplayer;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by mdavi on 2/7/2018.
 */

public class FlashbackPlaylist {
    private Vector<Song> listOfSongs;
    FlashbackPlaylist(Vector<Song> listOfSongs){
        this.listOfSongs = listOfSongs;
        for(Song s : listOfSongs){
            s.unsetPlayed();
        }
    }
    /*
     * getsNextSong for flashback mode (trigger on song end)
     * input(dataObj that contains the user location and time info)
     */
    public Song getNextSong(DataObj dataObj){
        for(Song s : listOfSongs){
            if (!s.beenPlayed())
                s.findWeight(dataObj);
        }
        Song next;
        int maxWeight = 0;
        for(Song s : listOfSongs){
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
        return next;
    }
}
