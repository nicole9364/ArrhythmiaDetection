package com.project84.ece2015.arrhythmiadetection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.microsoft.windowsazure.messaging.NotificationHub;
import com.microsoft.windowsazure.notifications.NotificationsManager;

/**
 * Created by Nicole on 2/09/2015.
 */
public class ManagePush {
    private String SENDER_ID = "678716789292";
    private GoogleCloudMessaging gcm;
    private NotificationHub hub;
    private String HubName = "arrhythmia-detectionhub";
    private String HubListenConnectionString = "Endpoint=sb://arrhythmia-detectionhub2-ns.servicebus.windows.net/;SharedAccessKeyName=DefaultListenSharedAccessSignature;SharedAccessKey=MKJWRZSBncKkmCmM68FF44w4CLj6qzxPqqlGXEzMTWI=";

    private Activity currentpage;

    public ManagePush(Activity page){
        this.currentpage = page;

        NotificationsManager.handleNotifications(page, SENDER_ID, MyHandler.class);
        gcm = GoogleCloudMessaging.getInstance(page);
        hub = new NotificationHub(HubName, HubListenConnectionString, page);
        //hub.register()
        //registerWithNotificationHubs();

    }

    /*public void registerWithNotificationHubs() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    String regid = gcm.register(SENDER_ID);
                    System.out.println("Registered Successfully"+"RegId : " +
                            hub.register(regid).getRegistrationId());
                    *//*DialogNotify("Registered Successfully","RegId : " +
                            hub.register(regid).getRegistrationId());*//*
                } catch (Exception e) {
                    System.out.println("NO");
                    *//*DialogNotify("Exception",e.getMessage());*//*
                    return e;
                }
                return null;
            }
        }.execute(null, null, null);
    }

    public void DialogNotify(final String title,final String message)
    {
        final AlertDialog.Builder dlg;
        dlg = new AlertDialog.Builder(currentpage);

        currentpage.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(currentpage);
                dlgAlert.setTitle(title);
                dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setMessage(message)
                        .setCancelable(false)
                        .show();


            }
        });
    }*/
}
