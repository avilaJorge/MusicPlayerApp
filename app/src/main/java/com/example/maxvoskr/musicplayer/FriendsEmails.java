package com.example.maxvoskr.musicplayer;

import com.google.api.services.people.v1.model.EmailAddress;

import java.util.ArrayList;

/**
 * Created by maxvoskr on 3/14/18.
 */

public class FriendsEmails {
    private ArrayList<String> friendsEmails;

    FriendsEmails() {
        this.friendsEmails = new ArrayList<String>();
    }

    void add(String email) {
        this.friendsEmails.add(email);
    }

    void remove(String email) {
        this.friendsEmails.remove(email);
    }

}
