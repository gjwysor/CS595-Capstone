package com.example.mrschmitz.jobs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ListView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
    }

    public void backgoogle(View view){
        Intent intent = new Intent(ListView.this, GoogleView.class);
        startActivity(intent);
    }
}
