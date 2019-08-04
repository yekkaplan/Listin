package com.alisverisim.yek.listin.AlertDialogs;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alisverisim.yek.listin.Activitys.MainActivity;
import com.alisverisim.yek.listin.Models.kullanicieklemodel;
import com.alisverisim.yek.listin.R;
import com.alisverisim.yek.listin.Utils.progressClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class notEkleAlertDialog {

    Activity activity;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String listeadi, urunadi, userid;


    public notEkleAlertDialog(Activity activity, String listeadi, String userid, String urunadi) {
        this.activity = activity;
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference();
        this.listeadi = listeadi;
        this.urunadi = urunadi;
        this.userid = userid;
    }

    public void ac() {
        LayoutInflater ınflater = activity.getLayoutInflater();

        View view = ınflater.inflate(R.layout.noteklealert, null);
        TextView notekle, iptal;
        final EditText notedit;
        final TextView basliktext;
        notekle = view.findViewById(R.id.noteklebutton);
        iptal = view.findViewById(R.id.notekleiptalbutton);
        notedit = view.findViewById(R.id.notedittext);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(activity);
        alert.setView(view);
        alert.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog = alert.create();

        dialog.show();


        notekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                String not = notedit.getText().toString();
                databaseReference = firebaseDatabase.getReference("Users").child(userid).child("lists").child(listeadi).child("Urunler").child(urunadi).child("notlar");
                databaseReference.setValue(not).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            dialog.dismiss();
                        }


                    }
                });


            }
        });


        iptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
            }
        });


    }


}
