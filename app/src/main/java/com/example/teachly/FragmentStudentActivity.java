package com.example.teachly;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentStudentActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentStudentActivity extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentStudentActivity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentStudentActivity.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentStudentActivity newInstance(String param1, String param2) {
        FragmentStudentActivity fragment = new FragmentStudentActivity();
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

        View rootView = inflater.inflate(R.layout.fragment_student_activity, container, false);

        String[] names = {"Exam","Visit Museum","Homework"};
        String[] descs = {"Exam about conjugations", "We will go to the Museum of Ilusions We will go to the Museum of Ilusions We will go to the Museum of Ilusions We will go to the Museum of Ilusions ", "Do page 43 and 45 of the book"};
        String[] dates = {"30/04/2024", "10/03/2024", "01/03/2024"};

        ListView listOfActivities = rootView.findViewById(R.id.listOfActivities);
        //CustomAdapterListOfActivities adapter = new CustomAdapterListOfActivities(getContext(),names,descs,dates);
        //listOfActivities.setAdapter(adapter);

        // Inflate the layout for this fragment
        return rootView;
    }
}