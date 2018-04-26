package com.example.oscarmangs.tournamentmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class WhoWonActivity extends AppCompatActivity implements View.OnClickListener {
    private Button player1btn;
    private Button player2btn;
    private Player player1;
    private Player player2;
    private Match currentMatch;
    private int scoreP1;
    private int scoreP2;
    private static final int WON = 1;
    private DatabaseReference playersRef;
    private DatabaseReference matchRef;
    private DatabaseReference mDatabase;
    ArrayList<Player> players = new ArrayList<>();
    private static final String TAG = "MatchInfo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_won);

        Intent intent = getIntent();
        currentMatch = (Match) intent.getExtras().getSerializable("match");

        player1btn = findViewById(R.id.who_won_btn_player1);
        player2btn = findViewById(R.id.who_won_btn_player2);
        player1btn.setOnClickListener(this);
        player2btn.setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = mDatabase.child(MainActivity.userId);
        playersRef = userRef.child("players");
        matchRef = userRef.child("matches");


        playersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                players = getPlayersFromFirebase(dataSnapshot);
                findPlayer(currentMatch);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
        ArrayList<Player> getPlayersFromFirebase (DataSnapshot dataSnapshot){
            ArrayList<Player> players = new ArrayList<>();
            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                Player p = snap.getValue(Player.class);
                p.setKey(snap.getKey());
                players.add(p);
                Log.d(TAG, "Value is: " + p.getName());
            }
            return players;
        }


        public void findPlayer(Match currentMatch) {

            if (!(currentMatch == null)) {

                player1 = Player.findPlayerFromId(currentMatch.getId1(), players);
                player1btn.setText(player1.getName());

                player2 = Player.findPlayerFromId(currentMatch.getId2(), players);
                player2btn.setText(player2.getName());


            }
        }


        @Override
        public void onClick (View v){

            switch (v.getId()) {
                case R.id.who_won_btn_player1:
                    scoreP1 = player1.getScore();
                    player1.setScore(scoreP1 + WON);
                    playersRef.child(player1.getKey()).setValue(player1);

                    Intent intent = new Intent(getApplicationContext(), MatchTableActivity.class);
                    intent.putExtra("player", (Serializable) player1);
                    startActivity(intent);
                    break;

                case R.id.who_won_btn_player2:
                    scoreP2 = player2.getScore();
                    player2.setScore(scoreP2 + WON);
                    playersRef.child(player2.getKey()).setValue(player2);

                    Intent i = new Intent(getApplicationContext(), MatchTableActivity.class);
                    i.putExtra("player", (Serializable) player2);
                    startActivity(i);
                    break;
            }
        }
    }
