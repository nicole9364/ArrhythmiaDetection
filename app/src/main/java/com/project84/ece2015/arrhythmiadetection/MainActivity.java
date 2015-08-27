package com.project84.ece2015.arrhythmiadetection;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.JsonObject;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;
import java.net.MalformedURLException;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import com.microsoft.windowsazure.mobileservices.*;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceJsonTable;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableJsonQueryCallback;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;


import java.util.Arrays;
import java.util.List;


public class MainActivity extends Activity {
    private LoginButton fbloginButton;
    private CallbackManager mCallbackManager;
    protected UserData currentUser;
    private ProgressBar spinner;



    private MobileServiceClient mClient;
    private MobileServiceTable<UserData> mUserDataTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MAIN", "ACTiVITY");
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());


        try {
            mClient = new MobileServiceClient(
                    "https://arrhythmiadetection.azure-mobile.net/",
                    "LPElhTMLVPNSIgciYyGYEGNQJpJtqs38",
                    this);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        mUserDataTable = mClient.getTable(UserData.class);

        setContentView(R.layout.activity_main);
        final Profile profile = Profile.getCurrentProfile();

        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
        fbloginButton = (LoginButton) findViewById(R.id.fb_login_button);




        if(profile != null ){ //if there is a user logged in
            Log.d("MAIN", "ifLoop");
            fbloginButton.setVisibility(View.GONE);
            spinner.setVisibility(View.VISIBLE);
            retrieveData(profile.getId());

        }else {



            fbloginButton.setReadPermissions(Arrays.asList("user_friends", "public_profile"));


            mCallbackManager = CallbackManager.Factory.create();
            fbloginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {

                    Log.d("LOgIN", "onSuccessmethod");
                    fbloginButton.setVisibility(View.GONE);

                    spinner.setVisibility(View.VISIBLE);

                    AccessToken accessToken = AccessToken.getCurrentAccessToken();
                    Log.e("Token", accessToken.toString());
                    retrieveData(accessToken.getUserId());
                }

                @Override
                public void onCancel() {
                    // App code
                }
                 @Override
                 public void onError(FacebookException exception) {
                     // App code
                 }


            });


        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void retrieveData(final String profileId){

        mUserDataTable.where().field("id").eq(profileId).execute(new TableQueryCallback<UserData>() {
            @Override
            public void onCompleted(List<UserData> result, int count, Exception exception, ServiceFilterResponse response) {
                spinner.setVisibility(View.GONE);
                if (exception == null){
                    if (result.size()==1){
                        Log.d("Found user data","ONE!");
                        for (UserData data : result){
                            currentUser = new UserData(data.getId(),data.getGroupId(),data.getIsManager(),data.getPhonenumb(),data.getGroupId());
                        }

                        try {
                            if (currentUser.getIsManager() == true) {
                                Intent intent = new Intent(MainActivity.this, HomePage.class);
                                startActivity(intent);
                                finish();
                            } else if (currentUser.getIsManager() == false) {
                                //TODO check if assigned in a group or not
                            /*Intent intent = new Intent(MainActivity.this, MemberHomePage.class);
                            startActivity(intent);
                            finish();*/
                            }
                        }catch(NullPointerException e){
                            Log.d("NO assigned group", "not a manager");
                            Intent intent = new Intent(MainActivity.this, NewUserPage.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    else if(result.size()==0){
                        Log.d("NO userData", "NONE");
                        createNewUserData(profileId);
                    }
                    else if(result.size()>1){
                        //TODO error msg
                        Log.d("SHOULDN'tHAPPEn","NOOO");
                    }
                }
            }
        });
    }

    public void createNewUserData(String profileId){
        Log.d("NEW","CREATE NEW USER");
        //ADDING NEW USERDATA
        UserData newUserData = new UserData(profileId);
        mUserDataTable.insert(newUserData, new TableOperationCallback<UserData>() {
            @Override
            public void onCompleted(UserData entity, Exception exception, ServiceFilterResponse response) {
                Log.d("Creating New User Data","success");
                //show newUserPage
                Intent intent = new Intent(MainActivity.this, NewUserPage.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
