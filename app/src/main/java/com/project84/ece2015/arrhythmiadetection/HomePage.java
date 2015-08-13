package com.project84.ece2015.arrhythmiadetection;

import android.app.ActionBar;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
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
    private Button manageGroup;
    private Button logoutbtn;
    private ProfilePictureView pic;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fbhomepage);

        Profile profile = Profile.getCurrentProfile();
        username = profile.getName();
        TextView name = (TextView) findViewById(R.id.usernametext);
        name.setText(username);

        /*TODO
        userpic = profile.getProfilePicture();
        ImageView pic = (ImageView) findViewById(R.id.profile_pic);
        pic.setImageBitmap(userpic);*/


        userid = profile.getId();
        pic = (ProfilePictureView) findViewById(R.id.fbprofile_pic);
        pic.setProfileId(userid);



        manageGroup = (Button) findViewById(R.id.manageGroup);
        manageGroup.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent(HomePage.this, ManageGroup.class);
                PendingIntent pendingIntent =
                        TaskStackBuilder.create(HomePage.this)
                                // add all of DetailsActivity's parents to the stack,
                                // followed by DetailsActivity itself
                                .addNextIntentWithParentStack(intent)
                                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(HomePage.this);
                builder.setContentIntent(pendingIntent);

                startActivity(intent);
                /*finish();*/
            }

        });

        logoutbtn = (Button) findViewById(R.id.logout);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if(accessToken != null && com.facebook.Profile.getCurrentProfile() != null){
                    Log.d("LOGOUT", "");
                    LoginManager.getInstance().logOut();
                    Intent intent = new Intent(HomePage.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}
