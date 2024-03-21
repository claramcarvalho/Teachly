package com.example.teachly;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cometchat.chat.models.User;
import com.cometchat.chatuikit.conversationswithmessages.CometChatConversationsWithMessages;
import com.cometchat.chatuikit.messagelist.MessageListConfiguration;
import com.cometchat.chatuikit.messages.CometChatMessages;

public class ChatOneOnOne extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_one_on_one);
        User test = new User("superhero1","Iron Man");
        CometChatMessages newConversation = findViewById(R.id.messageOneonOne);
        newConversation.setUser(test);
        newConversation.setBackgroundColor(getResources().getColor(R.color.lightGreen));
        //MessageListConfiguration configuration = new MessageListConfiguration();
        //configuration.set
    }
}