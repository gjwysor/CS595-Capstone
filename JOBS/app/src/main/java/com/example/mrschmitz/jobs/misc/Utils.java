package com.example.mrschmitz.jobs.misc;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.mrschmitz.jobs.Activities.ProfileActivity;
import com.example.mrschmitz.jobs.Activities.SplashActivity;
import com.example.mrschmitz.jobs.database.Users;
import com.example.mrschmitz.jobs.pojos.User;
import com.firebase.ui.auth.AuthUI;

import org.parceler.Parcels;

import agency.tango.android.avatarview.AvatarPlaceholder;
import agency.tango.android.avatarview.views.AvatarView;

/**
 * Created by Noah on 10/21/2017.
 */
public class Utils {

    public static void loadProfileImage(Context context, User user, AvatarView avatarView) {
        GlideApp.with(context)
                .load(user.getPhotoUrl())
                .fitCenter()
                .placeholder(new AvatarPlaceholder(user.getName()))
                .into(avatarView);
    }

    public static void signOutCurrentUser(final Context context, FragmentActivity activity) {
        AuthUI.getInstance()
                .signOut(activity)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        context.startActivity(new Intent(context, SplashActivity.class));
                    } else {
                        Toast.makeText(context, "Sign out failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void deleteCurrentUser(final Context context, final FragmentActivity activity) {
        new AlertDialog.Builder(context)
                .setMessage("Are you sure you want to delete this account?")
                .setPositiveButton("Yes, delete it!", (dialog, which) -> {
                    AuthUI.getInstance()
                            .delete(activity)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    context.startActivity(new Intent(context, SplashActivity.class));
                                } else {
                                    Toast.makeText(context, "Delete account failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                })
                .setNegativeButton("No", null)
                .show();
    }

    public static void viewUserProfile(Context context, String uid) {
        Users.loadUser(uid, user -> viewUserProfile(context, user));
    }

    public static void viewUserProfile(Context context, User user) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(Constants.USERS, Parcels.wrap(user));
        context.startActivity(intent);
    }

}
