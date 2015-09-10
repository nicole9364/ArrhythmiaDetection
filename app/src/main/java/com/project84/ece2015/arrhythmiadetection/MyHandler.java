package com.project84.ece2015.arrhythmiadetection;

import com.facebook.Profile;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.microsoft.windowsazure.messaging.NotificationHub;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;
import com.microsoft.windowsazure.notifications.NotificationsHandler;
import com.microsoft.windowsazure.notifications.NotificationsManager;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

/**
 * Created by Nicole on 1/09/2015.
 */

public class MyHandler extends NotificationsHandler {


    public static final int NOTIFICATION_ID = 1;
    public static String regid;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    Context ctx;

    private String SENDER_ID = "678716789292";//"arrhythmiadetection";
    private String apiKey = "AIzaSyDeBuOvQV0td09BYIiWnOhRS3AsHHmy-0A";
    private GoogleCloudMessaging gcm;
    private NotificationHub hub;
    private String HubName = "arrhythmia-detectionhub";
    private String HubListenConnectionString = "Endpoint=sb://arrhythmia-detectionhub2-ns.servicebus.windows.net/;SharedAccessKeyName=DefaultListenSharedAccessSignature;SharedAccessKey=MKJWRZSBncKkmCmM68FF44w4CLj6qzxPqqlGXEzMTWI=";
    private static final String BACKEND_ENDPOINT= "http://arrhythmia-detectionhub2-ns.servicebus.windows.net/";

    private MobileServiceClient mClient;
    private MobileServiceTable<UserData> mUserDataTable;
    public static String input;

    @Override
    public void onRegistered(Context context,  final String gcmregId){

        super.onRegistered(context, gcmregId);

        regid = gcmregId;


        System.out.println("GCM   "+gcmregId);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        new AsyncTask<Void, Void, Void>() {

            protected Void doInBackground(Void... params) {
                try {

                    String profileid = Profile.getCurrentProfile().getId();

                    String groupid = prefs.getString("groupid", null);
                    String id = prefs.getString("id", null);
                    if (groupid == null) {
                        System.out.println("ELSEMYHANDLER");
                        System.out.println(gcmregId);

                        MainActivity.mClient.getPush().register(gcmregId, new String[]{gcmregId});//new String[]{profileid});
                        //hub.register(gcmregId, Profile.getCurrentProfile().getId());
                    } else if (prefs.getBoolean("ismanager", false) == true) {
                        System.out.println("1");
                        MainActivity.mClient.getPush().register(gcmregId, new String[]{id});
                        //hub.register(gcmregId, id);
                    } else if (prefs.getBoolean("ismanager", false) == false) {
                        System.out.println("2");
                        MainActivity.mClient.getPush().register(gcmregId, new String[]{groupid, id});
                        //hub.register(gcmregId, groupid, id);
                    }
                    return null;
                } catch(Exception e) {
                    // handle error
                }
                return null;
            }
        }.execute();



        }



    @Override
    public void onReceive(Context context, Bundle bundle) {
        ctx = context;
        String nhMessage = bundle.getString("message");

        sendNotification(nhMessage);
        System.out.println(nhMessage);



        //managepush.DialogNotify( "Received Notification", nhMessage);
    }

    public void sendNotification(String msg){
        mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(ctx,0,new Intent(ctx,MainActivity.class),0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("New Message")
                .setStyle(new NotificationCompat.BigTextStyle()
                .bigText(msg))
                .setContentText(msg);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID,mBuilder.build());
    }

    public void sendNotification( String msg, Context context) {


        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Notification Hub")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify( NOTIFICATION_ID, mBuilder.build());
    }



    public void sendAlert(String input){
        this.input = input;
        new AlertNotification().execute(input);
    }

    public void sendPush(String input) {

        this.input = input;
        new InvitationTask().execute(input);
    }



}
