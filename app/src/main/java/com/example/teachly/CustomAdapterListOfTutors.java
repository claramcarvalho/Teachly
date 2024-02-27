package com.example.teachly;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.sql.Blob;

public class CustomAdapterListOfTutors extends BaseAdapter {

    Context context;
    String photos[];
    String names[], emails[], phones[];

    LayoutInflater inflater;

    public CustomAdapterListOfTutors (Context appContext, String[] photos, String[] names, String[] emails, String[] phones) {
        context = appContext;
        this.photos = photos;
        this.names = names;
        this.emails = emails;
        this.phones = phones;
        inflater = LayoutInflater.from(appContext);
    }
    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.list_item_tutors,null);
        ImageView photo = convertView.findViewById(R.id.photoTutorListView);
        TextView name = convertView.findViewById(R.id.txtTutorNameListView);
        TextView email = convertView.findViewById(R.id.txtTutorEmailListView);
        TextView phone = convertView.findViewById(R.id.txtTutorPhoneListView);

        name.setText(this.names[position]);
        email.setText(this.emails[position]);
        phone.setText(this.phones[position]);

        return convertView;
    }
}
