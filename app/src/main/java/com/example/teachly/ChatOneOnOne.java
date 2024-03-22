package com.example.teachly;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.models.User;
import com.cometchat.chatuikit.conversationswithmessages.CometChatConversationsWithMessages;
import com.cometchat.chatuikit.creategroup.CometChatCreateGroup;
import com.cometchat.chatuikit.messagelist.MessageListConfiguration;
import com.cometchat.chatuikit.messages.CometChatMessages;
import com.example.teachly.Classes.Class;

public class ChatOneOnOne extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_one_on_one);

        MenuBar menuBar = new MenuBar(this);
        menuBar.setupActionBar();

        com.example.teachly.Classes.User myUser = (com.example.teachly.Classes.User) getIntent().getSerializableExtra("userForChat");

        User test = new User(myUser.getUserId(), myUser.getFullName());
        CometChatMessages newConversation = findViewById(R.id.messageOneonOne);

        newConversation.setUser(test);
        newConversation.setBackgroundColor(getResources().getColor(R.color.lightGreen));
        //MessageListConfiguration configuration = new MessageListConfiguration();
        //configuration.set
    }
}