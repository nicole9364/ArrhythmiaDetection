package com.project84.ece2015.arrhythmiadetection;
/**
 * Created by Nicole on 6/08/2015.
 */
public class UserData {
    @com.google.gson.annotations.SerializedName("id")
    private String Id;

    @com.google.gson.annotations.SerializedName("ismanager")
    private Boolean IsManager;

    @com.google.gson.annotations.SerializedName("groupid")
    private String GroupId;


    public UserData(String id, String groupId, Boolean isManager){
        this.setId(id);
        this.setGroupId(groupId);
        this.setIsManager(isManager);
    }

    public UserData(String id){
        this.setId(id);
    }

    public String getId(){ return Id;}

    public final void setId(String id){
        Id = id;
    }

    public String getGroupId(){ return GroupId;}

    public void setGroupId(String groupId){
        GroupId = groupId;
    }

    public Boolean getIsManager(){ return IsManager;}

    public final void setIsManager(Boolean isManager){
        IsManager=isManager;
    }

}
