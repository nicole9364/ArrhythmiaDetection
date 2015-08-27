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

    private MobileServiceClient mClient;
    private MobileServiceTable<UserData> mUserDataTable;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invitepage);
        FacebookSdk.sdkInitialize(getApplicationContext());
        listview = (ListView) findViewById(R.id.listView);

        try {
            mClient = new MobileServiceClient(
                    "https://arrhythmiadetection.azure-mobile.net/",
                    "LPElhTMLVPNSIgciYyGYEGNQJpJtqs38",
                    this);


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
                    if(member.getId()!=Profile.getCurrentProfile().getId()) {
                        memberIds.add(member.getId());
                    }
                }
            }
        });

                GraphRequestBatch batch = new GraphRequestBatch(
                        GraphRequest.newMyFriendsRequest(
                                AccessToken.getCurrentAccessToken(),
                                new GraphRequest.GraphJSONArrayCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONArray jsonArray,
                                            GraphResponse response) {
                                        // Application code for users friends
                                        //System.out.println("getFriendsData onCompleted : jsonArray " + jsonArray);


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
                                                    System.out.println(data.getJSONObject(i).get("id"));
                                                    System.out.println(data.getJSONObject(i).get("name"));
                                                }

                                            }



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




        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(adapter.getCheckedItem());
                List<String> receiverList = adapter.getCheckedItem();

                sendInvitation(receiverList);

            }
        });
        invite = (Button) findViewById(R.id.invite);
        invite.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                //TODO

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
                            Toast.makeText(getApplicationContext(), "Invitation Sent Successfully!", Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onCancel() {
                            Toast.makeText(getApplicationContext(), "Invitation Cancelled!", Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onError(FacebookException e) {
                            Toast.makeText(getApplicationContext(), "Error Occured!", Toast.LENGTH_LONG).show();

                        }
                    });


                }

            }

        });
    }

    private void sendInvitation(List<String> receiverList) {
    }

}
