package com.example.teachly;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.teachly.R;

public class MenuUtils {

    public static void showMenu(Context context, View anchor){
        PopupMenu popup = new PopupMenu(context, anchor);
        popup.getMenuInflater().inflate(R.menu.menu_dropdown, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.editProfile){
                    Toast.makeText(context, "Edit Profile", Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId() == R.id.logOff){
                    Toast.makeText(context, "Log Off", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }
}
