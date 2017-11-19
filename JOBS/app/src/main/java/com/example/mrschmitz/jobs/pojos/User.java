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
public class User implements Serializable {

    private String uniqueId;
    private String name;
    private String photoUrl;
    private String bio;

}
