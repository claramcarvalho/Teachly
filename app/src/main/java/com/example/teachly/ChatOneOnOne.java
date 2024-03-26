package com.example.teachly;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cometchat.chat.constants.CometChatConstants;
import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.exceptions.CometChatException;
import com.cometchat.chat.models.Group;
import com.cometchat.chat.models.User;
import com.cometchat.chatuikit.conversationswithmessages.CometChatConversationsWithMessages;
import com.cometchat.chatuikit.creategroup.CometChatCreateGroup;
import com.cometchat.chatuikit.messagelist.CometChatMessageList;
import com.cometchat.chatuikit.messagelist.MessageListConfiguration;
import com.cometchat.chatuikit.messagelist.MessageListStyle;
import com.cometchat.chatuikit.messages.CometChatMessages;
import com.cometchat.chatuikit.messages.MessagesStyle;
import com.cometchat.chatuikit.shared.views.CometChatMessageBubble.CometChatMessageBubble;
import com.example.teachly.Classes.Class;

public class ChatOneOnOne extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_one_on_one);

        MenuBar menuBar = new MenuBar(this);
        menuBar.setupActionBar();

        com.example.teachly.Classes.User myUser = (com.example.teachly.Classes.User) getIntent().getSerializableExtra("userForChat");
        Class myClass = (Class) getIntent().getSerializableExtra("classForChat");

        CometChatMessages newConversation = findViewById(R.id.messageOneonOne);

        if (myUser != null){
            User userToChat = new User(myUser.getUserId(), myUser.getFullName());
            newConversation.setUser(userToChat);

        } else if (myClass != null){
            int membersCount = getIntent().getIntExtra("groupMembers", 0);
            Long groupCreatedAt = getIntent().getLongExtra("groupCreatedAt", 0);
            Long groupUpdatedAt = getIntent().getLongExtra("groupUpdatedAt", 0);
            Long groupJoinedAt = getIntent().getLongExtra("groupJoinedAt", 0);
            String groupScope = getIntent().getStringExtra("groupScope");

            Group group = new Group(myClass.getClassId(), myClass.getName(), CometChatConstants.GROUP_TYPE_PUBLIC, "");
            group.setMembersCount(membersCount);
            group.setIcon(null);
            group.setDescription(null);
            group.setOwner(myClass.getTeacherUId());
            group.setMetadata(null);
            group.setCreatedAt(groupCreatedAt);
            group.setUpdatedAt(groupUpdatedAt);
            group.setHasJoined(true);
            group.setJoinedAt(groupJoinedAt);
            group.setScope(groupScope);
            group.setTags(null);
            newConversation.setGroup(group);
        }

        newConversation.setBackgroundColor(getResources().getColor(R.color.lightGreen));
    }
}