package com.example.mrschmitz.jobs.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mrschmitz.jobs.Fragments.ProfileFragment;
import com.example.mrschmitz.jobs.R;
import com.example.mrschmitz.jobs.misc.Constants;
import com.example.mrschmitz.jobs.pojos.User;

import org.parceler.Parcels;

import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        User user = Parcels.unwrap(getIntent().getParcelableExtra(Constants.USERS));

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, ProfileFragment.newInstance(user))
                .commit();
    }
}
