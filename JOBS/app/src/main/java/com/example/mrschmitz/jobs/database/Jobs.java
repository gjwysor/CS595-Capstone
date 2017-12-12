package com.example.mrschmitz.jobs.database;

import android.net.Uri;

import com.example.mrschmitz.jobs.misc.Constants;
import com.example.mrschmitz.jobs.misc.Utils;
import com.example.mrschmitz.jobs.pojos.Job;
import com.example.mrschmitz.jobs.pojos.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
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

    public static void canApplyForJob(Job job, OnSuccessListener<Boolean> listener) {
        Users.loadCurrentUser(user -> {
            boolean userIsNotPoster = Users.isDifferentUser(user.getUniqueId(), job.getPosterUid());
            boolean jobIsOpen = job.getWorkerUid() == null;
            boolean canApplyForJob = userIsNotPoster && jobIsOpen;
            listener.onSuccess(canApplyForJob);
        });
    }

    public static void canDeleteJob(Job job, OnSuccessListener<Boolean> listener) {
        Users.loadCurrentUser(user -> {
            boolean userIsAdmin = user.isAdmin();
            boolean userIsPoster = !Users.isDifferentUser(user.getUniqueId(), job.getPosterUid());
            boolean jobIsOpen = job.getWorkerUid() == null;
            boolean canDeleteJob = userIsAdmin || (userIsPoster && jobIsOpen);
            listener.onSuccess(canDeleteJob);
        });
    }

    public static void applyForJob(Job job) {
        Users.loadCurrentUser(user -> {
            job.setWorkerUid(user.getUniqueId());
            updateJob(job);
        });
    }

    public static void flagJob(Job job) {
        job.setFlagged(true);
        updateJob(job);
    }

    public static void unflagJob(Job job) {
        job.setFlagged(false);
        updateJob(job);
    }

    public static void finishJob(Job job) {
        job.setFinished(true);
        updateJob(job);
    }

    public static void quitJob(Job job){
        job.setWorkerUid(null);
        updateJob(job);
    }

    private static void updateJob(Job job) {
        jobsCollection()
                .document(job.getUniqueId())
                .set(job);
    }

    public static void deleteJob(Job job) {
        jobsCollection()
                .document(job.getUniqueId())
                .delete();
    }

    public static void workedForEachOther(User userOne, User userTwo, OnSuccessListener<Boolean> listener) {
        workedForEmployer(userOne, userTwo, userOneWorkedForUserTwo -> {
            workedForEmployer(userTwo, userOne, userTwoWorkedForUserOne -> {
                boolean workedForEachOther = userOneWorkedForUserTwo || userTwoWorkedForUserOne;
                listener.onSuccess(workedForEachOther);
            });
        });
    }

    public static void workedForEmployer(User worker, User employer, OnSuccessListener<Boolean> listener) {
        jobsCollection()
                .whereEqualTo("posterUid", employer.getUniqueId())
                .whereEqualTo("workerUid", worker.getUniqueId())
                .whereEqualTo("finished", true)
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    boolean workedForEmployer = !documentSnapshots.isEmpty();
                    listener.onSuccess(workedForEmployer);
                });
    }

    public static void postJob(Job job, List<Uri> images, OnCompleteListener<Void> listener) {
        uploadJobImages(images, downloadUrls -> {
            String uniqueId = Utils.getUniqueId();
            job.setUniqueId(uniqueId);
            job.setImageUrls(downloadUrls);

            jobsCollection()
                    .document(uniqueId)
                    .set(job)
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