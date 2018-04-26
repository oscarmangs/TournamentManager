package com.example.oscarmangs.tournamentmanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout layout;
    private EditText nameInput;
    private Button addBtn;
    private Button nextBtn;
    private List<Player> players = new ArrayList<>();
    private DatabaseReference mDatabase;
    private DatabaseReference playersRef;
    private DatabaseReference matchRef;
    private static final String TAG = "Player";
    private ArrayList<String> createdPlayers;
    private ListView list;
    private ArrayAdapter<String> createAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        createdPlayers = new ArrayList<>();
        createAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, createdPlayers);

        list = findViewById(R.id.skapa_lista);
        list.setAdapter(createAdapter);

        findComponents();



        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = mDatabase.child(MainActivity.userId);
        playersRef = userRef.child("players");
        matchRef = userRef.child("matches");

        matchRef.removeValue();
        playersRef.removeValue();
    }

    public void findComponents(){

        nameInput = findViewById(R.id.create_name_input_text);
        addBtn = findViewById(R.id.create_add_btn);
       // layout = findViewById(R.id.create_linearlayout);
        nextBtn = findViewById(R.id.create_view_match_table);

        addBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);


    }

    public void createPlayer(String name){

        Player player = new Player(name);
        players.add(player);
        playersRef.push().setValue(player);


        //final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //final String textView = new String();
        //textView.setLayoutParams(lparams);
        //textView.setTextSize(32);
        //textView.setTextColor(Color.parseColor("#000000"));


//        for (Player x: players) {
//            textView.equals("Player: " + x.getName().toString());
//
//        }
//
//        return textView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.create_add_btn:
                String playerName = nameInput.getText().toString();
                createPlayer(playerName);
                createdPlayers.add(playerName);
                createAdapter.notifyDataSetChanged();
                //layout.addView(createNameView(nameInput.getText().toString()));
                nameInput.setText("");
                break;
            case R.id.create_view_match_table:
                Intent intent = new Intent(getApplicationContext(), ScheduleActivity.class);
                startActivity(intent);
                break;


        }

    }

}