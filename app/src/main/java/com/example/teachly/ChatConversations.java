package com.example.teachly;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.cometchat.chatuikit.contacts.ContactsConfiguration;
import com.cometchat.chatuikit.conversations.ConversationsConfiguration;
import com.cometchat.chatuikit.conversationswithmessages.CometChatConversationsWithMessages;
import com.cometchat.chatuikit.messageheader.MessageHeaderConfiguration;
import com.cometchat.chatuikit.messages.MessagesConfiguration;
import com.cometchat.chatuikit.users.UsersConfiguration;

public class ChatConversations extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CometChatConversationsWithMessages conversations = new CometChatConversationsWithMessages(this);
        setContentView(conversations);

    }
}