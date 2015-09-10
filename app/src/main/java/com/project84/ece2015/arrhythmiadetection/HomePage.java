package com.project84.ece2015.arrhythmiadetection;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
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
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.microsoft.windowsazure.messaging.NotificationHub;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;
import com.microsoft.windowsazure.notifications.NotificationsManager;


import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Nicole on 8/07/2015.
 */
public class HomePage extends BaseActivity {

    private String userid;
    private String username;
    private Button calibrate;
    private Button manageGroup;
    private Button logoutbtn;
    private ProfilePictureView pic;

    private MobileServiceClient mClient;
    private MobileServiceTable<UserData> mUserDataTable;
    private List<String> regIdList =  new ArrayList<String>();;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fbhomepage);


        try {
            mClient = new MobileServiceClient(
                    "https://arrhythmia-detection.azure-mobile.net/",
                    "XMdXwbdExMrUFlySSayXtOFTqnpMot14",
                    this
            );


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mUserDataTable = mClient.getTable(UserData.class);

        Profile profile = Profile.getCurrentProfile();
        username = profile.getName();
        TextView name = (TextView) findViewById(R.id.usernametext);
        name.setText(username);

        Button calibrate = (Button)findViewById(R.id.calibration);
        calibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String groupid = prefs.getString("groupid", null);
                System.out.println("MY GROUP ID: " + groupid);
                mUserDataTable.where().field("groupid").eq(groupid).execute(new TableQueryCallback<UserData>() {
                    @Override
                    public void onCompleted(List<UserData> result, int count, Exception exception, ServiceFilterResponse response) {
                        //get list of my group members
                        for (UserData member : result) {
                            System.out.println(member.getRegisterId());
                            if (!member.getId().equals(Profile.getCurrentProfile().getId())) { //exclude current user
                            regIdList.add(member.getRegisterId());
                            MyHandler handlepush = new MyHandler();
                            handlepush.sendAlert(member.getRegisterId());
                            }


                        }

                        /*String input = TextUtils.join(",", regIdList);
                        System.out.println("INPUT    " + input);
                        MyHandler handlepush = new MyHandler();
                        handlepush.sendAlert(input);*/
                    }
                });




            }
        });



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
                if (accessToken != null && com.facebook.Profile.getCurrentProfile() != null) {
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
