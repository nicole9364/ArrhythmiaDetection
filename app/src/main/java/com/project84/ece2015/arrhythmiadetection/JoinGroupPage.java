package com.project84.ece2015.arrhythmiadetection;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;
import com.microsoft.windowsazure.notifications.NotificationsManager;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicole on 28/08/2015.
 */
public class JoinGroupPage extends BaseActivity {
    private Button b1;
    private Button b2;
    private TextView txt;
    private ListView listview;
    private ProgressBar spinner;

    private MobileServiceClient mClient;
    private MobileServiceTable<UserData> mUserDataTable;
    private List<Invitation> invitationList = new ArrayList<Invitation>();
    private CustomInvitationAdapter adapter;
    private List<UserData> senderList = new ArrayList<UserData>();
    private UserData userSender;
    private PopupWindow popup;
    private String manager;
    private String group;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.managegroup);
        FacebookSdk.sdkInitialize(getApplicationContext());

        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);
        b1 = (Button) findViewById(R.id.send);
        b1.setVisibility(View.GONE);
        b2 = (Button) findViewById(R.id.invite);
        b2.setVisibility(View.GONE);

        txt = (TextView) findViewById(R.id.invitableFriends);
        txt.setText("Invitation From:");

        listview = (ListView) findViewById(R.id.listView);

        try {
            mClient = new MobileServiceClient(
                    "https://arrhythmia-detection.azure-mobile.net/",
                    "XMdXwbdExMrUFlySSayXtOFTqnpMot14",
                    this
            );


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mUserDataTable = mClient.getTable(UserData.class);

        //get invitation
        mUserDataTable.where().field("id").eq(Profile.getCurrentProfile().getId()).execute(new TableQueryCallback<UserData>() {
            @Override
            public void onCompleted(List<UserData> result, int count, Exception exception, ServiceFilterResponse response) {
                String sender = null;
                //get list of my group members

                for (UserData member : result) {
                    if (member.getInvitation() == null) {
                        spinner.setVisibility(View.GONE);
                        txt.setText("No Invitation!");
                    } else {
                        sender = member.getInvitation();
                    }
                }

                if (!sender.equals(null)) {
                    mUserDataTable.where().field("id").eq(sender).execute(new TableQueryCallback<UserData>() {
                        @Override
                        public void onCompleted(List<UserData> result, int count, Exception exception, ServiceFilterResponse response) {
                            userSender = result.get(0);
                            invitationList.add(new Invitation(userSender.getId(),userSender.getName(),userSender.getGroupId()));
                            spinner.setVisibility(View.GONE);
                            adapter = new CustomInvitationAdapter(JoinGroupPage.this, invitationList);
                            listview.setAdapter(adapter);
                        }
                    });


                }
            }
        });


        listview.setClickable(true);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                manager = adapter.getItem(position).getProfileName();
                group = adapter.getItem(position).getGrpName();
                LayoutInflater layoutInflater
                        = (LayoutInflater) getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.popup, null);

                popup = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                popup.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                TextView popupmsg = (TextView) popupView.findViewById(R.id.popupmsg);
                String msg = "Are you sure you want to accept invitation from " + manager + " to join " + group + " ?";
                popupmsg.setText(msg);

                Button cancelbtn = (Button) popupView.findViewById(R.id.cancelBtn);
                cancelbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup.dismiss();
                    }
                });

                Button popupbtn = (Button) popupView.findViewById(R.id.acceptBtn);
                popupbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //update groupid, ismanager
                        mUserDataTable.where().field("id").eq(Profile.getCurrentProfile().getId()).execute(new TableQueryCallback<UserData>() {
                            @Override
                            public void onCompleted(List<UserData> result, int count, Exception exception, ServiceFilterResponse response) {
                                if (exception == null) {

                                    result.get(0).setIsManager(false);
                                    result.get(0).setGroupId(group);
                                    //result.get(0).setPhonenumb(phone);

                                    mUserDataTable.update(result.get(0));

                                    Intent intent = new Intent(JoinGroupPage.this, MemberHomePage.class);
                                    startActivity(intent);

                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("id", Profile.getCurrentProfile().getId());
                                    editor.putString("groupid", group);
                                    editor.putBoolean("ismanager",false);
                                    //editor.putString("phone",phone);

                                    editor.commit();

                                    NotificationsManager.handleNotifications(JoinGroupPage.this, "678716789292", MyHandler.class);

                                    finish();

                                } else {
                                    System.out.println(exception);
                                }
                            }
                        });

                    }
                });
            }
        });



    }

}
