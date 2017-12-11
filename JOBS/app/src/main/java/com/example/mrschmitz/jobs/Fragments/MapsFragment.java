package com.example.mrschmitz.jobs.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.mrschmitz.jobs.Activities.JobListActivity;
import com.example.mrschmitz.jobs.Activities.PostJobActivity;
import com.example.mrschmitz.jobs.Activities.ViewJobActivity;
import com.example.mrschmitz.jobs.R;
import com.example.mrschmitz.jobs.database.Jobs;
import com.example.mrschmitz.jobs.database.Users;
import com.example.mrschmitz.jobs.misc.Constants;
import com.example.mrschmitz.jobs.pojos.Job;
import com.example.mrschmitz.jobs.pojos.User;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.commons.lang3.StringUtils;
import org.parceler.Parcels;

import java.text.NumberFormat;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.OnClick;

/**
 * Created by Noah on 11/29/2017.
 */
public class MapsFragment extends CurrentLocationMapsFragment implements GoogleMap.OnInfoWindowClickListener {

    private User user;
    private GoogleMap map;
    private List<Job> openJobs;

    @OnClick(R.id.post_job)
    public void postJob() {
        startActivity(new Intent(getContext(), PostJobActivity.class));
    }

    public static MapsFragment newInstance(User user) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.USERS, Parcels.wrap(user));
        fragment.setArguments(args);
        return fragment;
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
    public void onMapReady(GoogleMap map) {
        super.onMapReady(map);
        this.map = map;
        setMapOptions();
        loadAndShowJobs();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAndShowJobs();
    }

    private void setMapOptions() {
        map.getUiSettings().setRotateGesturesEnabled(false);
        map.getUiSettings().setTiltGesturesEnabled(false);
        map.getUiSettings().setCompassEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.setOnInfoWindowClickListener(this);
    }

    private List<Job> searchOpenJobsByTitle(final String title) {
        return openJobs.stream()
                .filter(job -> StringUtils.containsIgnoreCase(job.getTitle(), title))
                .collect(Collectors.toList());
    }

    private void loadAndShowJobs() {
        Jobs.loadOpenJobs(jobs -> {
            openJobs = jobs;
            showJobs(openJobs);
        });
    }

    private void showJobs(List<Job> jobs) {
        addMapMarkers(jobs);
        animateCamera(jobs);
    }

    private void addMapMarkers(List<Job> jobs) {
        map.clear();
        for (Job job: jobs) {
            LatLng position = new LatLng(job.getLatitude(), job.getLongitude());
            String paymentAmount = NumberFormat.getCurrencyInstance().format(job.getPaymentAmount());
            if(job.getPaymentType().equals("Hourly")) {
                paymentAmount += "/hr";
            }

            boolean isNotJobPoster = Users.isDifferentUser(user.getUniqueId(), job.getPosterUid());
            float markerColor = isNotJobPoster ? BitmapDescriptorFactory.HUE_ORANGE : BitmapDescriptorFactory.HUE_RED;
            MarkerOptions markerOptions = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(markerColor))
                    .position(position)
                    .title(job.getTitle())
                    .snippet(paymentAmount);

            map.addMarker(markerOptions).setTag(job);
        }
    }

    private void animateCamera(List<Job> jobs) {
        LatLngBounds.Builder bounds = LatLngBounds.builder();

        bounds.include(getLastKnownPosition());
        for (Job job: jobs) {
            LatLng position = new LatLng(job.getLatitude(), job.getLongitude());
            bounds.include(position);
        }

        int padding = 200;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds.build(), padding);
        map.animateCamera(cameraUpdate);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(getContext(), ViewJobActivity.class);

        Job job = (Job) marker.getTag();
        intent.putExtra(Constants.JOBS, Parcels.wrap(job));
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_maps, menu);

        final MenuItem flaggedJobsMenuItem = menu.findItem(R.id.flaggedJobs);
        if (user.isAdmin()) {
            flaggedJobsMenuItem.setVisible(true);
        }

        final MenuItem searchMenuItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String title) {
                if (StringUtils.isBlank(title)) {
                    showJobs(openJobs);

                } else {
                    showJobs(searchOpenJobsByTitle(title));
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        searchView.setOnCloseListener(() -> {
            showJobs(openJobs);
            return false;
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.flaggedJobs:
                Jobs.loadFlaggedJobs(flaggedJobs -> {
                    Intent intent = new Intent(getContext(), JobListActivity.class);
                    intent.putExtra(Constants.TITLE, "Flagged Jobs");
                    intent.putExtra(Constants.JOBS, Parcels.wrap(flaggedJobs));
                    startActivity(intent);
                });
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
