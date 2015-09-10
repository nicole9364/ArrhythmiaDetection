package com.project84.ece2015.arrhythmiadetection;

import android.os.AsyncTask;

import com.facebook.Profile;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Nicole on 5/09/2015.
 */
public class AlertNotification extends AsyncTask<String,Void,Void> {

    private String apiKey = "AIzaSyDeBuOvQV0td09BYIiWnOhRS3AsHHmy-0A";
    String id = MyHandler.input;
    protected Void doInBackground(String... ids) {

        System.out.println("Alert to ids:  "+id);
        String input = "{\"registration_ids\" : [\""+id+"\"],\"data\" : {\"message\": \"ALERT!: "+ Profile.getCurrentProfile().getName()+" needs help!\"},}";

        URL url = null;
        try {
            url = new URL("https://android.googleapis.com/gcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "key=" + apiKey);

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush(); //send
            os.close();

            int responseCode = conn.getResponseCode();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

