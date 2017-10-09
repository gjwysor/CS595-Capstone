package com.example.mrschmitz.jobs;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class HomeActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void butt(View view){
        Intent intent = new Intent(HomeActivity.this, GoogleView.class);
        startActivity(intent);
    }
}


