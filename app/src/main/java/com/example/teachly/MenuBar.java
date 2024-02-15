package com.example.teachly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.view.MenuItem;
import android.widget.PopupMenu;


public class MenuBar {

    private Context context;

    public MenuBar(Context context) {
        this.context = context;
        //setupActionBar();
    }

    public void setupActionBar() {

        View customActionBarView = LayoutInflater.from(context).inflate(R.layout.menu_bar_with_logo, null);
        AppCompatActivity activity = (AppCompatActivity) context;

        android.app.ActionBar actionBar = ((AppCompatActivity) context).getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(customActionBarView);
        }

        // Access buttons in the custom action bar
        ImageButton menu = activity.findViewById(R.id.btnMenu);

           // Set click listeners for the buttons
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(context, v);
            }
        });
    }

    public static void showMenu(Context context, View anchor){
        PopupMenu popup = new PopupMenu( context, anchor);
        popup.getMenuInflater().inflate(R.menu.menu_dropdown, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.editProfile){
                    Intent intent = new Intent(context, EditProfile.class);
                    context.startActivity(intent);
                }
                else if (item.getItemId() == R.id.logOff){
                }
                return false;
            }
        });
    }
}
