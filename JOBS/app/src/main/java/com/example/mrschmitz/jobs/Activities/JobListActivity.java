package com.example.mrschmitz.jobs.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.mrschmitz.jobs.R;
import com.example.mrschmitz.jobs.adapters.JobAdapter;
import com.example.mrschmitz.jobs.misc.Constants;
import com.example.mrschmitz.jobs.pojos.Job;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class JobListActivity extends AppCompatActivity {

    @BindView(R.id.viewJobs)
    ListView jobsListView;

    private JobAdapter jobsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);
        ButterKnife.bind(this);

        setupJobsListView();
    }

    private void setupJobsListView() {
        String title = getIntent().getStringExtra(Constants.TITLE);
        setTitle(title);


        ArrayList<Job> jobs = Parcels.unwrap(getIntent().getParcelableExtra(Constants.JOBS));
        jobsAdapter = new JobAdapter(this, jobs);
        jobsListView.setAdapter(jobsAdapter);
        jobsListView.setEmptyView(findViewById(android.R.id.empty));
        jobsListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(view.getContext(), ViewJobActivity.class);

            Job job = jobsAdapter.getItem(position);
            intent.putExtra(Constants.JOBS, Parcels.wrap(job));
            startActivity(intent);
        });
    }

}
