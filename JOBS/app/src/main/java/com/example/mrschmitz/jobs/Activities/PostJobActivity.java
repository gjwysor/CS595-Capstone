package com.example.mrschmitz.jobs.Activities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;
import com.example.mrschmitz.jobs.Activities.Utilities.BottomNavigationViewHelper;

import com.example.mrschmitz.jobs.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
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
    private static final String TAG = "PostJobActivity";
    private static final int ACTIVITY_NUM = 2;
    private Context mContext = PostJobActivity.this;

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
        setupBottomNavigationView();
    }

    @OnClick(R.id.post_job)
    public void postJob() {
        Job job = new Job(
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                titleEditText.getText().toString(),
                skillsEditText.getText().toString(),
                descriptionEditText.getText().toString(),
                false);

        FirebaseFirestore.getInstance().collection(Constants.JOBS).add(job)
        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                String message = task.isComplete() ? "Job posted!" : "Error posting job";
                Toast.makeText(PostJobActivity.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        startActivity(new Intent(PostJobActivity.this, HomeActivity.class));
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
