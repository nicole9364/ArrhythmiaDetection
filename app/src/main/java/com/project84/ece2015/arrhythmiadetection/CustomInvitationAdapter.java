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
 * Created by Nicole on 28/08/2015.
 */
public class CustomInvitationAdapter extends ArrayAdapter<Invitation> {
    List<Invitation> invitationList = new ArrayList<Invitation>();
    Context context;

    public CustomInvitationAdapter(Context context,List<Invitation> resource){
        super(context,R.layout.row,resource);
        this.context=context;
        this.invitationList = resource;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.row, parent, false);


        final String id = invitationList.get(position).getId();
        ProfilePictureView pic = (ProfilePictureView) convertView.findViewById(R.id.fbprofile_pic);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox);
        TextView msg = (TextView) convertView.findViewById(R.id.message);



        pic.setProfileId(invitationList.get(position).getId());
        name.setText(invitationList.get(position).getProfileName());
        cb.setVisibility(View.GONE);
        msg.setText("Please join my group: "+ invitationList.get(position).getGrpName());

        return convertView;
    }
}
