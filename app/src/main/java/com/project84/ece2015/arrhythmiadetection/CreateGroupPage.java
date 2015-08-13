package com.project84.ece2015.arrhythmiadetection;


import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Nicole on 12/08/2015.
 */
public class CreateGroupPage extends BaseActivity  {

    Spinner spinner;
    private String[] conditions= {"Tachycardia","Bradycardia"};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creategroup);

        spinner = (Spinner) findViewById(R.id.heartCondition);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateGroupPage.this, android.R.layout.simple_spinner_dropdown_item) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount()-1; // you dont display last item. It is used as hint.
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
                String selected_condition = (String) parent.getItemAtPosition(position);
                Log.d("condition selected:",selected_condition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateGroupPage.this,android.R.layout.simple_spinner_dropdown_item, conditions);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected_condition = (String) parent.getItemAtPosition(position);
                Log.d("condition selected:",selected_condition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/





    }

}
