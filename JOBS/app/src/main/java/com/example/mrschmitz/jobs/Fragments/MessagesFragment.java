package com.example.mrschmitz.jobs.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mrschmitz.jobs.R;
import com.example.mrschmitz.jobs.misc.Constants;
import com.example.mrschmitz.jobs.pojos.User;

import org.parceler.Parcels;

public class MessagesFragment extends Fragment {

    private User user;

    public MessagesFragment() {
        // Required empty public constructor
    }

    public static MessagesFragment newInstance(User user) {
        MessagesFragment fragment = new MessagesFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.USERS, Parcels.wrap(user));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = Parcels.unwrap(getArguments().getParcelable(Constants.USERS));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

}
