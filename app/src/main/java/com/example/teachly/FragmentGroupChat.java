package com.example.teachly;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cometchat.chat.constants.CometChatConstants;
import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.core.ConversationsRequest;
import com.cometchat.chat.exceptions.CometChatException;
import com.cometchat.chat.models.AppEntity;
import com.cometchat.chat.models.Conversation;
import com.cometchat.chat.models.Group;
import com.cometchat.chatuikit.conversations.CometChatConversations;
import com.cometchat.chatuikit.groupswithmessages.CometChatGroupsWithMessages;
import com.cometchat.chatuikit.messages.CometChatMessages;
import com.cometchat.chatuikit.shared.resources.utils.item_clickListener.OnItemClickListener;
import com.example.teachly.Classes.Class;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentGroupChat#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentGroupChat extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String userId;
    SharedPreferences sharedPreferences;
    Class myClass;

    public FragmentGroupChat() {
        // Required empty public constructor
    }
    public FragmentGroupChat(Class myClass) {
        this.myClass = myClass;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentTeacherChat.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentGroupChat newInstance(String param1, String param2) {
        FragmentGroupChat fragment = new FragmentGroupChat();
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
        View rootView = inflater.inflate(R.layout.fragment_group_chat, container, false);

        CometChatConversations conversations = rootView.findViewById(R.id.conversations);
        conversations.setBackground(getResources().getColor(R.color.lightGreen));
        List<String> tags = new ArrayList<>();
        tags.add(myClass.getClassId());
        ConversationsRequest.ConversationsRequestBuilder conversationsRequest = new ConversationsRequest.ConversationsRequestBuilder()
                .setLimit(1)
                .setGroupTags(tags);

        conversations.setConversationsRequestBuilder(conversationsRequest);


        conversations.setItemClickListener(new OnItemClickListener<Conversation>() {
            @Override
            public void OnItemClick(Conversation conversation, int i) {
                AppEntity appEntity = conversation.getConversationWith();
                Group group = (Group) appEntity;
                System.out.println(group);
                Class classToChat = new Class(group.getGuid(), group.getName(), null, myClass.getTeacherUId(), null, null);
                Intent intent = new Intent(getActivity(), ChatOneOnOne.class);
                intent.putExtra("classForChat", classToChat);
                intent.putExtra("groupMembers", group.getMembersCount());
                intent.putExtra("groupCreatedAt", group.getCreatedAt());
                intent.putExtra("groupUpdatedAt", group.getUpdatedAt());
                intent.putExtra("groupJoinedAt", group.getJoinedAt());
                intent.putExtra("groupScope", group.getScope());
                startActivity(intent);

            }
        });
        return rootView;
    }
}