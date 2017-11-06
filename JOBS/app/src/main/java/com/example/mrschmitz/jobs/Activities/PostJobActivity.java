package com.example.mrschmitz.jobs.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.mrschmitz.jobs.R;

public class PostJobActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);
    }

    public void backToProfile(View view){
        Intent intent = new Intent(PostJobActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
}
