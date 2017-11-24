package com.example.mrschmitz.jobs.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mrschmitz.jobs.Activities.Utilities.BottomNavigationViewHelper;

import com.example.mrschmitz.jobs.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.Serializable;

import static com.example.mrschmitz.jobs.R.layout.activity_post_job;

public class PostJobActivity extends AppCompatActivity {

    public static class jobOb implements Serializable {

        private String jobTitle;
        private String jobSkills;
        private String jobDesc;

        public jobOb(){}

        public jobOb(String title, String skills, String desc){
            jobTitle = title;
            jobSkills = skills;
            jobDesc = desc;
        }

        public String getTitle(){
            return jobTitle;
        }

        public String getSkills(){
            return jobSkills;
        }

        public String getDesc(){
            return jobDesc;
        }

        public void setTitle(String title){
            jobTitle = title;
        }

        public void setSkills(String skills){
            jobSkills = skills;
        }

        public void setDesc(String desc){
            jobDesc = desc;
        }
    }

    private static final String TAG = "PostJobActivity";
    private static final int ACTIVITY_NUM = 2;

    private Context mContext = PostJobActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);

        Log.d(TAG, "onCreate: starting.");

        setupBottomNavigationView();
    }



    public void backToProfile(View view){
        startActivity(new Intent(PostJobActivity.this, ProfileActivity.class));
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

    public void postJob(View view){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        EditText title = findViewById(R.id.editText3);
        EditText skills = findViewById(R.id.editText4);
        EditText desc = findViewById(R.id.editText5);

        jobOb refJob = new jobOb(title.getText().toString(), skills.getText().toString(), desc.getText().toString());

        myRef.child("Jobs").push().setValue(refJob);

        Context context = getApplicationContext();

        Toast.makeText(context, "Job Posted!",
                Toast.LENGTH_SHORT).show();
    }

}
