package com.example.teachly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.teachly.Classes.Class;
import com.example.teachly.Classes.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class CustomAdapterListOfTutors extends BaseAdapter {

    Context context;
    ArrayList<Class> list;
    LayoutInflater inflater;

    User teacher;

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
        className.setText(this.list.get(position).getName());
        classCategory.setText(this.list.get(position).getCategory().name());

        String teacherId = this.list.get(position).getTeacherUId();

        RelativeLayout layout = convertView.findViewById(R.id.btnGoCheckTutor);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_view_teacher, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

                Query findTutor = databaseReference.orderByChild("userId").equalTo(teacherId);
                findTutor.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                String fullNameFound = childSnapshot.child("fullName").getValue(String.class);
                                String emailFound = childSnapshot.child("email").getValue(String.class);
                                String phoneFound = childSnapshot.child("phoneNumber").getValue(String.class);
                                TextView fullname = dialogView.findViewById(R.id.nameTeacherViewTeacher);
                                TextView email = dialogView.findViewById(R.id.emailTeacherViewTeacher);
                                TextView phone = dialogView.findViewById(R.id.phoneTeacherViewTeacher);
                                fullname.setText(fullNameFound);
                                email.setText(emailFound);
                                phone.setText(phoneFound);
                                dialog.show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        return convertView;
    }
}
