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

import java.lang.reflect.Member;

/**
 * Created by Nicole on 11/08/2015.
 */
public class MemberHomePage extends BaseActivity {

        private String userid;
        private String username;
        private Button logoutbtn;
        private Button call;

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fbhomepage);

            Profile profile = Profile.getCurrentProfile();
            username = profile.getName();
            TextView name = (TextView) findViewById(R.id.usernametext);
            name.setText(username);

            userid = profile.getId();
            ProfilePictureView pic = (ProfilePictureView) findViewById(R.id.fbprofile_pic);
            pic.setProfileId(userid);


            findViewById(R.id.calibration).setVisibility(View.GONE);
            findViewById(R.id.manageGroup).setVisibility(View.GONE);

            //TODO create a see group button/feature



            logoutbtn = (Button) findViewById(R.id.logout);
            logoutbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AccessToken accessToken = AccessToken.getCurrentAccessToken();
                    if(accessToken != null && com.facebook.Profile.getCurrentProfile() != null){
                        Log.d("LOGOUT", "");
                        LoginManager.getInstance().logOut();
                        Intent intent = new Intent(MemberHomePage.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });



        }


}
