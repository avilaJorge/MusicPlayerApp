package com.example.maxvoskr.musicplayer;

import java.util.ArrayList;

/**
 * Created by maxvoskr on 3/14/18.
 */

public class FriendsNames {
    static private ArrayList<String> friendsNames;

    FriendsNames() {
        this.friendsNames = new ArrayList<String>();
    }

    static void add(String email) {
        friendsNames.add(email);
    }

    static void remove(String email) {
        friendsNames.remove(email);
    }

    static ArrayList<String> getNames() {
        return friendsNames;
    }

}
