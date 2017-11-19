package com.example.mrschmitz.jobs.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrschmitz.jobs.R;

public class ViewJobDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_job_details);

        String getMe = (String) getIntent().getSerializableExtra("getMe");
        TextView jobTitle = findViewById(R.id.detailedJobTitle);
        jobTitle.setText(getMe);

    }

    public void jobApply(View view){
        Context context = getApplicationContext();
        Toast.makeText(context, "Applied for Job!",
                Toast.LENGTH_SHORT).show();
    }
}
