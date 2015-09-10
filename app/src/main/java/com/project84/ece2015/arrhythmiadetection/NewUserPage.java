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
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;
import com.microsoft.windowsazure.notifications.NotificationsManager;

import java.net.MalformedURLException;
import java.util.List;

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

    private MobileServiceClient mClient;
    private MobileServiceTable<UserData> mUserDataTable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newuserpage);


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


        mUserDataTable.where().field("id").eq(Profile.getCurrentProfile().getId()).execute(new TableQueryCallback<UserData>() {
            @Override
            public void onCompleted(List<UserData> result, int count, Exception exception, ServiceFilterResponse response) {
                //get list of my group members
                for (UserData member : result) {
                    if(member.getInvitation()!=null){
                        Toast.makeText(NewUserPage.this, "You have received an invitation", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

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
                Intent intent = new Intent(NewUserPage.this,JoinGroupPage.class);
                PendingIntent pendingIntent =
                        TaskStackBuilder.create(NewUserPage.this)
                                .addNextIntentWithParentStack(intent)
                                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(NewUserPage.this);
                builder.setContentIntent(pendingIntent);

                startActivity(intent);
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
