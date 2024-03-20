package com.example.teachly;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentStudentClassDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentStudentClassDetail extends Fragment {

    String classDescription, classCategory, teacherName, teacherEmail, teacherPhone, classId;

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
        classDesc.setText(this.classDescription);
        classTag.setText(this.classCategory);
        teacherName.setText(this.teacherName);
        teacherEmail.setText(this.teacherEmail);
        teacherPhone.setText(this.teacherPhone);

        return rootView;
    }
}