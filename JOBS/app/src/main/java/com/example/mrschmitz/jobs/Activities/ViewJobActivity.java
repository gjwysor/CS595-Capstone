package com.example.mrschmitz.jobs.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mrschmitz.jobs.R;
import com.example.mrschmitz.jobs.database.Jobs;
import com.example.mrschmitz.jobs.database.Users;
import com.example.mrschmitz.jobs.misc.Constants;
import com.example.mrschmitz.jobs.misc.Utils;
import com.example.mrschmitz.jobs.pojos.Job;
import com.example.mrschmitz.jobs.pojos.User;
import com.synnapps.carouselview.CarouselView;

import org.parceler.Parcels;

import java.text.NumberFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewJobActivity extends AppCompatActivity {

    private Job job;
    private User poster;

    @BindView(R.id.carouselView)
    CarouselView carouselView;

    @BindView(R.id.job_poster)
    TextView posterTextView;

    @BindView(R.id.job_title)
    TextView titleTextView;

    @BindView(R.id.description)
    TextView descriptionTextView;

    @BindView(R.id.location)
    TextView locationTextView;

    @BindView(R.id.payment)
    TextView paymentAmountTextView;

    @BindView(R.id.apply_for_job)
    Button applyForJobButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job);
        ButterKnife.bind(this);

        job = Parcels.unwrap(getIntent().getParcelableExtra(Constants.JOBS));

        setupImages();
        setupApplyForJobButton();
        Users.loadUser(job.getPosterUid(), user -> {
            poster = user;
            setupJobInfo();
        });

    }

    private void setupImages() {
        List<String> imageUrls = job.getImageUrls();
        if (imageUrls.isEmpty()) {
            carouselView.setVisibility(View.GONE);

        } else {
            carouselView.setPageCount(job.getImageUrls().size());
            carouselView.setImageListener((position, imageView) -> {
                Glide.with(this)
                        .load(job.getImageUrls().get(position))
                        .into(imageView);
            });
        }
    }

    private void setupJobInfo() {
        posterTextView.setText(poster.getName());
        titleTextView.setText(job.getTitle());
        descriptionTextView.setText(job.getDescription());
        locationTextView.setText(job.getAddress());

        String payment = NumberFormat.getCurrencyInstance().format(job.getPaymentAmount());
        if(job.getPaymentType().equals("Hourly")) {
            payment += "/hr";
        }

        boolean paidInCash = job.getPaymentMethod().equals("Cash");
        payment += paidInCash ? " in cash" : " via " + job.getPaymentMethod();

        paymentAmountTextView.setText(payment);
    }

    private void setupApplyForJobButton() {
        Jobs.canApplyForJob(job, canApplyForJob -> {
            int visibility = canApplyForJob ? View.VISIBLE : View.GONE;
            applyForJobButton.setVisibility(visibility);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_job, menu);

        Users.loadCurrentUser(user -> {
            boolean userIsAdmin = user.isAdmin();

            MenuItem flagJobMenuItem = menu.findItem(R.id.flagJob);
            flagJobMenuItem.setVisible(!userIsAdmin);

            MenuItem unflagJobMenuItem = menu.findItem(R.id.unflagJob);
            unflagJobMenuItem.setVisible(userIsAdmin);

            MenuItem deleteJobMenuItem = menu.findItem(R.id.deleteJob);
            Jobs.canDeleteJob(job, deleteJobMenuItem::setVisible);
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.flagJob:
                Jobs.flagJob(job);
                Toast.makeText(this, "Job flagged", Toast.LENGTH_SHORT).show();
                break;

            case R.id.unflagJob:
                Jobs.unflagJob(job);
                Toast.makeText(this, "Job unflagged", Toast.LENGTH_SHORT).show();
                break;

            case R.id.deleteJob:
                Jobs.deleteJob(job);
                Toast.makeText(this, "Job deleted", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.job_poster)
    public void jobPoster(View view){
        Utils.viewUserProfile(this, poster);
    }

    @OnClick(R.id.apply_for_job)
    public void applyForJob(View view){
        Toast.makeText(this, "Applied for Job!", Toast.LENGTH_SHORT).show();
    }

}
