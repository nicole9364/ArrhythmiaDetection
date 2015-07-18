package com.project84.ece2015.arrhythmiadetection;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;


/**
 * Created by Nicole on 10/07/2015.
 */
public class BaseActivity extends AppCompatActivity {



    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;
            case R.id.action_logout:
                openLogout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void openSettings() {


    }

    private void openLogout() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken != null && com.facebook.Profile.getCurrentProfile() != null){
            Log.d("LOGOUT", "if loop");
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(BaseActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


    }
}



