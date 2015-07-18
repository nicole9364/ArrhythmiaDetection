package com.project84.ece2015.arrhythmiadetection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Nicole on 12/07/2015.
 */
public class FacebookProfile extends HomePage {

    private Profile _profile;
    private String _id;
    private String _name;
    private String _firstName;
    private String _lastName;
    private Bitmap _pic;
    private URL _url;

    public FacebookProfile(){

        this._profile = Profile.getCurrentProfile();
        this._id = Profile.getCurrentProfile().getId();
        this._name = Profile.getCurrentProfile().getName();
        this._firstName = Profile.getCurrentProfile().getFirstName();
        this._lastName = Profile.getCurrentProfile().getLastName();
        /*try {
            this._url = new URL("http://graph.facebook.com/"+this._profile.getId()+"/picture?type=small");
            this._pic = BitmapFactory.decodeStream((InputStream) this._url.getContent());
        } catch (MalformedURLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e){
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }*/

    }


    public String getId(){
        return _id;
    }

    public String getName(){
        return _name;
    }

    /*public Bitmap getProfilePicture(){
        return _pic;
    }*/

    public String getFirstName(){
        return _firstName;
    }

    public String getLastName(){
        return _lastName;
    }


}
