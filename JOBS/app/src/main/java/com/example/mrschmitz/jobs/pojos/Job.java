package com.example.mrschmitz.jobs.pojos;

import org.parceler.Parcel;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Noah on 11/7/2017.
 */

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Parcel
public class Job {

    List<String> imageUrls;
    String title;
    double paymentAmount;
    String paymentType;
    String paymentMethod;
    String description;
    String address;
    double latitude;
    double longitude;
    String posterUid;
    String workerUid;
    boolean finished;
    boolean flagged;

}
