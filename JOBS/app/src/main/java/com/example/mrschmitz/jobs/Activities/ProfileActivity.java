package com.example.mrschmitz.jobs.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mrschmitz.jobs.Activities.Utilities.BottomNavigationViewHelper;
import com.example.mrschmitz.jobs.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import agency.tango.android.avatarview.views.AvatarView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity {

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
    TextView jobsInProgressTextView;

    @BindView(R.id.average_rating)
    TextView averageRatingTextView;

    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUM = 4;

    private Context mContext = ProfileActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Log.d(TAG, "onCreate: starting.");

        setupBottomNavigationView();

        ButterKnife.bind(this);

        Utils.loadProfileImage(this, avatarView);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        usernameTextView.setText(user.getDisplayName());
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
                        boolean inputIsEmpty = input.toString().trim().isEmpty();
                        bioTextView.setText(inputIsEmpty ? "¯\\_(ツ)_/¯" : input);
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
