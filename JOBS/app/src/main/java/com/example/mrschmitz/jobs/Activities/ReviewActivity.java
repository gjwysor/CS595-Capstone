package com.example.mrschmitz.jobs.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mrschmitz.jobs.R;
import com.example.mrschmitz.jobs.adapters.ReviewAdapter;
import com.example.mrschmitz.jobs.database.Reviews;
import com.example.mrschmitz.jobs.database.Users;
import com.example.mrschmitz.jobs.misc.Constants;
import com.example.mrschmitz.jobs.misc.Utils;
import com.example.mrschmitz.jobs.pojos.Review;
import com.example.mrschmitz.jobs.pojos.User;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReviewActivity extends AppCompatActivity {

    @BindView(R.id.reviews)
    ListView reviewsListView;

    @BindView(R.id.writeReview)
    FloatingActionButton writeReviewButton;

    private User reviewedUser, currentUser;
    private ReviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);

        Users.loadCurrentUser(user -> {
            currentUser = user;
            reviewedUser = Parcels.unwrap(getIntent().getParcelableExtra(Constants.USERS));

            setTitle(reviewedUser.getName());
            showHideWriteReviewButton();
            setupReviewsListView();
        });
    }

    private void setupReviewsListView() {
        adapter = new ReviewAdapter(this, new ArrayList<>());
        reviewsListView.setAdapter(adapter);
        reviewsListView.setEmptyView(findViewById(android.R.id.empty));
        reviewsListView.setOnItemClickListener((parent, view, position, id) -> {
            Review review = adapter.getItem(position);
            Utils.viewUserProfile(this, review.getReviewerUid());
        });

        Reviews.loadReviews(reviewedUser, reviews -> adapter.addAll(reviews));
    }

    private void showHideWriteReviewButton() {
        Reviews.canWriteReview(currentUser, reviewedUser, canWriteReview -> {
            if (canWriteReview) {
                writeReviewButton.show();

            } else {
                writeReviewButton.hide();
            }
        });
    }

    @OnClick(R.id.writeReview)
    public void writeReview() {
        View view = getLayoutInflater().inflate(R.layout.dialog_review, null);
        final RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        final EditText reviewEditText = view.findViewById(R.id.review);

        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            float minRating = 1.0f;
            ratingBar.setRating(Math.max(minRating, rating));
        });

        new MaterialDialog.Builder(this)
                .title("Review")
                .customView(view, false)
                .positiveText("Submit")
                .onPositive((dialog, which) -> {
                    Review review = Review.builder()
                            .reviewedUid(reviewedUser.getUniqueId())
                            .reviewerUid(currentUser.getUniqueId())
                            .rating(Math.round(ratingBar.getRating()))
                            .review(reviewEditText.getText().toString())
                            .build();

                    adapter.add(review);
                    writeReviewButton.hide();
                    Reviews.writeReview(review);
                })
                .show();
    }

}
