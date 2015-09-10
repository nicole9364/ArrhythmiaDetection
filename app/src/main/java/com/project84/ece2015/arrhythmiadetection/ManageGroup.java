package com.project84.ece2015.arrhythmiadetection;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.JoinAppGroupDialog;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Nicole on 22/07/2015.
 */
public class ManageGroup extends BaseActivity {

    private Button invite;
    private Button add;
    private TextView txt;
    private ProgressBar spinner;


    ListView listview;
    List<MemberRow> memberList = new ArrayList<MemberRow>();

    private MobileServiceClient mClient;
    private MobileServiceTable<UserData> mUserDataTable;

    private CustomMemberAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.managegroup);
        FacebookSdk.sdkInitialize(getApplicationContext());
        listview = (ListView) findViewById(R.id.listView);

        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);


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



        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String groupid = prefs.getString("groupid",null);

        txt = (TextView) findViewById(R.id.invitableFriends);
        txt.setText("Current Members of " + groupid + ":");

        //get all members id of the group
        mUserDataTable.where().field("groupid").eq(groupid).execute(new TableQueryCallback<UserData>() {
            @Override
            public void onCompleted(List<UserData> result, int count, Exception exception, ServiceFilterResponse response) {
                //get list of my group members
                for (UserData member : result) {

                    if (!member.getId().equals(Profile.getCurrentProfile().getId()) ) {//excluding current user
                        memberList.add(new MemberRow(member.getId(), member.getName()));
                    }
                }
                spinner.setVisibility(View.GONE);
                adapter = new CustomMemberAdapter(ManageGroup.this, memberList);
                listview.setAdapter(adapter);

            }
        });






        add = (Button) findViewById(R.id.send);
        add.setVisibility(View.GONE);
        invite = (Button) findViewById(R.id.invite);
        invite.setText("Invite to Group");
        invite.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent intent = new Intent(ManageGroup.this, InvitePage.class);
                PendingIntent pendingIntent =
                        TaskStackBuilder.create(ManageGroup.this)
                                // add all of DetailsActivity's parents to the stack,
                                // followed by DetailsActivity itself
                                .addNextIntentWithParentStack(intent)
                                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(ManageGroup.this);
                builder.setContentIntent(pendingIntent);

                startActivity(intent);

            }

        });
    }

}
