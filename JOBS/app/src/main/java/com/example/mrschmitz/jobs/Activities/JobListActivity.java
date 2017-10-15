package com.example.mrschmitz.jobs.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.mrschmitz.jobs.R;

public class JobListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);
    }

    public void backgoogle(View view){
        Intent intent = new Intent(JobListActivity.this, MapActivity.class);
        startActivity(intent);
    }
}
