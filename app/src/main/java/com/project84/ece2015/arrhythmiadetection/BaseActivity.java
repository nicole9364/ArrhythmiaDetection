package com.project84.ece2015.arrhythmiadetection;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


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

    }
}



