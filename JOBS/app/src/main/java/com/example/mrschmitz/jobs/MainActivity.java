package com.example.mrschmitz.jobs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void butt(View view){
        Intent intent = new Intent(MainActivity.this, GoogleView.class);
        startActivity(intent);
    }
}


