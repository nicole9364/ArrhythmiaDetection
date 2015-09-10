package com.project84.ece2015.arrhythmiadetection;

import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

/**
 * Created by Nicole on 25/08/2015.
 */
public class MemberRow {
    String id;
    String profileName;

    public MemberRow(String id, String name){
        super();
        this.id = id;
        this.profileName = name;
    }

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getProfileName(){
        return profileName;
    }
    public void setProfileName(String name){
        this.profileName = name;
    }

}
