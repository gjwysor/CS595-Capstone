package com.example.mrschmitz.jobs.pojos;

import org.parceler.Parcel;

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
@Parcel
public class User {

    String uniqueId;
    String name;
    String photoUrl;
    String bio;
    boolean admin;

}
