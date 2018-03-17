package com.example.maxvoskr.musicplayer;

import com.google.api.services.people.v1.model.EmailAddress;

import java.util.ArrayList;

/**
 * Created by maxvoskr on 3/14/18.
 */

public class FriendsNames {
    private ArrayList<String> friendsNames;

    FriendsNames() {
        this.friendsNames = new ArrayList<String>();
    }

    void add(String email) {
        this.friendsNames.add(email);
    }

    void remove(String email) {
        this.friendsNames.remove(email);
    }

}
