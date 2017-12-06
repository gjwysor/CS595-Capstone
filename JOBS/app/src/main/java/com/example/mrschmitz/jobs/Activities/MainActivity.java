package com.example.mrschmitz.jobs.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.mrschmitz.jobs.Fragments.MapsFragment;
import com.example.mrschmitz.jobs.Fragments.MessagesFragment;
import com.example.mrschmitz.jobs.Fragments.ProfileFragment;
import com.example.mrschmitz.jobs.R;
import com.example.mrschmitz.jobs.database.Users;
import com.example.mrschmitz.jobs.pojos.User;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.navigation)
    BottomNavigationViewEx navigationView;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Users.loadCurrentUser(user -> {
            this.currentUser = user;

            FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), getFragments());
            viewPager.setAdapter(adapter);

            navigationView.setupWithViewPager(viewPager);
            navigationView.enableAnimation(false);
            navigationView.enableItemShiftingMode(false);
            navigationView.enableShiftingMode(false);
            navigationView.setTextVisibility(false);
        });

    }

    private List<Fragment> getFragments(){
        List<Fragment> fragments = new ArrayList<>();

        fragments.add(MapsFragment.newInstance(currentUser));
        fragments.add(MessagesFragment.newInstance(currentUser));
        fragments.add(ProfileFragment.newInstance(currentUser));

        return fragments;
    }

    private class FragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        private FragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }

    }

}
