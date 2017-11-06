package com.example.mrschmitz.jobs.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mrschmitz.jobs.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import agency.tango.android.avatarview.views.AvatarView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReviewActivity extends AppCompatActivity {

    @BindView(R.id.avatar)
    AvatarView avatarView;

    @BindView(R.id.username)
    TextView usernameTextView;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);

        Utils.loadProfileImage(this, avatarView);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        usernameTextView.setText(user.getDisplayName());

        // Force ratings of one star or more
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rating < 1.0f) {
                    ratingBar.setRating(1.0f);
                }
            }
        });
    }

    @OnClick(R.id.submit)
    public void submit() {
        finish();
    }

}
