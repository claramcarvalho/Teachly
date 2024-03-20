package com.example.teachly;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teachly.Classes.Activity;
import com.example.teachly.Classes.Class;
import com.example.teachly.Classes.EnumCategoryClass;
import com.example.teachly.Classes.EnumTypeActivity;
import com.example.teachly.Classes.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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

    String classId, userId;
    SharedPreferences sharedPreferences;
    ArrayList<Activity> listActivities;

    public FragmentTeacherActivity() {
        // Required empty public constructor
    }
    public FragmentTeacherActivity(String classId, ArrayList<Activity> listActivities) {
        this.classId = classId;
        this.listActivities = listActivities;
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
        sharedPreferences = getContext().getSharedPreferences("Teachly", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("uId", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_teacher_activity, container, false);

        ListView listOfActivities = rootView.findViewById(R.id.listOfActivities);
        CustomAdapterListOfActivities adapter = new CustomAdapterListOfActivities(getContext(),listActivities, classId);
        listOfActivities.setAdapter(adapter);

        ImageButton btnAdd = rootView.findViewById(R.id.btn_teacher_add_activity);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = inflater.inflate(R.layout.dialog_teacher_view_activity, null);

                Spinner spinner = dialogView.findViewById(R.id.spinnerActivityType);
                List<String> typeActivities = new ArrayList<String>();
                for (EnumTypeActivity value : EnumTypeActivity.values()){
                    typeActivities.add(value.name());
                }
                ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, typeActivities);
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

                EditText edtName = dialogView.findViewById(R.id.edtActivityName);
                EditText edtDescription = dialogView.findViewById(R.id.edtActivityDescription);
                TextView btnSave = dialogView.findViewById(R.id.btnSaveActivity);

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialog.show();

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = edtName.getText().toString().trim();
                        String desc = edtDescription.getText().toString().trim();
                        String type = spinner.getSelectedItem().toString();
                        String dateStr = date.getText().toString();
                        String timeStr = hour.getText().toString();
                        String dateTimeStr = dateStr + " " + timeStr;
                        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

                        if (name.isEmpty()){
                            Toast.makeText(getContext(), "Please enter a name!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (desc.isEmpty()){
                            Toast.makeText(getContext(), "Please enter a description!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (dateStr.isEmpty() || timeStr.isEmpty()){
                            Toast.makeText(getContext(), "Please enter date and time correctly!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try {
                            Date combinedDateTime = format.parse(dateTimeStr);
                            long timestamp = combinedDateTime.getTime();

                            DatabaseReference classesRef = FirebaseDatabase.getInstance().getReference("classes");
                            DatabaseReference classRef = classesRef.child(classId);

                            Activity newActivity = new Activity(name, desc, timestamp, EnumTypeActivity.valueOf(type));

                            DatabaseReference newActivityRef = classRef.child("activities").push();
                            newActivityRef.setValue(newActivity);

                            Toast.makeText(getContext(), "Activity " + name + " was saved!", Toast.LENGTH_SHORT).show();
                            Class.loadAllClassesByTeacherUId(getContext(), userId);

                            listActivities.add(newActivity);
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();

                        } catch (ParseException e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
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