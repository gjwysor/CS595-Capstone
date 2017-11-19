package com.example.mrschmitz.jobs.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mrschmitz.jobs.R;
import com.example.mrschmitz.jobs.misc.Constants;
import com.example.mrschmitz.jobs.misc.Utils;
import com.example.mrschmitz.jobs.pojos.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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

    @BindView(R.id.review)
    EditText reviewEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);

        Utils.loadProfileImage(this, avatarView);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            usernameTextView.setText(user.getDisplayName());
        }

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
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Review review = Review.builder()
                .reviewerUid(uid)
                .reviewedUid(null) // TODO pass in the user that we are reviewing and save to database
                .review(reviewEditText.getText().toString())
                .rating(ratingBar.getNumStars())
                .build();

        FirebaseFirestore.getInstance()
                .collection(Constants.REVIEWS)
                .add(review);
        finish();
    }

}
