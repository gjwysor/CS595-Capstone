package com.example.mrschmitz.jobs.pojos;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Noah on 11/7/2017.
 */

@Data
@Builder
@NoArgsConstructor
public class Review implements Serializable {

    private String reviewedUid;
    private String reviewerUid;
    private int rating;
    private String review;

    public Review(String reviewed, String reviewer, int rate, String newReview){
        reviewedUid = reviewed;
        reviewerUid = reviewer;
        rating = rate;
        review = newReview;
    }

    public String getReviewed(){
        return reviewedUid;
    }

    public String getReviewer(){
        return reviewerUid;
    }

    public int getRating(){
        return rating;
    }

    public String getReview(){
        return review;
    }

    public void setReviewed(String reviewedID){
        reviewedUid = reviewedID;
    }

    public void setReviewer(String reviewerID){
        reviewerUid = reviewerID;
    }

    public void setRating(int rate){
        rating = rate;
    }

    public void setReview(String newReview){
        review = newReview;
    }

}
