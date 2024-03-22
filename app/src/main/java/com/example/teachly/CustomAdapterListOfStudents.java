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

import com.example.teachly.Classes.Class;
import com.example.teachly.Classes.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CustomAdapterListOfStudents extends BaseAdapter implements AdapterView.OnItemSelectedListener {

    static Context context;
    ArrayList<User> listStudent;
    LayoutInflater inflater;
    String classId, teacherId;

    SharedPreferences sharedPreferences;

    public CustomAdapterListOfStudents (Context appContext, ArrayList<User> listStudent, String classId) {
        context = appContext;
        this.listStudent = listStudent;
        this.classId = classId;
        inflater = LayoutInflater.from(appContext);

        sharedPreferences = context.getSharedPreferences("Teachly", Context.MODE_PRIVATE);
        teacherId = sharedPreferences.getString("uId", "");
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
                String userId = listStudent.get(position).getUserId();

                btnTalkToStudent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ChatOneOnOne.class);
                        intent.putExtra("userForChat", listStudent.get(position));
                        context.startActivity(intent);
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialog.show();

                btnRemoveStudent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("classes/"+classId+"/students");
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot studentSnapshot : snapshot.getChildren()) {
                                        String uId = studentSnapshot.getValue(String.class);
                                        if (uId.equals(userId)){
                                            studentSnapshot.getRef().removeValue();
                                        }
                                    }
                                    for (User item : listStudent){
                                        if (item.getUserId().equals(userId)){
                                            listStudent.remove(item);
                                            notifyDataSetChanged();
                                            Toast.makeText(parent.getContext(), "Student " + item.getFullName() + " was removed!", Toast.LENGTH_SHORT).show();
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
        });

        return convertView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
