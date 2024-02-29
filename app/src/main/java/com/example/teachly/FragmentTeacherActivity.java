package com.example.teachly;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTeacherActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTeacherActivity extends Fragment implements AdapterView.OnItemSelectedListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentTeacherActivity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentTeacherActivity.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTeacherActivity newInstance(String param1, String param2) {
        FragmentTeacherActivity fragment = new FragmentTeacherActivity();
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
        View rootView = inflater.inflate(R.layout.fragment_teacher_activity, container, false);

        String[] names = {"Exammmmm","Visit Museum","Homework"};
        String[] descs = {"Exammmmm about conjugations", "We will go to the Museum of Ilusions We will go to the Museum of Ilusions We will go to the Museum of Ilusions We will go to the Museum of Ilusions ", "Do page 43 and 45 of the book"};
        String[] dates = {"3000/04/2024", "10/03/2024", "01/03/2024"};

        ListView listOfActivities = rootView.findViewById(R.id.listOfActivities);
        CustomAdapterListOfActivities adapter = new CustomAdapterListOfActivities(getContext(),names,descs,dates);
        listOfActivities.setAdapter(adapter);

        ImageButton btnAdd = rootView.findViewById(R.id.btn_teacher_add_activity);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = inflater.inflate(R.layout.dialog_teacher_view_activity, null);

                Spinner spinner = dialogView.findViewById(R.id.spinnerActivityType);
                List<String> categories = new ArrayList<String>();
                categories.add("Homework");
                categories.add("Exam");
                categories.add("Trip");
                categories.add("Extra class");
                categories.add("Book to read");
                ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categories);
                adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapterSpinner);
                spinner.setOnItemSelectedListener(FragmentTeacherActivity.this);

                TextView date = dialogView.findViewById(R.id.edtActivityDate);
                TextView hour = dialogView.findViewById(R.id.edtActivityTime);

                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CustomAdapterListOfActivities.showDatePickerDialog(date);
                    }
                });

                hour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CustomAdapterListOfActivities.showTimePickerDialog(hour);
                    }
                });

                TextView btnDelete = dialogView.findViewById(R.id.btnDeleteActivity);
                btnDelete.setVisibility(View.GONE);


                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}