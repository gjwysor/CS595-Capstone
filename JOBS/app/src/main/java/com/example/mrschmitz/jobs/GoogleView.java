package com.example.mrschmitz.jobs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class GoogleView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_view);
    }

    public void list(View view){
        Intent intent = new Intent(GoogleView.this, ListView.class);
        startActivity(intent);
    }

    public void profile(View view){
        Intent intent = new Intent(GoogleView.this, ProfileView.class);
        startActivity(intent);
    }
}
