package com.example.teachly;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.app.DatePickerDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.widget.DatePicker;

import com.example.teachly.Classes.Activity;
import com.example.teachly.Classes.Class;
import com.example.teachly.Classes.EnumTypeActivity;
import com.example.teachly.Classes.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomAdapterListOfActivities extends BaseAdapter implements AdapterView.OnItemSelectedListener {

    static Context context;
    LayoutInflater inflater;
    ArrayList<Activity> listActivities;

    String activityId, classId, teacherId;

    SharedPreferences sharedPreferences;


    public CustomAdapterListOfActivities (Context appContext,  ArrayList<Activity> listActivities, String classId) {
        context = appContext;
        this.listActivities = listActivities;
        this.classId = classId;
        inflater = LayoutInflater.from(appContext);

        sharedPreferences = context.getSharedPreferences("Teachly", Context.MODE_PRIVATE);
        teacherId = sharedPreferences.getString("uId", "");
    }
    @Override
    public int getCount() {
        if (listActivities == null){
            return 0;
        }
        return listActivities.size();
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

        activityId = this.listActivities.get(position).getId();

        name.setText(this.listActivities.get(position).getName());
        desc.setText(this.listActivities.get(position).getDescription());
        Long timestamp = this.listActivities.get(position).getDueDate();
        Date dateReceived = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = dateFormat.format(dateReceived);

        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
        String hourString = hourFormat.format(dateReceived);

        date.setText(dateString);

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///////////////////////////////////////// Testing the type of the user
                SharedPreferences sharedPreferences = context.getSharedPreferences("Teachly", Context.MODE_PRIVATE);
                String typeUser = sharedPreferences.getString("type", "");
                if (typeUser.equals("Teacher")){
                    View dialogView = inflater.inflate(R.layout.dialog_teacher_view_activity, null);

                    TextView name = dialogView.findViewById(R.id.edtActivityName);
                    TextView desc = dialogView.findViewById(R.id.edtActivityDescription);
                    TextView date = dialogView.findViewById(R.id.edtActivityDate);
                    TextView hour = dialogView.findViewById(R.id.edtActivityTime);

                    name.setText(listActivities.get(position).getName());
                    desc.setText(listActivities.get(position).getDescription());
                    date.setText(dateString);
                    hour.setText(hourString);

                    date.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDatePickerDialog(date);
                        }
                    });

                    hour.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showTimePickerDialog(hour);
                        }
                    });

                    Spinner spinner = dialogView.findViewById(R.id.spinnerActivityType);
                    List<String> typeActivities = new ArrayList<String>();
                    for (EnumTypeActivity value : EnumTypeActivity.values()){
                        typeActivities.add(value.name());
                    }
                    ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, typeActivities);
                    adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapterSpinner);
                    spinner.setOnItemSelectedListener(CustomAdapterListOfActivities.this);

                    Integer selectedIndex = 0;
                    for (int i = 0;i<typeActivities.size();i++) {
                        if (typeActivities.get(i).equals(listActivities.get(position).getType().name())) {
                            selectedIndex = i;
                        }
                    }
                    spinner.setSelection(selectedIndex);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(dialogView);
                    AlertDialog dialog = builder.create();
                    dialog.show();


                    TextView btnSave = dialogView.findViewById(R.id.btnSaveActivity);
                    TextView btnDelete = dialogView.findViewById(R.id.btnDeleteActivity);

                    btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("classes/"+classId+"/activities");
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    ////////// NAO TERMINAMOS DE DELETAR, TA DELETANDO O ULTIMO SEMPRE
                                    if (snapshot.exists()) {
                                        for (DataSnapshot activitySnapshot : snapshot.getChildren()) {
                                            String id = activitySnapshot.getKey();
                                            if (id.equals(activityId)){
                                                Toast.makeText(parent.getContext(), id, Toast.LENGTH_SHORT).show();

                                                // activitySnapshot.getRef().removeValue();
                                            }
                                        }
                                        //for (Activity item : listActivities){
                                            //if (item.getId().equals(activityId)){
                                            //    listActivities.remove(item);
                                            //    notifyDataSetChanged();
                                          //      Toast.makeText(parent.getContext(), "Activity " + item.getName() + " was removed!", Toast.LENGTH_SHORT).show();
                                           //     Class.loadAllClassesByTeacherUId(parent.getContext(), teacherId);
                                           //     dialog.dismiss();
                                          //      return;
                                           // }
                                       // }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    System.out.println("Error: " + error.getMessage());
                                }
                            });
                        }
                    });






                }
                /*if (typeUser.equals("Student")){
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
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }*/


            }
        });
        return convertView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static void showDatePickerDialog(View v) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        EditText date = (EditText) v;
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Do something with the selected date
                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                date.setText(selectedDate);
            }
            }, year, month, day);

        datePickerDialog.show();
    }

    public static void showTimePickerDialog(View v) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        EditText time = (EditText) v;

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String selectedTime = hourOfDay + ":" + minute;
                time.setText(selectedTime);
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }

}
