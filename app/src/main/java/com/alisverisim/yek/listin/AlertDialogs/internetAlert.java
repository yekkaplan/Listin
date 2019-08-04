package com.alisverisim.yek.listin.AlertDialogs;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alisverisim.yek.listin.Activitys.MainActivity;
import com.alisverisim.yek.listin.Models.kullanicieklemodel;
import com.alisverisim.yek.listin.R;
import com.alisverisim.yek.listin.Utils.progressClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class internetAlert {

    Activity activity;
    android.support.v7.app.AlertDialog dialog;

    public internetAlert(Activity activity) {
        this.activity = activity;
    }

    public void ac() {
        LayoutInflater ınflater = activity.getLayoutInflater();

        View view = ınflater.inflate(R.layout.internetyokalert, null);


        TextView button = view.findViewById(R.id.internetkoptutamambtn);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(activity);
        alert.setView(view);
        alert.setCancelable(true);
        dialog = alert.create();


        dialog.show();


        // email alma ve kontrol işlemleri
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
            }
        });


    }

    public void kapat() {


        dialog.dismiss();

    }

}
