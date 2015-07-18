package com.project84.ece2015.arrhythmiadetection;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Nicole on 8/07/2015.
 */
public class HomePage extends BaseActivity {

    private String userid;
    private String username;
    private Bitmap userpic;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fbhomepage);

        FacebookProfile profile = new FacebookProfile();
        username = profile.getName();
        TextView name = (TextView) findViewById(R.id.usernametext);
        name.setText(username);

        /*TODO
        userpic = profile.getProfilePicture();
        ImageView pic = (ImageView) findViewById(R.id.profile_pic);
        pic.setImageBitmap(userpic);*/


        userid = profile.getId();
        ProfilePictureView pic = (ProfilePictureView) findViewById(R.id.fbprofile_pic);
        pic.setProfileId(userid);



    }
}
