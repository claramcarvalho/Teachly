package com.example.teachly;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.cometchat.chat.constants.CometChatConstants;
import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.exceptions.CometChatException;
import com.cometchat.chat.models.GroupMember;
import com.example.teachly.Classes.Class;
import com.example.teachly.Classes.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
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

    ArrayList<User> listStudent;
    String classId, userId;

    SharedPreferences sharedPreferences;

    public FragmentTeacherStudent() {
        // Required empty public constructor
    }

    public FragmentTeacherStudent(ArrayList<User> listStudent, String classId) {
        this.listStudent = listStudent;
        this.classId = classId;
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
        sharedPreferences = getContext().getSharedPreferences("Teachly", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("uId", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_teacher_student, container, false);
        ListView listOfStudents = rootView.findViewById(R.id.listOfStudents);
        CustomAdapterListOfStudents adapter = new CustomAdapterListOfStudents(getContext(),listStudent, classId);
        listOfStudents.setAdapter(adapter);

        ImageButton btnAddStudent = rootView.findViewById(R.id.btn_teacher_add_student);
        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = inflater.inflate(R.layout.dialog_teacher_add_student, null);

                TextView btnAddStudentToClass = dialogView.findViewById(R.id.btnAddStudentToClass);
                EditText txtEmail = dialogView.findViewById(R.id.edtAddNewStudentEmail);

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialog.show();
                btnAddStudentToClass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String email = txtEmail.getText().toString().trim();
                        if (email.isEmpty()){
                            Toast.makeText(getContext(), "Please enter a valid e-mail!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (listStudent!=null) {
                            for (User item : listStudent){
                                if (item.getEmail().equals(email)){
                                    Toast.makeText(getContext(), "This user is already in this class!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        }

                        DatabaseReference databaseReferenceUsers = FirebaseDatabase.getInstance().getReference("users");
                        Query findUser = databaseReferenceUsers.orderByChild("email").equalTo(email);

                        findUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                        String type = userSnapshot.child("type").getValue(String.class);

                                        if (!type.equals("Student")){
                                            Toast.makeText(getContext(), "This user is not a student!", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            String uId = userSnapshot.child("userId").getValue(String.class);
                                            String fullname = userSnapshot.child("fullName").getValue(String.class);
                                            String email = userSnapshot.child("email").getValue(String.class);
                                            String password = userSnapshot.child("password").getValue(String.class);
                                            String phoneNumber = userSnapshot.child("phoneNumber").getValue(String.class);
                                            String newType = userSnapshot.child("type").getValue(String.class);
                                            User newUser = new User(uId, email, password, fullname, phoneNumber, newType);

                                            listStudent.add(newUser);

                                            DatabaseReference classesRef = FirebaseDatabase.getInstance().getReference("classes");
                                            DatabaseReference classRef = classesRef.child(classId);

                                            DatabaseReference newStudentRef = classRef.child("students").push();
                                            newStudentRef.setValue(uId);

                                            addStudentToTheGroupChat(classId, uId);
                                            Class.loadAllClassesByTeacherUId(getContext(), userId);

                                            Toast.makeText(getContext(), "Student was added to this class!", Toast.LENGTH_SHORT).show();

                                            adapter.notifyDataSetChanged();
                                            dialog.dismiss();
                                        }
                                    }
                                }
                                else {
                                    Toast.makeText(getContext(), "This user does not exist!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                });
            }
        });

        return rootView;
    }

    public void addStudentToTheGroupChat(String classId, String studentId){
        List<GroupMember> members = new ArrayList<>();
        members.add(new GroupMember(studentId, CometChatConstants.SCOPE_PARTICIPANT));

        CometChat.addMembersToGroup(classId, members, null, new CometChat.CallbackListener<HashMap<String, String>>() {
            @Override
            public void onSuccess(HashMap<String, String> stringStringHashMap) {
                System.out.println("Student was added to the chat group");
            }

            @Override
            public void onError(CometChatException e) {
                System.out.println("Student was not added to the chat group");
            }
        });
    }
}