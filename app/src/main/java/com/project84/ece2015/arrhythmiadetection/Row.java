package com.project84.ece2015.arrhythmiadetection;

import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

/**
 * Created by Nicole on 25/08/2015.
 */
public class Row {
    //ProfilePictureView picture;
    String id;
    String profileName;
    boolean selected;

    public Row(String id, String name, boolean selected){
        super();
        this.id = id;
        this.profileName = name;
        this.selected = selected;
    }

    /*public ProfilePictureView getPicture(){
        return picture;
    }
    public void setPicture(ProfilePictureView pic){
        this.picture = pic;
    }*/

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

    public boolean isChecked(){
        return selected;
    }
    public void setChecked(boolean selected) {
        this.selected = selected;
    }
}
