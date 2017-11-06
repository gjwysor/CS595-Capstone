package com.example.mrschmitz.jobs.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.mrschmitz.jobs.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import static com.example.mrschmitz.jobs.R.layout.activity_post_job;

public class PostJobActivity extends AppCompatActivity {
    public static class jobOb{
        private String jobTitle;
        private String jobSkills;
        private String jobDesc;

        private jobOb(){
            jobTitle = null;
            jobSkills = null;
            jobDesc = null;
        }

        private jobOb(String title, String skills, String desc){
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_post_job);
    }



    public void backToProfile(View view){
        startActivity(new Intent(PostJobActivity.this, ProfileActivity.class));
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
      }
}
