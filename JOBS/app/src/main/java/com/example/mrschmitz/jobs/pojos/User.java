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

    public User(String uid, String uname){
        uniqueId = uid;
        name = uname;
    }

    public String getUid(){
        return uniqueId;
    }

    public String getName(){
        return name;
    }

    public String getPhoto(){
        return photoUrl;
    }

    public String getBio(){
        return bio;
    }

    public void setUid(String uid){
        uniqueId = uid;
    }

    public void setName(String userName){
        name = userName;
    }

    public void setPhoto(String purl){
        photoUrl = purl;
    }

    public void setBio(String biostring){
        bio = biostring;
    }
}
