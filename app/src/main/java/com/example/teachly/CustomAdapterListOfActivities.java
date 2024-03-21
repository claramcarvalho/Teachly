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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomAdapterListOfActivities extends BaseAdapter implements AdapterView.OnItemSelectedListener {

    static Context context;
    LayoutInflater inflater;
    ArrayList<Activity> listActivities;

    String classId, teacherId;

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

        name.setText(this.listActivities.get(position).getName());
        desc.setText(this.listActivities.get(position).getDescription());
        Long timestamp = this.listActivities.get(position).getDueDate();
        Date dateReceived = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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

                String activityId = listActivities.get(position).getId();

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

                    btnSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("classes/"+classId+"/activities/"+activityId);
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        if (isActivityNameChanged(databaseReference,name.getText().toString().trim(),snapshot.child("name").getValue(String.class),position)) {
                                            Toast.makeText(context, "Activity Name was updated!", Toast.LENGTH_SHORT).show();
                                        }
                                        if (isActivityDescriptionChanged(databaseReference,desc.getText().toString().trim(),snapshot.child("description").getValue(String.class),position)) {
                                            Toast.makeText(context, "Activity Description was updated!", Toast.LENGTH_SHORT).show();
                                        }
                                        if (isActivityDateTimeChanged(databaseReference,date.getText().toString().trim(),hour.getText().toString().trim(),snapshot.child("dueDate").getValue(Long.class),position)) {
                                            Toast.makeText(context, "Activity Date was updated!", Toast.LENGTH_SHORT).show();
                                        }
                                        if (isActivityTypeChanged(databaseReference,spinner.getSelectedItem().toString(),snapshot.child("type").getValue(String.class),position)) {
                                            Toast.makeText(context, "Activity Type was updated!", Toast.LENGTH_SHORT).show();
                                        }
                                        notifyDataSetChanged();
                                        dialog.dismiss();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    System.out.println("Error: " + error.getMessage());
                                }
                            });
                        }
                    });

                    btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("classes/"+classId+"/activities");
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        for (DataSnapshot activitySnapshot : snapshot.getChildren()) {
                                            String id = activitySnapshot.getKey();
                                            if (id.equals(activityId)){
                                                activitySnapshot.getRef().removeValue();
                                            }
                                        }
                                        for (Activity item : listActivities){
                                            if (item.getId().equals(activityId)){
                                                listActivities.remove(item);
                                                notifyDataSetChanged();
                                                Toast.makeText(parent.getContext(), "Activity " + item.getName() + " was removed!", Toast.LENGTH_SHORT).show();
                                                Class.loadAllClassesByTeacherUId(parent.getContext(), teacherId);
                                                dialog.dismiss();
                                                return;
                                            }
                                        }
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
                if (typeUser.equals("Student")){
                    View dialogView = inflater.inflate(R.layout.dialog_student_view_activity, null);

                    TextView name = dialogView.findViewById(R.id.textNameActivityStudentModal);
                    TextView desc = dialogView.findViewById(R.id.textActivityDescriptionStudentModal);
                    TextView date = dialogView.findViewById(R.id.textDateActivityStudentModal);
                    TextView hour = dialogView.findViewById(R.id.textHourActivityStudentModal);
                    TextView tag = dialogView.findViewById(R.id.activity_tag);

                    name.setText(listActivities.get(position).getName());
                    desc.setText(listActivities.get(position).getDescription());
                    date.setText(dateString);
                    hour.setText(hourString);
                    tag.setText(listActivities.get(position).getType().name());

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(dialogView);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
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

    private boolean isActivityNameChanged (DatabaseReference reference, String newName, String oldName, Integer position){
        if (!newName.equals(oldName)) {
            reference.child("name").setValue(newName);
            listActivities.get(position).setName(newName);
            return true;
        }
        return false;
    }
    private boolean isActivityDescriptionChanged (DatabaseReference reference, String newDesc, String oldDesc, Integer position){
        if (!newDesc.equals(oldDesc)) {
            reference.child("description").setValue(newDesc);
            listActivities.get(position).setDescription(newDesc);
            return true;
        }
        return false;
    }
    private boolean isActivityDateTimeChanged (DatabaseReference reference, String newDate, String newTime, Long oldDate, Integer position){
        String dateTimeStr = newDate + " " + newTime;
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        try {
            Date combinedDateTime = format.parse(dateTimeStr);
            long timestamp = combinedDateTime.getTime();
            if (timestamp != oldDate) {
                reference.child("dueDate").setValue(timestamp);
                listActivities.get(position).setDueDate(timestamp);
                return true;
            }
            return false;
        } catch (ParseException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    private boolean isActivityTypeChanged (DatabaseReference reference, String newType, String oldType, Integer position){
        if (!newType.equals(oldType)) {
            reference.child("type").setValue(newType);
            listActivities.get(position).setType(EnumTypeActivity.valueOf(newType));
            return true;
        }
        return false;
    }

}
