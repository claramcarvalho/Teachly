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

import androidx.appcompat.app.AlertDialog;

public class CustomAdapterListOfActivities extends BaseAdapter {

    Context context;
    String activityName[], activityDesc[], activityDueDate[];

    LayoutInflater inflater;

    public CustomAdapterListOfActivities (Context appContext, String[] activityName, String[] activityDesc, String[] activityDueDate) {
        context = appContext;
        this.activityName = activityName;
        this.activityDesc = activityDesc;
        this.activityDueDate = activityDueDate;
        inflater = LayoutInflater.from(appContext);
    }
    @Override
    public int getCount() {
        return activityName.length;
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
        convertView = inflater.inflate(R.layout.list_item_activity,null);
        TextView name = convertView.findViewById(R.id.textNameActivity);
        TextView desc = convertView.findViewById(R.id.textActivityDescription);
        TextView date = convertView.findViewById(R.id.textActivityDueDate);
        RelativeLayout item = convertView.findViewById(R.id.btnGoCheckActivity);

        name.setText(this.activityName[position]);
        desc.setText(this.activityDesc[position]);
        date.setText(this.activityDueDate[position]);

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///////////////////////////////////////// Testing the type of the user
                SharedPreferences sharedPreferences = context.getSharedPreferences("Teachly", Context.MODE_PRIVATE);
                String typeUser = sharedPreferences.getString("type", "");
                if (typeUser.equals("Teacher")){
                    Intent intent = new Intent(context,ClassPageTeacher.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                if (typeUser.equals("Student")){
                    View dialogView = inflater.inflate(R.layout.dialog_student_view_activity, null);

                    TextView name = dialogView.findViewById(R.id.textNameActivityStudentModal);
                    TextView desc = dialogView.findViewById(R.id.textActivityDescriptionStudentModal);
                    TextView date = dialogView.findViewById(R.id.textDateActivityStudentModal);
                    TextView hour = dialogView.findViewById(R.id.textHourActivityStudentModal);
                    TextView tag = dialogView.findViewById(R.id.activity_tag);

                    name.setText(activityName[position]);
                    desc.setText(activityDesc[position]);
                    date.setText(activityDueDate[position]);
                    hour.setText("17:00");
                    tag.setText("Homework");

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(dialogView);
                    //builder.setTitle("Search new tutors");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }


            }
        });
        return convertView;
    }
}
