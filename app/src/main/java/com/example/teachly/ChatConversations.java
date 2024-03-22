package com.example.teachly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cometchat.chat.models.AppEntity;
import com.cometchat.chat.models.Conversation;
import com.cometchat.chat.models.User;
import com.cometchat.chatuikit.contacts.ContactsConfiguration;
import com.cometchat.chatuikit.conversations.CometChatConversations;
import com.cometchat.chatuikit.conversations.ConversationsConfiguration;
import com.cometchat.chatuikit.conversations.ConversationsStyle;
import com.cometchat.chatuikit.conversationswithmessages.CometChatConversationsWithMessages;
import com.cometchat.chatuikit.messageheader.MessageHeaderConfiguration;
import com.cometchat.chatuikit.messages.MessagesConfiguration;
import com.cometchat.chatuikit.shared.resources.utils.item_clickListener.OnItemClickListener;
import com.cometchat.chatuikit.shared.views.CometChatStatusIndicator.StatusIndicatorStyle;
import com.cometchat.chatuikit.users.UsersConfiguration;

public class ChatConversations extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_conversations);

        CometChatConversations conversations = findViewById(R.id.conversation);
        conversations.setBackground(getResources().getColor(R.color.lightGreen));

        conversations.setItemClickListener(new OnItemClickListener<Conversation>() {
            @Override
            public void OnItemClick(Conversation conversation, int i) {
                AppEntity appEntity = conversation.getConversationWith();
                User userFromConversation = (User) appEntity;
                com.example.teachly.Classes.User ourUser = new com.example.teachly.Classes.User();
                ourUser.setUserId(userFromConversation.getUid());
                ourUser.setFullName(userFromConversation.getName());
                Intent intent = new Intent(ChatConversations.this, ChatOneOnOne.class);
                intent.putExtra("userForChat", ourUser);
                startActivity(intent);

                //AJUSTAR PRA QUANDO A PESSOA CLICAR EM UM GRUPO AO INVES DE UMA CONVERSA PRIVADA
            }
        });

        MenuBar menuBar = new MenuBar(this);
        menuBar.setupActionBar();

    }
}