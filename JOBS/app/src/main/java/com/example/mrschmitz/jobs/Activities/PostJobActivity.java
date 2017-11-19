package com.example.mrschmitz.jobs.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mrschmitz.jobs.R;
import com.example.mrschmitz.jobs.misc.Constants;
import com.example.mrschmitz.jobs.pojos.Job;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.mrschmitz.jobs.R.layout.activity_post_job;

public class PostJobActivity extends AppCompatActivity {


    @BindView(R.id.job_title)
    EditText titleEditText;

    @BindView(R.id.skills)
    EditText skillsEditText;

    @BindView(R.id.description)
    EditText descriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_post_job);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.post_job)
    public void postJob() {
        Job job = Job.builder()
                .posterUid(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .title(titleEditText.getText().toString())
                .skills(skillsEditText.getText().toString())
                .description(descriptionEditText.getText().toString())
                .finished(false)
                .build();

        FirebaseFirestore.getInstance().collection(Constants.JOBS).add(job)
        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                String message = task.isComplete() ? "Job posted!" : "Error posting job";
                Toast.makeText(PostJobActivity.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        startActivity(new Intent(PostJobActivity.this, JobListActivity.class));
    }

}
