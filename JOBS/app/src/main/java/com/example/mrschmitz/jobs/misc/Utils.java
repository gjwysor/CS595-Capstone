package com.example.mrschmitz.jobs.misc;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.mrschmitz.jobs.pojos.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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

    public static void getCurrentUser(GetUserListener listener) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        getUser(user.getUid(), listener);
    }

    public static void getUser(String uid, final GetUserListener listener) {
        FirebaseFirestore.getInstance().collection("Users")
                .whereEqualTo("uniqueId", uid)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            User user = task.getResult().toObjects(User.class).get(0);
                            listener.onSuccess(user);

                        } else {
                            listener.onFailed();
                        }
                    }
                });
    }

    public interface GetUserListener {
        public void onSuccess(User user);
        public void onFailed();
    }

}
