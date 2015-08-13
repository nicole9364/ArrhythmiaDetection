package com.project84.ece2015.arrhythmiadetection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;

/**
 * Created by Nicole on 22/07/2015.
 */
public class ManageGroup extends BaseActivity {

    private Button invite;
    private Button add;
    String appLinkUrl;
    private CallbackManager facebookCallbackManager;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.managegroup);

        invite = (Button) findViewById(R.id.invite);
        invite.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //facebook invitation
                //openDialogInvite(ManageGroup.this);


                appLinkUrl = "https://fb.me/1616024505322544";

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


                }

            }

        });
    }

}
