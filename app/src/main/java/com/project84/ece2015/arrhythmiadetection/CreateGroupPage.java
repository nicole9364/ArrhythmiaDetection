package com.project84.ece2015.arrhythmiadetection;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.gson.GsonBuilder;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by Nicole on 12/08/2015.
 */
public class CreateGroupPage extends BaseActivity  {

    Spinner spinner;
    private Button completeBtn;
    private String grpName;
    private String phoneNum;
    private String selected_condition;
    private int selected_condition_position;
    private ArrayAdapter<String> adapter;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    private MobileServiceClient mClient;
    private MobileServiceTable<UserData> mUserDataTable;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creategroup);

        try {
            mClient = new MobileServiceClient(
                    "https://arrhythmiadetection.azure-mobile.net/",
                    "LPElhTMLVPNSIgciYyGYEGNQJpJtqs38",
                    CreateGroupPage.this);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mUserDataTable = mClient.getTable(UserData.class);


        grpName = ((EditText) findViewById(R.id.grpname)).getText().toString();
        phoneNum = ((EditText) findViewById(R.id.phoneNo)).getText().toString();


        spinner = (Spinner) findViewById(R.id.heartCondition);
        adapter = new ArrayAdapter<String>(CreateGroupPage.this, android.R.layout.simple_spinner_dropdown_item) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("Tachycardia");
        adapter.add("Bradycardia");
        adapter.add("Choose One.."); //This is the text that will be displayed as hint.


        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount()); //set the hint the default selection so it appears on launch.
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_condition_position = position;
                selected_condition = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_condition = null;
            }
        });

        completeBtn = (Button) findViewById(R.id.complete);
        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grpName = ((EditText) findViewById(R.id.grpname)).getText().toString();
                phoneNum = ((EditText) findViewById(R.id.phoneNo)).getText().toString();


                if (grpName.trim().length() == 0) {
                    Toast.makeText(CreateGroupPage.this, "Please fill out all required fields", Toast.LENGTH_LONG).show();
                } else if ((!phoneNum.matches("[0-9]+")) | phoneNum.length() < 9) {
                    Toast.makeText(CreateGroupPage.this, "Mobile number must be 9 digit or more", Toast.LENGTH_LONG).show();
                } else if (selected_condition_position == adapter.getCount()) {
                    Toast.makeText(CreateGroupPage.this, "You must select your heart condition", Toast.LENGTH_LONG).show();
                }



                //check if there exists identical groupName
                mUserDataTable.where().field("groupid").eq(grpName).execute(new TableQueryCallback<UserData>() {
                    @Override
                    public void onCompleted(List<UserData> result, int count, Exception exception, ServiceFilterResponse response) {
                        if (exception == null) {
                            if (result.size() == 1 | result.size() > 1) {
                                Toast.makeText(CreateGroupPage.this, "Group Name already exists. Please choose another group name.", Toast.LENGTH_LONG).show();
                            } else if (result.size() == 0) {
                                createNewGroup(grpName,phoneNum,selected_condition);
                            }

                        }
                    }

                });


            }
        });








    }

    public void createNewGroup(final String groupName, final String phone, final String condition) {
        mUserDataTable.where().field("id").eq(Profile.getCurrentProfile().getId()).execute(new TableQueryCallback<UserData>() {
            @Override
            public void onCompleted(List<UserData> result, int count, Exception exception, ServiceFilterResponse response) {
                if (exception == null){
                   /* for (UserData data : result){

                        System.out.println("HI"+groupName+"    "+data.getId()+"   " +data.getGroupId());
                        data.setGroupId(groupName);
                        data.setIsManager(true);
                    }*/

                    result.get(0).setIsManager(true);
                    result.get(0).setGroupId(groupName);
                    result.get(0).setPhonenumb(phone);
                    result.get(0).setCondition(condition);

                    mUserDataTable.update(result.get(0));

                    Intent intent = new Intent(CreateGroupPage.this, HomePage.class);
                    startActivity(intent);

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("id",Profile.getCurrentProfile().getId());
                    editor.putString("groupid",groupName);
                    editor.putString("phone",phone);
                    editor.putString("condition",condition);

                    editor.commit();

                    finish();

                }else{
                    System.out.println(exception);
                }
            }
        });
    }

}
