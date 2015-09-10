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

    @com.google.gson.annotations.SerializedName("phonenumb")
    private String Phonenumb;

    @com.google.gson.annotations.SerializedName("condition")
    private String Condition;

    @com.google.gson.annotations.SerializedName("invitation")
    private String Invitation;

    @com.google.gson.annotations.SerializedName("name")
    private String Name;

    @com.google.gson.annotations.SerializedName("registerid")
    private String RegisterId;


    public UserData(String id,  String groupId, Boolean isManager,String phonenumb,String condition, String invitation, String name, String registerId){
        this.setId(id);
        this.setGroupId(groupId);
        this.setIsManager(isManager);
        this.setPhonenumb(phonenumb);
        this.setCondition(condition);
        this.setInvitation(invitation);
        this.setName(name);
        this.setRegisterId(registerId);
    }

    public UserData(String id,String name){//,String registerId){
        this.setId(id);
        this.setName(name);
        //this.setRegisterId(registerId);
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

    public void setIsManager(Boolean isManager){
        IsManager=isManager;
    }

    public String getPhonenumb(){return Phonenumb;}

    public void setPhonenumb(String phonenumb){
        Phonenumb = phonenumb;
    }

    public void setCondition(String condition){
        Condition=condition;
    }

    public String getCondition(){
        return Condition;
    }

    public void setInvitation(String senderId){
        Invitation = senderId;
    }
    public String getInvitation(){
        return Invitation;
    }

    public void setName(String name){ Name = name;}
    public String getName(){ return Name;}

    public void setRegisterId(String regid){RegisterId = regid;}
    public String getRegisterId(){return RegisterId;}

}
