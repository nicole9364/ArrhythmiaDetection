package com.project84.ece2015.arrhythmiadetection;

/**
 * Created by Nicole on 28/08/2015.
 */
public class Invitation {

    String id;
    String profileName;
    String msg;
    String grpName;

    public Invitation(String id, String name,String groupName){
        super();
        this.id = id;
        this.profileName = name;
        this.msg = "Please join my group" + groupName;
        this.grpName = groupName;
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

    public String getGrpName(){
        return grpName;
    }
    public void setGrpName(String groupName){
        this.grpName = groupName;
    }
}
