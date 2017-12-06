package com.example.mrschmitz.jobs.database;

import com.example.mrschmitz.jobs.misc.Constants;
import com.example.mrschmitz.jobs.pojos.Review;
import com.example.mrschmitz.jobs.pojos.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * Created by Noah on 12/1/2017.
 */

public class Reviews {

    private static CollectionReference reviewsCollection() {
        return FirebaseFirestore.getInstance().collection(Constants.REVIEWS);
    }

    public static void loadReviews(User user, OnSuccessListener<List<Review>> listener) {
        reviewsCollection()
                .whereEqualTo("reviewedUid", user.getUniqueId())
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    listener.onSuccess(documentSnapshots.toObjects(Review.class));
                });
    }

    public static void writeReview(Review review) {
        reviewsCollection()
                .add(review);
    }

}
