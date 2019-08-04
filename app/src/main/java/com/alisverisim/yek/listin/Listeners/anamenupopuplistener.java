package com.alisverisim.yek.listin.Listeners;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.alisverisim.yek.listin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class anamenupopuplistener implements PopupMenu.OnMenuItemClickListener {
    Context context;

    public anamenupopuplistener(Context context)  {
        this.context = context;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.cikisyap:

                android.os.Process.killProcess(android.os.Process.myPid());
                return true;
            default:
        }
        return false;
    }



}