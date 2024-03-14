package com.example.teachly;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.teachly.Classes.Class;

import java.util.ArrayList;

public class CustomAdapterListOfClasses extends BaseAdapter {

    Context context;
    ArrayList<Class> classesReceived = new ArrayList<>();
    LayoutInflater inflater;

    public CustomAdapterListOfClasses (Context appContext, ArrayList<Class> classes) {
        context = appContext;
        this.classesReceived = classes;
        inflater = LayoutInflater.from(appContext);
    }
    @Override
    public int getCount() {
        return classesReceived.size();
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

        Class classItem = classesReceived.get(position);
        String shapeColor = classItem.getColor();
        String nameClass = classItem.getName();
        String numberOfStudents = "0";

        shape.setColorFilter(Color.parseColor(shapeColor), PorterDuff.Mode.SRC_IN);
        name.setText(nameClass);
        nbStudents.setText(numberOfStudents);

        /*item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///////////////////////////////////////// Testing the type of the user
                SharedPreferences sharedPreferences = context.getSharedPreferences("Teachly", Context.MODE_PRIVATE);
                String typeUser = sharedPreferences.getString("type", "");
                if (typeUser.equals("Teacher")){
                    Intent intent = new Intent(context,ClassPageTeacher.class);
                    intent.putExtra("className", className[position]);
                    intent.putExtra("classColor", shapeColor[position]);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                if (typeUser.equals("Student")){
                    Intent intent = new Intent(context,ClassPageStudent.class);
                    intent.putExtra("className", className[position]);
                    intent.putExtra("classColor", shapeColor[position]);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }


            }
        });*/
        return convertView;
    }
}
