package com.example.oscarmangs.tournamentmanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference matchRef;
    ArrayList<String> schema;
    private DatabaseReference playersRef;
    ArrayList<Player> players = new ArrayList<>();
    private List<Match> matches = new ArrayList<>();
    private static final String TAG = "MatchInfo";
    private Button nextGame;
    private DatabaseReference mDatabase;
    ListView lstView;
    ArrayAdapter<String> itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        schema = new ArrayList<>();

        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, schema);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = mDatabase.child(MainActivity.userId);
        playersRef = userRef.child("players");
        matchRef = userRef.child("matches");

        lstView = findViewById(R.id.lv);
        lstView.setAdapter(itemsAdapter);
        nextGame = findViewById(R.id.nextGame);
        nextGame.setOnClickListener(this);


        playersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                players = getPlayers(dataSnapshot);
                matchRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChildren() ) {
                            for( DataSnapshot snap : dataSnapshot.getChildren()) {
                                Match match = snap.getValue(Match.class);
                                match.setKey(snap.getKey());

                                matches.add(match);
                            }
                            printMatches();
                        }else {
                            matches = createMatches(players);
                            Collections.shuffle(matches);
                            pushMatchesToFirebase();
                            printMatches();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }



            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    void pushMatchesToFirebase() {
        for (int i = 0; i < matches.size(); i++) {
            String key = matchRef.push().getKey();
            matchRef.child(key).setValue(matches.get(i));
            matches.get(i).setKey(key);
        }
    }

    ArrayList<Player> getPlayers(DataSnapshot dataSnapshot) {
        ArrayList<Player> players = new ArrayList<>();
        for (DataSnapshot snap : dataSnapshot.getChildren()) {
            Player p = snap.getValue(Player.class);
            p.setKey(snap.getKey());
            players.add(p);
            Log.d("Oscar", "Value is: " + p.getName());
        }
        return  players;

    }


    ArrayList<Match> createMatches(ArrayList<Player> playerList) {

        ArrayList<Match> matches = new ArrayList<>();
        for (int i = 0; i < playerList.size(); i++) {
            for (int j = i + 1; j < playerList.size(); j++) {
                Match match = new Match(playerList.get(i).getKey(), playerList.get(j).getKey());

                matches.add(match);
                Log.d("MatchTest", "Value is: " + matches.get(i));

            }
        }
        return matches;

    }





    void printMatches() {

        for (Match x : matches) {
            schema.add(Player.findPlayerFromId(x.getId1(), players).getName() + "     VS      " + Player.findPlayerFromId(x.getId2(), players).getName() + "\n");

        }



        itemsAdapter.notifyDataSetChanged();
    }



    @Override
    public void onClick(View v) {
        if (matches.size() > 0) {
            Intent intent = new Intent(getApplicationContext(), WhoWonActivity.class);

            intent.putExtra("match", (Serializable) matches.get(0));
            matchRef.child(matches.get(0).key).removeValue();
            startActivity(intent);
        }
    }
}