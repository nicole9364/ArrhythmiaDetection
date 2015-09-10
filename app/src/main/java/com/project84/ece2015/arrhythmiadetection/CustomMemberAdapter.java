package com.project84.ece2015.arrhythmiadetection;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicole on 27/08/2015.
 */
public class CustomMemberAdapter extends ArrayAdapter<MemberRow> {
    List<MemberRow> rowItemList = new ArrayList<MemberRow>();
    Context context;

    public CustomMemberAdapter(Context context,List<MemberRow> resource){
        super(context,R.layout.row,resource);
        this.context=context;
        this.rowItemList = resource;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.row, parent, false);


        final String id = rowItemList.get(position).getId();
        ProfilePictureView pic = (ProfilePictureView) convertView.findViewById(R.id.fbprofile_pic);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox);


        pic.setProfileId(rowItemList.get(position).getId());
        name.setText(rowItemList.get(position).getProfileName());
        cb.setVisibility(View.GONE);

        return convertView;
    }

}
