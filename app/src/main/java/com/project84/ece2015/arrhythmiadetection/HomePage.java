package com.project84.ece2015.arrhythmiadetection;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

/**
 * Created by Nicole on 8/07/2015.
 */
public class HomePage extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        Object actionBar = getSupportActionBar();

        Log.d("HOMEPAGE", "HAHAHHAHHAA");



    }
}
