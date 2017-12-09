package com.example.mrschmitz.jobs.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mrschmitz.jobs.Activities.JobListActivity;
import com.example.mrschmitz.jobs.Activities.ReviewActivity;
import com.example.mrschmitz.jobs.R;
import com.example.mrschmitz.jobs.database.Jobs;
import com.example.mrschmitz.jobs.database.Reviews;
import com.example.mrschmitz.jobs.database.Users;
import com.example.mrschmitz.jobs.misc.Constants;
import com.example.mrschmitz.jobs.misc.Utils;
import com.example.mrschmitz.jobs.pojos.Job;
import com.example.mrschmitz.jobs.pojos.User;

import org.apache.commons.lang3.StringUtils;
import org.parceler.Parcels;

import java.text.DecimalFormat;
import java.util.List;

import agency.tango.android.avatarview.views.AvatarView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileFragment extends Fragment {

    private static final String NO_BIO = "None";
    private User user;
    private List<Job> pastJobs, inProgressJobs;

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

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(User user) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.USERS, Parcels.wrap(user));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            user = Parcels.unwrap(getArguments().getParcelable(Constants.USERS));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                Utils.signOutCurrentUser(getContext(), getActivity());
                break;

            case R.id.delete_account:
                Utils.deleteCurrentUser(getContext(), getActivity());
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAvatarAndNameAndBio();
        loadJobs();
        loadAverageRating();
    }

    private void loadAvatarAndNameAndBio() {
        String username = user.getName();
        if (user.isAdmin()) {
            username += " - Admin";
        }

        usernameTextView.setText(username);
        setBioTextView(user.getBio());
        Utils.loadProfileImage(getContext(), user, avatarView);
    }

    private void loadJobs() {
        Jobs.loadPastJobs(user, pastJobs -> {
            this.pastJobs = pastJobs;
            String numPastJobs = Integer.toString(pastJobs.size());
            previousJobsTextView.setText(numPastJobs);
        });

        Jobs.loadInProgressJobs(user, inProgressJobs -> {
            this.inProgressJobs = inProgressJobs;
            String numInProgressJobs = Integer.toString(inProgressJobs.size());
            inProgressJobsTextView.setText(numInProgressJobs);
        });
    }

    private void loadAverageRating() {
        Reviews.loadAverageRating(user, averageRating -> {
            if (averageRating.isPresent()) {
                averageRatingTextView.setText(formatRating(averageRating.getAsDouble()));
            }
        });
    }

    private String formatRating(double rating) {
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(1);
        return decimalFormat.format(rating);
    }

    private void setBioTextView(String bio) {
        bioTextView.setText(StringUtils.isBlank(bio) ? NO_BIO : bio);
    }

    @OnClick(R.id.bio_card)
    public void bioCard() {
        new MaterialDialog.Builder(getContext())
                .title("Edit Bio")
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                .input("Some info about you...", user.getBio(), (dialog, input) -> {
                    String bio = input.toString();
                    setBioTextView(bio);
                    user.setBio(bio);
                    Users.updateUser(user);
                }).show();
    }

    @OnClick(R.id.previous_jobs_card)
    public void previousJobsCard() {
        Intent intent = new Intent(getContext(), JobListActivity.class);
        intent.putExtra(Constants.TITLE, "Previous Jobs");
        intent.putExtra(Constants.JOBS, Parcels.wrap(pastJobs));
        startActivity(intent);
    }

    @OnClick(R.id.jobs_in_progress_card)
    public void jobsInProgressCard() {
        Intent intent = new Intent(getContext(), JobListActivity.class);
        intent.putExtra(Constants.TITLE, "Jobs In Progress");
        intent.putExtra(Constants.JOBS, Parcels.wrap(inProgressJobs));
        startActivity(intent);
    }

    @OnClick(R.id.average_rating_card)
    public void averageRatingCard() {
        Intent intent = new Intent(getContext(), ReviewActivity.class);
        intent.putExtra(Constants.USERS, Parcels.wrap(user));
        startActivity(intent);
    }

}
