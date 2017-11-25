package com.example.mrschmitz.jobs.Activities;

import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.util.Log;
import com.example.mrschmitz.jobs.Activities.Utilities.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
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

    private static final String TAG = "JobListActivity";
    private static final int ACTIVITY_NUM = 1;
    private Context mContext = ViewJobActivity.this;

    @BindView(R.id.job_title)
    TextView jobTitleTextView;

    @BindView(R.id.job_description)
    TextView jobDescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job);
        ButterKnife.bind(this);
        Log.d(TAG, "onCreate: starting.");
        setupBottomNavigationView();

        Job job = (Job) getIntent().getSerializableExtra("job");
        jobTitleTextView.setText(job.getTitle());
        jobDescriptionTextView.setText(job.getDesc());
    }

    @OnClick(R.id.apply_for_job)
    public void applyForJob(View view){
        Toast.makeText(this, "Applied for Job!", Toast.LENGTH_SHORT).show();
    }

    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
