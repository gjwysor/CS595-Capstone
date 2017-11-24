package com.example.mrschmitz.jobs.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrschmitz.jobs.Activities.Utilities.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.example.mrschmitz.jobs.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class JobListActivity extends AppCompatActivity {
    private static final String TAG = "JobListActivity";
    private static final int ACTIVITY_NUM = 1;

    private Context mContext = JobListActivity.this;

    ArrayList<String> jobList;
    DatabaseReference myRef;
    String[] jobTitles;
    ChildEventListener jobListener;
    ListView listView;
    ArrayAdapter adapter;
    AdapterView.OnItemClickListener onClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);

        jobList = new ArrayList<>();
        jobTitles = new String[jobList.size()];

        myRef = FirebaseDatabase.getInstance().getReference().child("Jobs");

        adapter = new ArrayAdapter<>(this,
                R.layout.list_item, jobList);

        listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        setupBottomNavigationView();

        onClick = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String sendMe = (String) adapter.getItem(position);
                Intent intent = new Intent(view.getContext(), ViewJobDetailsActivity.class);
                intent.putExtra("getMe", sendMe);
                startActivity(intent);

            }
        };

        listView.setOnItemClickListener(onClick);

        jobListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                // A new job has been added, add it to the displayed list
                PostJobActivity.jobOb job = dataSnapshot.getValue(PostJobActivity.jobOb.class);

                adapter.add(job.getTitle());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                // A job has changed, use the key to determine if we are displaying this
                // job and if so displayed the changed comment.
                PostJobActivity.jobOb job = dataSnapshot.getValue(PostJobActivity.jobOb.class);
                String jobKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // A job has changed, use the key to determine if we are displaying this
                // job and if so remove it.
                PostJobActivity.jobOb job = dataSnapshot.getValue(PostJobActivity.jobOb.class);
                String jobKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                // A job has changed position, use the key to determine if we are
                // displaying this job and if so move it.
                PostJobActivity.jobOb job = dataSnapshot.getValue(PostJobActivity.jobOb.class);
                String jobKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Context toastContext = getApplicationContext();

                Toast.makeText(toastContext, "Failed to load Jobs.",
                        Toast.LENGTH_SHORT).show();
            }
        };

        myRef.addChildEventListener(jobListener);

    }

    public void backgoogle(View view){
        Intent intent = new Intent(JobListActivity.this, MapsActivity.class);
        startActivity(intent);
    }

    /** BottomNavigationView setup*/

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
