package com.example.mrschmitz.jobs.Activities;

import android.content.Context;
import android.util.Log;
import com.example.mrschmitz.jobs.Activities.Utilities.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import android.view.Menu;
import android.view.MenuItem;
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

import com.example.mrschmitz.jobs.misc.Utils;
import agency.tango.android.avatarview.views.AvatarView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReviewActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUM = 4;
    private Context mContext = ReviewActivity.this;

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
        Log.d(TAG, "onCreate: starting.");
        setupBottomNavigationView();

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
        Review review = new Review(uid,
                null, //TODO pass in the user that we are reviewing and save to database
                ratingBar.getNumStars(),
                reviewEditText.getText().toString());

        FirebaseFirestore.getInstance()
                .collection(Constants.REVIEWS)
                .add(review);
        finish();
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
