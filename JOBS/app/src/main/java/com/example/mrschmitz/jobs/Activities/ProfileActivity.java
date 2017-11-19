package com.example.mrschmitz.jobs.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mrschmitz.jobs.R;
import com.example.mrschmitz.jobs.misc.Constants;
import com.example.mrschmitz.jobs.misc.Utils;
import com.example.mrschmitz.jobs.pojos.Review;
import com.example.mrschmitz.jobs.pojos.User;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;

import agency.tango.android.avatarview.views.AvatarView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity {

    private static final String NO_BIO = "¯\\_(ツ)_/¯";

    @BindView(android.R.id.content)
    View rootView;

    @BindView(R.id.avatar)
    AvatarView avatarView;

    @BindView(R.id.username)
    TextView usernameTextView;

    @BindView(R.id.bio)
    TextView bioTextView;

    @BindView(R.id.previous_jobs)
    TextView previousJobsTextView;

    @BindView(R.id.jobs_in_progress)
    TextView inProgressJobsTextView;

    @BindView(R.id.average_rating)
    TextView averageRatingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        loadAvatarAndNameAndBio();
        loadJobsCount(previousJobsTextView, true);
        loadJobsCount(inProgressJobsTextView, false);
        loadAverageRating();
    }

    private void loadAvatarAndNameAndBio() {
        Utils.loadProfileImage(this, avatarView);
        Utils.getCurrentUser(new Utils.GetUserListener() {
            @Override
            public void onSuccess(User user) {
                usernameTextView.setText(user.getName());

                String bio = user.getBio();
                bioTextView.setText(StringUtils.isBlank(bio) ? NO_BIO : bio);
            }

            @Override
            public void onFailed() {
                Toast.makeText(ProfileActivity.this, "Error loading user", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void loadJobsCount(final TextView textView, boolean finished) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance().collection(Constants.JOBS)
                .whereEqualTo("workerUid", uid)
                .whereEqualTo("finished", finished)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String count = task.isSuccessful() ? Integer.toString(task.getResult().size()) : "ERROR";
                        textView.setText(count);
                    }
                });
    }

    private void loadAverageRating() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection(Constants.REVIEWS)
                .whereEqualTo("reviewedUid", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String averageRating;
                        if (task.isSuccessful()) {
                            int numRatings = task.getResult().size();
                            if (numRatings > 0) {
                                int ratingSum = 0;
                                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                    Review review = documentSnapshot.toObject(Review.class);
                                    ratingSum += review.getRating();
                                }

                                DecimalFormat decimalFormat = new DecimalFormat();
                                decimalFormat.setMaximumFractionDigits(1);
                                averageRating = decimalFormat.format((double) ratingSum / numRatings);
                            } else {
                                averageRating = "-";
                            }

                        } else {
                            averageRating = "ERROR";
                        }

                        averageRatingTextView.setText(averageRating);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                signOut();
                break;

            case R.id.delete_account:
                deleteAccount();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(ProfileActivity.this, SplashActivity.class));
                            finish();
                        } else {
                            Snackbar.make(rootView, "Sign out failed", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void deleteAccount() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to delete this account?")
                .setPositiveButton("Yes, delete it!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AuthUI.getInstance()
                                .delete(ProfileActivity.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            startActivity(new Intent(ProfileActivity.this, SplashActivity.class));
                                            finish();
                                        } else {
                                            Snackbar.make(rootView, "Delete account failed", Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @OnClick(R.id.bio_card)
    public void bioCard() {
        new MaterialDialog.Builder(this)
                .title("Edit Bio")
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                .input("Some info about you...", bioTextView.getText(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        bioTextView.setText(StringUtils.isBlank(input) ? NO_BIO : input);

                        // TODO update bio in database
                    }
                }).show();
    }

    @OnClick(R.id.previous_jobs_card)
    public void previousJobsCard() {
        Toast.makeText(this, "Previous Jobs", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.jobs_in_progress_card)
    public void jobsInProgressCard() {
        Toast.makeText(this, "Jobs In Progress", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.average_rating_card)
    public void averageRatingCard() {
        startActivity(new Intent(ProfileActivity.this, ReviewActivity.class));
    }
    
    @OnClick(R.id.post_job_button)
    public void goToPost() {		
        startActivity(new Intent(ProfileActivity.this, PostJobActivity.class));		
    }

}
