package com.example.mrschmitz.jobs.database;

import android.net.Uri;

import com.example.mrschmitz.jobs.misc.Constants;
import com.example.mrschmitz.jobs.pojos.Job;
import com.example.mrschmitz.jobs.pojos.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Noah on 12/1/2017.
 */

public class Jobs {

    private static CollectionReference jobsCollection() {
        return FirebaseFirestore.getInstance().collection(Constants.JOBS);
    }

    public static void loadOpenJobs(OnSuccessListener<List<Job>> listener) {
        jobsCollection()
                .whereEqualTo("workerUid", null)
                .whereEqualTo("finished", false)
                .whereEqualTo("flagged", false)
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    listener.onSuccess(documentSnapshots.toObjects(Job.class));
                });
    }

    public static void loadPastJobs(User user, OnSuccessListener<List<Job>> listener) {
        jobsCollection()
                .whereEqualTo("workerUid", user.getUniqueId())
                .whereEqualTo("finished", true)
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    listener.onSuccess(documentSnapshots.toObjects(Job.class));
                });
    }

    public static void loadInProgressJobs(User user, OnSuccessListener<List<Job>> listener) {
        jobsCollection()
                .whereEqualTo("workerUid", user.getUniqueId())
                .whereEqualTo("finished", false)
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    listener.onSuccess(documentSnapshots.toObjects(Job.class));
                });
    }

    public static void loadFlaggedJobs(OnSuccessListener<List<Job>> listener) {
        jobsCollection()
                .whereEqualTo("flagged", true)
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    listener.onSuccess(documentSnapshots.toObjects(Job.class));
                });
    }

    public static void flagJob(Job job) {
        // TODO
    }

    public static void unflagJob(Job job) {
        // TODO
    }

    public static void deleteJob(Job job) {
        // TODO
    }


    public static void postJob(Job job, List<Uri> images, OnCompleteListener<DocumentReference> listener) {
        uploadJobImages(images, downloadUrls -> {
            job.setImageUrls(downloadUrls);
            jobsCollection()
                    .add(job)
                    .addOnCompleteListener(listener);
        });
    }

    private static void uploadJobImages(List<Uri> images, OnSuccessListener<List<String>> listener) {
        recursivelyUploadJobImages(images, new ArrayList<>(), 0, listener);
    }

    private static void recursivelyUploadJobImages(List<Uri> images, List<String> downloadUrls, int i, OnSuccessListener<List<String>> listener) {
        if (i < images.size()) {
            uploadJobImage(images.get(i), downloadUrl -> {
                downloadUrls.add(downloadUrl);
                recursivelyUploadJobImages(images, downloadUrls, i + 1, listener);
            });

        } else {
            listener.onSuccess(downloadUrls);
        }
    }

    private static void uploadJobImage(Uri image, OnSuccessListener<String> listener) {
        FirebaseStorage.getInstance().getReference()
                .child(UUID.randomUUID().toString())
                .putFile(image)
                .addOnSuccessListener(taskSnapshot -> {
                    listener.onSuccess(taskSnapshot.getDownloadUrl().toString());
                });
    }

}
