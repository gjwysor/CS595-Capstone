package com.example.mrschmitz.jobs.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.mrschmitz.jobs.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    public void googleBack(View view){
        Intent intent = new Intent(ProfileActivity.this, MapActivity.class);
        startActivity(intent);
    }

    public void postjob1(View view){
        Intent intent = new Intent(ProfileActivity.this, PostJobActivity.class);
        startActivity(intent);
    }

    public void review(View view){
        Intent intent = new Intent(ProfileActivity.this, ReviewActivity.class);
        startActivity(intent);
    }
}
