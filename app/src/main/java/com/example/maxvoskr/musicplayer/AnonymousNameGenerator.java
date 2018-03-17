package com.example.maxvoskr.musicplayer;

/**
 * Created by mdavi on 3/16/2018.
 */

public class AnonymousNameGenerator {

    static private String[] animals = {"Dolphin","Eagle","Pony","Giraffe","Wolf","Lion","Bear","Kitten","Puppy","Lizard"};

    static public String generateAnonymousName(String userID){
        int idAsNum = 0;
        for (char c : userID.toCharArray()){
            idAsNum += c;
        }
        int nameIndex = idAsNum % 10;
        int fourDigId = (idAsNum*1543) % 10000;
        String anon = animals[nameIndex] + String.format("%04d", fourDigId);
        return anon;
    }
}
