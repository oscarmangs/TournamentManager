package com.example.oscarmangs.tournamentmanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class MatchTableActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MatchInfo";

    private ArrayList<Player> players;
    private ArrayList<String> resultat;
    private DatabaseReference playersRef;
    private DatabaseReference matchRef;
    private DatabaseReference mDatabase;
    private Button btn;
    private Boolean avsluta = false;
    private ListView lista;
    private ArrayAdapter<String> resultatAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_table);

        resultat = new ArrayList<String>();

        resultatAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, resultat);

        lista = findViewById(R.id.lista);
        btn = findViewById(R.id.return_button);
        btn.setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = mDatabase.child(MainActivity.userId);
        playersRef = userRef.child("players");
        matchRef = userRef.child("matches");

        matchRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    Toast.makeText(MatchTableActivity.this,
                            "Vi har en vinnare!!!", Toast.LENGTH_LONG).show();
                    btn.setText("Avsluta turnering");
                    avsluta = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        playersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                players = getPlayers(dataSnapshot);
                playerScoreTable(players);

            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    ArrayList<Player> getPlayers(DataSnapshot dataSnapshot) {
        ArrayList<Player> players = new ArrayList<>();
        for (DataSnapshot snap : dataSnapshot.getChildren()) {
            Player p = snap.getValue(Player.class);
            p.setKey(snap.getKey());
            players.add(p);
            Log.d("Oscar", "Value is: " + p.getName());
        }
        return players;
    }

    public void playerScoreTable(ArrayList<Player> scoreList){
       // final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //layout = findViewById(R.id.match_linearlayout);


        for (Player x: scoreList) {
           // final TextView textView = new TextView(this);
           // textView.setLayoutParams(lparams);
            //textView.setText("Deltagare:   " + x.getName() + "   ");
            //String score = String.valueOf("Poäng:   " + x.getScore());
            resultat.add(x.getName() + "        Poäng:      " + x.getScore());
            //textView.append(score);
            //textView.setTextSize(20);
            //textView.setTextColor(Color.parseColor("#000000"));
            lista.setAdapter(resultatAdapter);
        }
    }


    @Override
    public void onClick(View view) {
            if (!avsluta){
                Intent intent = new Intent(getApplicationContext(), ScheduleActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }


    }
}
