package com.example.teachly;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentStudentClassDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentStudentClassDetail extends Fragment {

    String classDescription, classCategory, teacherName, teacherEmail, teacherPhone, classId, userId;
    SharedPreferences sharedPreferences;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentStudentClassDetail() {
        // Required empty public constructor
    }

    public FragmentStudentClassDetail(String classDescription, String classCategory, String teacherName, String teacherEmail, String teacherPhone, String classId) {
        this.classDescription = classDescription;
        this.classCategory = classCategory;
        this.teacherName = teacherName;
        this.teacherEmail = teacherEmail;
        this.teacherPhone = teacherPhone;
        this.classId = classId;
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentStudentClassDetail.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentStudentClassDetail newInstance(String param1, String param2) {
        FragmentStudentClassDetail fragment = new FragmentStudentClassDetail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        sharedPreferences = getContext().getSharedPreferences("Teachly", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("uId", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_student_class_detail, container, false);

        TextView classDesc = rootView.findViewById(R.id.class_description);
        TextView classTag = rootView.findViewById(R.id.class_tag);
        TextView teacherName = rootView.findViewById(R.id.tutorNameClassDetails);
        TextView teacherEmail = rootView.findViewById(R.id.tutorEmailClassDetail);
        TextView teacherPhone = rootView.findViewById(R.id.tutorPhoneClassDetail);
        TextView btnTalkToTeacher = rootView.findViewById(R.id.btnStudentTalkTeacher);
        TextView btnDropClass = rootView.findViewById(R.id.btnStudentDropClass);
        classDesc.setText(this.classDescription);
        classTag.setText(this.classCategory);
        teacherName.setText(this.teacherName);
        teacherEmail.setText(this.teacherEmail);
        teacherPhone.setText(this.teacherPhone);

        btnTalkToTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnDropClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReferenceClasses = FirebaseDatabase.getInstance().getReference("classes/"+classId+"/students");
                databaseReferenceClasses.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot studentSnapshot : snapshot.getChildren()) {
                                String uId = studentSnapshot.getValue(String.class);
                                if (uId.equals(userId)){
                                    studentSnapshot.getRef().removeValue();
                                    Toast.makeText(getContext(), "You were removed from this class successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), HomeStudent.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                    return;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        return rootView;
    }
}