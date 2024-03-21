package com.example.teachly;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.teachly.Classes.Class;
import com.example.teachly.Classes.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomAdapterListOfClasses extends BaseAdapter {

    Context context;
    ArrayList<Class> classesReceived = new ArrayList<>();
    String typeUser;
    LayoutInflater inflater;

    public CustomAdapterListOfClasses (Context appContext, ArrayList<Class> classes, String type) {
        context = appContext;
        this.classesReceived = classes;
        this.typeUser = type;
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

        Integer numberOfStudents = classItem.getStudents().size();

        shape.setColorFilter(Color.parseColor(shapeColor), PorterDuff.Mode.SRC_IN);
        name.setText(nameClass);
        nbStudents.setText(String.valueOf(numberOfStudents) + " Students");

        if (typeUser.equals("Student")) {
            nbStudents.setVisibility(View.GONE);
        }
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeUser.equals("Teacher")){
                    Intent intent = new Intent(context,ClassPageTeacher.class);
                    intent.putExtra("classId", classItem.getClassId());
                    intent.putExtra("className", nameClass);
                    intent.putExtra("classDescription", classItem.getDescription());
                    intent.putExtra("classCategory", classItem.getCategory().name());
                    intent.putExtra("classColor", shapeColor);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                if (typeUser.equals("Student")){
                    Intent intent = new Intent(context,ClassPageStudent.class);

                    intent.putExtra("myClass", classItem);

                    DatabaseReference teacherReference = FirebaseDatabase.getInstance().getReference("users");
                    Query findTeacher = teacherReference.orderByChild("userId").equalTo(classItem.getTeacherUId());

                    findTeacher.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String teacherId = "";
                                String password = "";
                                String teacherName = "";
                                String teacherEmail = "";
                                String teacherPhone = "";
                                String type = "";

                                for (DataSnapshot teacherSnapshot : snapshot.getChildren()) {
                                    teacherId = teacherSnapshot.child("userId").getValue(String.class);
                                    password = teacherSnapshot.child("password").getValue(String.class);
                                    teacherName = teacherSnapshot.child("fullName").getValue(String.class);
                                    teacherEmail = teacherSnapshot.child("email").getValue(String.class);
                                    teacherPhone = teacherSnapshot.child("phoneNumber").getValue(String.class);
                                    type = teacherSnapshot.child("type").getValue(String.class);
                                }

                                User myTeacher = new User(teacherId,teacherEmail,password,teacherName,teacherPhone,type);

                                intent.putExtra("myTeacher", myTeacher);

                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        return convertView;
    }
}
