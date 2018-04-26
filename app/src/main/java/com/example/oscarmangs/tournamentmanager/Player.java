package com.example.oscarmangs.tournamentmanager;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
    private String name;
    private int score;
    @Exclude
    private String key;


    public Player(){}

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    static Player findPlayerFromId(String id, ArrayList<Player> players) {
        for (Player p : players) {
            if (p.getKey().equals(id)) {
                return p;
            }
        }
        return null;
    }

}
