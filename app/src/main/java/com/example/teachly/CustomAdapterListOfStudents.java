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

import androidx.appcompat.app.AlertDialog;
import android.widget.DatePicker;

import com.example.teachly.Classes.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CustomAdapterListOfStudents extends BaseAdapter implements AdapterView.OnItemSelectedListener {

    static Context context;
    String studentNames[];
    ArrayList<User> listStudent;
    LayoutInflater inflater;

    public CustomAdapterListOfStudents (Context appContext, ArrayList<User> listStudent) {
        context = appContext;
        this.listStudent = listStudent;
        inflater = LayoutInflater.from(appContext);
    }
    @Override
    public int getCount() {
        if (listStudent == null){
            return 0;
        }
        return listStudent.size();
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
        convertView = inflater.inflate(R.layout.list_item_students,null);
        TextView name = convertView.findViewById(R.id.txtNameStudent);
        RelativeLayout item = convertView.findViewById(R.id.btnGoCheckStudent);

        if (listStudent != null){
            name.setText(this.listStudent.get(position).getFullName());
        }

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View dialogView = inflater.inflate(R.layout.dialog_teacher_view_student, null);
                TextView name = dialogView.findViewById(R.id.studentName);
                TextView email = dialogView.findViewById(R.id.studentEmail);
                TextView btnTalkToStudent = dialogView.findViewById(R.id.btnTeacherTalkToStudent);
                TextView btnRemoveStudent = dialogView.findViewById(R.id.btnTeacherRemoveStudent);

                name.setText(listStudent.get(position).getFullName());
                email.setText(listStudent.get(position).getEmail());

                btnTalkToStudent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Talk to " + studentNames[position], Toast.LENGTH_SHORT).show();
                    }
                });

                btnRemoveStudent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Remove student " + studentNames[position], Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialog.show();
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
