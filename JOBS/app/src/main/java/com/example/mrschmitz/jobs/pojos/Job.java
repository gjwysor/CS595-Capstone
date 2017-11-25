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
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Job implements Serializable {

    private String jobTitle;
    private String jobSkills;
    private String jobDesc;
    private String posterUid;
    private String workerUid;
    private boolean finished;

    public Job(String postID, String title, String skills, String desc, boolean status){
        posterUid = postID;
        jobTitle = title;
        jobSkills = skills;
        jobDesc = desc;
        finished = status;
    }

    public String getPoster(){
        return posterUid;
    }

    public String getWorker(){
        return workerUid;
    }

    public boolean getFinished(){
        return finished;
    }

    public String getTitle(){
        return jobTitle;
    }

    public String getSkills(){
        return jobSkills;
    }

    public String getDesc(){
        return jobDesc;
    }

    public void setTitle(String title){
        jobTitle = title;
    }

    public void setSkills(String skills){
        jobSkills = skills;
    }

    public void setDesc(String desc){
        jobDesc = desc;
    }

    public void setPoster(String postKey){
        posterUid = postKey;
    }

    public void getWorker(String workKey){
        workerUid = workKey;
    }

    public void getFinished(boolean status){
        finished = status;
    }
}
