package com.example.mrschmitz.jobs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ProfileView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
    }

    public void googleBack(View view){
        Intent intent = new Intent(ProfileView.this, GoogleView.class);
        startActivity(intent);
    }

    public void postjob1(View view){
        Intent intent = new Intent(ProfileView.this, PostJob.class);
        startActivity(intent);
    }
}
