package com.project84.ece2015.arrhythmiadetection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.JoinAppGroupDialog;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;
import com.microsoft.windowsazure.notifications.NotificationsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * Created by Nicole on 22/07/2015.
 */
public class InvitePage extends BaseActivity {

    private Button invite;
    private Button send;
    String appLinkUrl;
    private CallbackManager facebookCallbackManager;
    private JSONArray list;
    ListView listview;
    CustomAdapter adapter;
    List<Row> rowItemList = new ArrayList<Row>();
    List<String> memberIds = new ArrayList<String>();
    List<String> recRegIds = new ArrayList<String>();
    private ProgressBar spinner;

    private MobileServiceClient mClient;
    private MobileServiceTable<UserData> mUserDataTable;



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

        mUserDataTable.where().field("groupid").eq(groupid).execute(new TableQueryCallback<UserData>() {
            @Override
            public void onCompleted(List<UserData> result, int count, Exception exception, ServiceFilterResponse response) {
                //get list of my group members
                for (UserData member : result){
                    System.out.println(member.getId());
                    if(!member.getId().equals(Profile.getCurrentProfile().getId())) { //exclude current user
                        memberIds.add(member.getId());
                    }
                }
                GraphRequestBatch batch = new GraphRequestBatch(
                        GraphRequest.newMyFriendsRequest(
                                AccessToken.getCurrentAccessToken(),
                                new GraphRequest.GraphJSONArrayCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONArray jsonArray,
                                            GraphResponse response) {

                                        try {
                                            JSONObject jsonObject = response.getJSONObject();
                                            System.out.println("getFriendsData onCompleted : jsonObject " + jsonObject);
                                            JSONArray data = jsonObject.getJSONArray("data");
                                            int size = data.length(); //number of friends who have installed the app

                                            //create listview
                                            String id;
                                            for (int i = 0; i < size; i++) {
                                                id = data.getJSONObject(i).get("id").toString();
                                                if (!memberIds.contains(id)) { //exclude group members from the list
                                                    rowItemList.add(new Row(data.getJSONObject(i).get("id").toString(), data.getJSONObject(i).get("name").toString(), false));

                                                }
                                            }
                                            spinner.setVisibility(View.GONE);
                                            adapter = new CustomAdapter(InvitePage.this, rowItemList);
                                            listview.setAdapter(adapter);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                );
                batch.addCallback(new GraphRequestBatch.Callback() {
                    @Override
                    public void onBatchCompleted(GraphRequestBatch graphRequests) {
                        // Application code for when the batch finishes
                    }
                });
                batch.executeAsync();
            }


        });

        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(adapter.getCheckedItem());
                List<String> receiverList = adapter.getCheckedItem();



                    if(receiverList.size()==0)

                    {
                        Toast.makeText(InvitePage.this, "Select friends to invite", Toast.LENGTH_LONG).show();
                    }

                    else

                    {

                        for(String receiver:receiverList) {
                            mUserDataTable.where().field("id").eq(receiver).execute(new TableQueryCallback<UserData>() {
                                @Override
                                public void onCompleted(List<UserData> result, int count, Exception exception, ServiceFilterResponse response) {
                                    //get list of my group members
                                    for (UserData member : result) {
                                        System.out.println("RECEIVER REGID   " + member.getRegisterId());
                                        recRegIds.add(member.getRegisterId());
                                    }
                                }
                            });
                        }


                        sendInvitation(receiverList);
                        Toast.makeText(InvitePage.this, "Invitation Sent Successfully!", Toast.LENGTH_LONG).show();

                        finish();
                    }

                }
            });
        invite = (Button) findViewById(R.id.invite);
        invite.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                appLinkUrl = "https://fb.me/1616024505322544";

                facebookCallbackManager = CallbackManager.Factory.create();
                if (AppInviteDialog.canShow()) {
                    AppInviteContent content = new AppInviteContent.Builder()
                            .setApplinkUrl(appLinkUrl)
                            .build();
                    AppInviteDialog.show(InvitePage.this, content);

                    AppInviteDialog appInviteDialog = new AppInviteDialog(InvitePage.this);


                    appInviteDialog.registerCallback(facebookCallbackManager, new FacebookCallback<AppInviteDialog.Result>() {
                        @Override
                        public void onSuccess(AppInviteDialog.Result result) {
                            Toast.makeText(InvitePage.this, "Invitation Sent Successfully!", Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onCancel() {
                            Toast.makeText(InvitePage.this, "Invitation Cancelled!", Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onError(FacebookException e) {
                            Toast.makeText(InvitePage.this, "Error Occured!", Toast.LENGTH_LONG).show();

                        }
                    });


                }

            }

        });
    }

    private void sendInvitation(List<String> receiverList) {

        for(int i =0; i<receiverList.size();i++) {
            System.out.println("id: "+receiverList.get(i));
            mUserDataTable.where().field("id").eq(receiverList.get(i)).execute(new TableQueryCallback<UserData>() {
                @Override
                public void onCompleted(List<UserData> result, int count, Exception exception, ServiceFilterResponse response) {
                    for (UserData member : result) {
                        System.out.println("MEMBER TO INVITE : " + member.getId());

                        //TODO send push msg

                        MyHandler handlepush = new MyHandler();

                        /*Content cont = new Content();
                        cont.addRegId("7096390951985654009-6064627686871787204-17");
                        cont.createData("New Invitation", "You have received a new invitation from " + Profile.getCurrentProfile().getName().toString());
                        String input =  "{\"registration_ids\" : [\"7096390951985654009-6064627686871787204-17\"],\"data\" : {\"message\": \"You have received a new invitation\"},}";
*/

                        for(String regid : recRegIds) {
                            handlepush.sendPush(regid);
                        }

                        member.setInvitation(Profile.getCurrentProfile().getId());
                        mUserDataTable.update(member);

                    }

                }
            });
        }

    }



}
