package com.example.mrschmitz.jobs.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mrschmitz.jobs.R;
import com.example.mrschmitz.jobs.adapters.JobAdapter;
import com.example.mrschmitz.jobs.misc.Constants;
import com.example.mrschmitz.jobs.pojos.Job;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class JobListActivity extends AppCompatActivity {

    @BindView(R.id.viewJobs)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);
        ButterKnife.bind(this);

        final JobAdapter adapter = new JobAdapter(this, new ArrayList<Job>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), ViewJobActivity.class);

                Job job = adapter.getItem(position);
                intent.putExtra("job", job);
                startActivity(intent);
            }
        });

        FirebaseFirestore.getInstance()
                .collection(Constants.JOBS)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                            if (documentSnapshot.exists()) {
                                adapter.add(documentSnapshot.toObject(Job.class));
                            }
                        }
                    }
                });
    }

    @OnClick(R.id.back2google)
    public void back2google(View view){
        startActivity(new Intent(JobListActivity.this, MapActivity.class));
    }

}
