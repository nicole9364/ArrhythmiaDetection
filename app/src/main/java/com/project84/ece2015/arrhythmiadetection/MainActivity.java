package com.project84.ece2015.arrhythmiadetection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;


public class MainActivity extends Activity {
    private Boolean signedIn;
    private LoginButton fbloginButton;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MAIN", "ACTiVITY");
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());



        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken != null ){
            Log.d("MAIN", "ifLoop");
            Intent intent = new Intent(MainActivity.this, HomePage.class);
            startActivity(intent);
            finish();
        }
        /*final SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        signedIn = sharedPref.getBoolean("signedin", false);
        if (signedIn) {
            Log.d("MAIN", "ifLoop");
            Intent intent = new Intent(MainActivity.this, HomePage.class);
            startActivity(intent);
            finish();
        }*/

        setContentView(R.layout.activity_main);

        fbloginButton = (LoginButton) findViewById(R.id.fb_login_button);
        fbloginButton.setReadPermissions(Arrays.asList("user_friends", "public_profile"));

        mCallbackManager = CallbackManager.Factory.create();
        fbloginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                Log.d("LOgIN", "onSuccessmethod");

                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                Log.e("Token", accessToken.toString());
                /*signedIn = true;
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("signedin",signedIn);
                editor.apply();*/
                Intent intent = new Intent(MainActivity.this, HomePage.class);
                startActivity(intent);
                finish();
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
}
