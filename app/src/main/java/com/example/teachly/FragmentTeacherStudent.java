package com.example.teachly;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTeacherStudent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTeacherStudent extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentTeacherStudent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentTeacherStudent.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTeacherStudent newInstance(String param1, String param2) {
        FragmentTeacherStudent fragment = new FragmentTeacherStudent();
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
        View rootView = inflater.inflate(R.layout.fragment_teacher_student, container, false);
        ListView listOfStudents = rootView.findViewById(R.id.listOfStudents);
        String[] names = {"Paul Robert", "John Jones", "Mary Green", "Laura Brown"};
        CustomAdapterListOfStudents adapter = new CustomAdapterListOfStudents(getContext(),names);
        listOfStudents.setAdapter(adapter);

        ImageButton btnAddStudent = rootView.findViewById(R.id.btn_teacher_add_student);
        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = inflater.inflate(R.layout.dialog_teacher_add_student, null);

                TextView btnAddStudentToClass = dialogView.findViewById(R.id.btnAddStudentToClass);
                EditText txtEmail = dialogView.findViewById(R.id.edtAddNewStudentEmail);
                btnAddStudentToClass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(requireContext(), "Adicionado " + txtEmail.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return rootView;
    }
}