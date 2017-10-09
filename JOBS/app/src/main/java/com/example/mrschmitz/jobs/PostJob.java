package com.example.mrschmitz.jobs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PostJob extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);
    }

    public void backToProfile(View view){
        Intent intent = new Intent(PostJob.this, ProfileView.class);
        startActivity(intent);
    }
}
