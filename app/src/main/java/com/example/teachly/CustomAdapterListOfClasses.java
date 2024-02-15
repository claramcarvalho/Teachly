package com.example.teachly;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAdapterListOfClasses extends BaseAdapter {

    Context context;
    String shapeColor[], className[], numberOfStudents[];

    LayoutInflater inflater;

    public CustomAdapterListOfClasses (Context appContext, String[] shapeColor, String[] className, String[] numberOfStudents) {
        context = appContext;
        this.shapeColor = shapeColor;
        this.className = className;
        this.numberOfStudents = numberOfStudents;
        inflater = LayoutInflater.from(appContext);
    }
    @Override
    public int getCount() {
        return className.length;
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
        convertView = inflater.inflate(R.layout.list_item_classes,null);
        ImageView shape = convertView.findViewById(R.id.shape);
        TextView name = convertView.findViewById(R.id.txtNameClass);
        TextView nbStudents = convertView.findViewById(R.id.textNumberOfStudents);
        RelativeLayout item = convertView.findViewById(R.id.btnGoCheckClass);

        shape.setColorFilter(Color.parseColor(shapeColor[position]), PorterDuff.Mode.SRC_IN);
        name.setText(this.className[position]);
        nbStudents.setText(this.numberOfStudents[position]);

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(), className[position] ,Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }
}
