package com.project84.ece2015.arrhythmiadetection;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

/**
 * Created by Nicole on 11/08/2015.
 */
public class NewUserPage extends BaseActivity {


    private String userid;
    private String username;
    private Button manageGroup;
    private ProfilePictureView pic;
    private Button createGroup;
    private Button joinGroup;
    private Button logoutbtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newuserpage);




        Profile profile = Profile.getCurrentProfile();

        username = profile.getName();
        TextView name = (TextView) findViewById(R.id.usernametext);
        name.setText(username);

        userid = profile.getId();
        pic = (ProfilePictureView) findViewById(R.id.fbprofile_pic);
        pic.setProfileId(userid);

        createGroup = (Button) findViewById(R.id.createGroup);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewUserPage.this,CreateGroupPage.class);
                PendingIntent pendingIntent =
                        TaskStackBuilder.create(NewUserPage.this)
                                // add all of DetailsActivity's parents to the stack,
                                // followed by DetailsActivity itself
                                .addNextIntentWithParentStack(intent)
                                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(NewUserPage.this);
                builder.setContentIntent(pendingIntent);

                startActivity(intent);


            }
        });

        joinGroup = (Button) findViewById(R.id.joinGroup);
        joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    Intent intent = new Intent(NewUserPage.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }


}
