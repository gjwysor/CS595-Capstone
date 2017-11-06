package com.example.mrschmitz.jobs.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.mrschmitz.jobs.Activities.Utilities.BottomNavigationViewHelper;
import com.example.mrschmitz.jobs.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;


public class JobListActivity extends AppCompatActivity {
    private static final String TAG = "JobListActivity";
    private static final int ACTIVITY_NUM = 1;

    private Context mContext = JobListActivity.this;

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);
        Log.d(TAG, "onCreate: started.");

        listView = findViewById(R.id.list_view);

        ArrayList<String> theList = new ArrayList<>();

        theList.add("Clean my gutters");
        theList.add("Rotate my tires!");
        theList.add("PLZ help me move!");
        theList.add("Volunteer for cleaning the rivers");
        theList.add("Cut my grass.");

        ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, theList);

        listView.setAdapter(listAdapter);

        setupBottomNavigationView();
    }

    public void backgoogle(View view){
        Intent intent = new Intent(JobListActivity.this, MapActivity.class);
        startActivity(intent);
    }
    /**
     * BottomNavigationView setup
     */
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
