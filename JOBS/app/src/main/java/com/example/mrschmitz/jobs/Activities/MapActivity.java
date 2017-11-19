package com.example.mrschmitz.jobs.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.mrschmitz.jobs.R;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Button buttonList = findViewById(R.id.buttonList);
        buttonList.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, JobListActivity.class);
                startActivity(intent);
            }
        });

        Button buttonProfile = findViewById(R.id.buttonProfile);
        buttonProfile.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
