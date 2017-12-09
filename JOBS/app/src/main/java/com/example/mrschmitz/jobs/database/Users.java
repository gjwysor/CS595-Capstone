package com.example.mrschmitz.jobs.database;

import android.net.Uri;

import com.example.mrschmitz.jobs.misc.Constants;
import com.example.mrschmitz.jobs.pojos.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

/**
 * Created by Noah on 12/1/2017.
 */

public class Users {

    private static CollectionReference usersCollection() {
        return FirebaseFirestore.getInstance().collection(Constants.USERS);
    }

    public static void saveCurrentUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            userExists(firebaseUser.getUid(), userExists -> {
                if (!userExists) {
                    Uri photoUrl = firebaseUser.getPhotoUrl();
                    User user = User.builder()
                            .uniqueId(firebaseUser.getUid())
                            .name(firebaseUser.getDisplayName())
                            .photoUrl(photoUrl == null ? null : photoUrl.toString())
                            .admin(false)
                            .build();

                    usersCollection()
                            .document(firebaseUser.getUid())
                            .set(user);
                }
            });
        }
    }

    public static void userExists(String uid, OnSuccessListener<Boolean> listener) {
        usersCollection()
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    boolean userExists = documentSnapshot.exists();
                    listener.onSuccess(userExists);
                });
    }

    public static void loadUser(String uid, OnSuccessListener<User> listener) {
        usersCollection()
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    listener.onSuccess(user);
                });
    }

    public static void loadCurrentUser(OnSuccessListener<User> listener) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        loadUser(user.getUid(), listener);
    }

    public static void updateUser(User user) {
        usersCollection()
                .document(user.getUniqueId())
                .set(user, SetOptions.merge());
    }

    public static boolean isDifferentUser(User userOne, User userTwo) {
        return isDifferentUser(userOne.getUniqueId(), userTwo.getUniqueId());
    }

    public static boolean isDifferentUser(String uidOne, String uidTwo) {
        return !uidOne.equals(uidTwo);
    }

}
