package com.example.mrschmitz.jobs.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrschmitz.jobs.R;
import com.example.mrschmitz.jobs.pojos.Job;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewJobActivity extends AppCompatActivity {

    @BindView(R.id.job_title)
    TextView jobTitleTextView;

    @BindView(R.id.job_description)
    TextView jobDescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job);
        ButterKnife.bind(this);

        Job job = (Job) getIntent().getSerializableExtra("job");
        jobTitleTextView.setText(job.getTitle());
        jobDescriptionTextView.setText(job.getDescription());
    }

    @OnClick(R.id.apply_for_job)
    public void applyForJob(View view){
        Toast.makeText(this, "Applied for Job!", Toast.LENGTH_SHORT).show();
    }
}
