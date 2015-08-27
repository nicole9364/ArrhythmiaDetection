package com.project84.ece2015.arrhythmiadetection;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.JoinAppGroupDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Nicole on 22/07/2015.
 */
public class ManageGroup extends BaseActivity {

    private Button invite;
    private Button add;
    String appLinkUrl;
    private CallbackManager facebookCallbackManager;
    private JSONArray list;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.managegroup);

        invite = (Button) findViewById(R.id.invite);
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

                /*GraphRequestBatch batch = new GraphRequestBatch(
                        GraphRequest.newMyFriendsRequest(
                                AccessToken.getCurrentAccessToken(),
                                new GraphRequest.GraphJSONArrayCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONArray jsonArray,
                                            GraphResponse response) {
                                        // Application code for users friends
                                        System.out.println("getFriendsData onCompleted : jsonArray " + jsonArray);

                                        try {
                                            JSONObject jsonObject = response.getJSONObject();
                                            System.out.println("getFriendsData onCompleted : jsonObject " + jsonObject);
                                            JSONObject summary = jsonObject.getJSONObject("summary");
                                            JSONArray data = jsonObject.getJSONArray("data");
                                            System.out.println(data);
                                            System.out.print(data.getJSONObject(0).get("id"));
                                            System.out.println(data.length());
                                            System.out.println("getFriendsData onCompleted : summary total_count - " + summary.getString("total_count"));
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
                *//*new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/me/friends",
                        //"/{friend-list-id}",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
            *//**//* handle the result *//**//*
                                FacebookRequestError error =response.getError();
                                if(error!=null && response!=null){
                                    Log.d("error",error.toString());
                                }else{
                                    System.out.println(response.getRawResponse());



                                }
                            }
                        }
                ).executeAsync();*//*
                //TODO

                *//*appLinkUrl = "https://fb.me/1616024505322544";

                facebookCallbackManager = CallbackManager.Factory.create();
                if (AppInviteDialog.canShow()) {
                    AppInviteContent content = new AppInviteContent.Builder()
                            .setApplinkUrl(appLinkUrl)
                            .build();
                    AppInviteDialog.show(ManageGroup.this, content);

                    AppInviteDialog appInviteDialog = new AppInviteDialog(ManageGroup.this);


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


                }*/

            }

        });
    }

}
