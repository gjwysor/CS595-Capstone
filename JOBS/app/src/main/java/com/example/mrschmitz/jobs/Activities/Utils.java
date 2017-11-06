package com.example.mrschmitz.jobs.Activities;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import agency.tango.android.avatarview.AvatarPlaceholder;
import agency.tango.android.avatarview.views.AvatarView;

/**
 * Created by Noah on 10/21/2017.
 */

public class Utils {

    public static void loadProfileImage(Context context, AvatarView avatarView) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        GlideApp.with(context)
                .load(user.getPhotoUrl())
                .fitCenter()
                .placeholder(new AvatarPlaceholder(user.getDisplayName()))
                .into(avatarView);
    }

}
