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
public class Job implements Serializable {

    private String posterUid;
    private String workerUid;
    private String title;
    private String skills;
    private String description;
    private boolean finished;

}
