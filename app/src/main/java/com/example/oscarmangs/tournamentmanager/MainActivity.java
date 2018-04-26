package com.example.oscarmangs.tournamentmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn;
    public static String userId = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (userId == null) {
            SharedPreferences sharedPref = this.getSharedPreferences(
                    "KeyValue", Context.MODE_PRIVATE);


            userId = sharedPref.getString("KeyValue", null);

            if (userId == null) {
                userId = UUID.randomUUID().toString();

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("KeyValue", userId);
                editor.commit();
            }
        }
        findComponents();

    }

    public void findComponents(){
        btn = findViewById(R.id.main_button);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), CreateActivity.class);
        startActivity(intent);
    }
}
