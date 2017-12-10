package com.example.mrschmitz.jobs.database;

import com.example.mrschmitz.jobs.misc.Constants;
import com.example.mrschmitz.jobs.pojos.Review;
import com.example.mrschmitz.jobs.pojos.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.OptionalDouble;

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

    public static void loadAverageRating(User user, OnSuccessListener<OptionalDouble> listener) {
        loadReviews(user, reviews -> {
            OptionalDouble averageRating = reviews
                    .stream()
                    .mapToDouble(Review::getRating)
                    .average();

            listener.onSuccess(averageRating);
        });
    }

    public static void writeReview(Review review) {
        reviewsCollection()
                .add(review);
    }

    public static void canWriteReview(User reviewerUser, User reviewedUser, OnSuccessListener<Boolean> listener) {
        if (Users.isDifferentUser(reviewerUser, reviewedUser)) {
            hasWrittenReview(reviewerUser, reviewedUser, hasWrittenReview -> {
                Jobs.workedForEachOther(reviewerUser, reviewedUser, workedForEachOther -> {
                    boolean canWriteReview = !hasWrittenReview && workedForEachOther;
                    listener.onSuccess(canWriteReview);
                });
            });
        }

    }

    public static void hasWrittenReview(User reviewerUser, User reviewedUser, OnSuccessListener<Boolean> listener) {
        reviewsCollection()
                .whereEqualTo("reviewerUid", reviewerUser.getUniqueId())
                .whereEqualTo("reviewedUid", reviewedUser.getUniqueId())
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    boolean hasWrittenReview = !documentSnapshots.isEmpty();
                    listener.onSuccess(hasWrittenReview);
                });
    }

}
