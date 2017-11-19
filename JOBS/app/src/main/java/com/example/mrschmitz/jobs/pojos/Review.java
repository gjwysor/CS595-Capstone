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
@AllArgsConstructor
public class Review implements Serializable {

    private String reviewedUid;
    private String reviewerUid;
    private int rating;
    private String review;

}
