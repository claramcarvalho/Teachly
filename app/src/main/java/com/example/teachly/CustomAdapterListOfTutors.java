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

import androidx.annotation.NonNull;

import com.example.teachly.Classes.Class;
import com.example.teachly.Classes.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Blob;
import java.util.ArrayList;

public class CustomAdapterListOfTutors extends BaseAdapter {

    Context context;
    ArrayList<Class> list;
    String nameOfTeacher, teacherEmail, teacherPhone;
    LayoutInflater inflater;

    public CustomAdapterListOfTutors (Context appContext, ArrayList<Class> list) {
        context = appContext;
        this.list = list;
        inflater = LayoutInflater.from(appContext);
    }
    @Override
    public int getCount() {
        return list.size();
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
        TextView className = convertView.findViewById(R.id.txtClassNameListView);
        TextView classCategory = convertView.findViewById(R.id.txtClassTypeListView);
        TextView name = convertView.findViewById(R.id.txtTutorNameListView);
        TextView email = convertView.findViewById(R.id.txtTutorEmailListView);
        TextView phone = convertView.findViewById(R.id.txtTutorPhoneListView);

        className.setText(this.list.get(position).getName());
        classCategory.setText(this.list.get(position).getCategory().name());

        String teacherId = this.list.get(position).getTeacherUId();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query findTutor = reference.orderByChild("uId").equalTo(teacherId);

        findTutor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    nameOfTeacher = snapshot.child("fullName").getValue(String.class);
                    teacherEmail = snapshot.child("email").getValue(String.class);
                    teacherPhone = snapshot.child("phoneNumber").getValue(String.class);

                    name.setText(nameOfTeacher);
                    email.setText(teacherEmail);
                    phone.setText(teacherPhone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return convertView;
    }
}
