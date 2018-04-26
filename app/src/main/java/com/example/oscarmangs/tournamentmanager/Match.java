package com.example.oscarmangs.tournamentmanager;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;

public class Match implements Serializable {
    String id1;
    String id2;
    @Exclude
    String key;

    public Match() {}

    public Match(String id1, String id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    public String getKey() {
        return key;
    }
    public String getId1(){
        return id1;
    }

    public String getId2(){
        return id2;
    }

    public void setKey(String key) {
        this.key = key;
    }

    static Match findMatchFromId(String id, ArrayList<Match> matches) {
        for (Match m : matches) {
            if (m.getKey().equals(id)) {
                return m;
            }
        }
        return null;
    }
}
