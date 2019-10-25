package com.alisverisim.yek.listin.AlertDialogs;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
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
import com.pd.chocobar.ChocoBar;


import java.util.HashMap;


public class cevrimiciKullanicialert {

    Activity activity;
    progressClass progressClass;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    kullanicieklemodel kullanicieklemodel;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference, databaseReference2;
    String listeadi;
    String email;
    HashMap<String, String> emaillistesi;

    public cevrimiciKullanicialert(Activity activity, String listeadi) {
        this.activity = activity;
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        emaillistesi = new HashMap<>();

        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference();
        vericek();
        this.listeadi = listeadi;

    }

    public void ac() {
        LayoutInflater ınflater = activity.getLayoutInflater();

        View view = ınflater.inflate(R.layout.cevrimicikullaniciadd, null);


        final EditText editText = view.findViewById(R.id.cevrimiciurunmenualertedittext);
        TextView button = view.findViewById(R.id.cevrimiciurunmenualertbutton);
        TextView iptalbutton = view.findViewById(R.id.cevrimciciuruniptalbuttonyenidenadlandır);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(activity);

        alert.setView(view);
        alert.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog = alert.create();


        dialog.show();


        // email alma ve kontrol işlemleri
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = editText.getText().toString();

                String key = emaillistesi.get(email);

                if (key == null) {


                    Toast.makeText(activity,"Böyle bir email kayıtlı değil.",Toast.LENGTH_SHORT).show();


                } else if (email.equals(MainActivity.mail)) {


                    Toast.makeText(activity,"Kendi mailiniz ile işlem yapamazsınız.",Toast.LENGTH_SHORT).show();


                } else if (key != null) {


                    dialog.dismiss();
                    cevrimicikullanicieklenildialert cevrimicikullanicieklenildialert = new cevrimicikullanicieklenildialert(activity, email, key, listeadi);
                    cevrimicikullanicieklenildialert.ac();
                }


            }
        });

        iptalbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

    }


    public void vericek() {

        databaseReference2 = firebaseDatabase.getReference();
        databaseReference2.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {


                    kullanicieklemodel = new kullanicieklemodel();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        if (ds.getValue() != null) {


                            Log.i("deneme", ds.child("email").getValue(String.class).toString() + "  keyy: " + ds.getKey() + " ");
                            emaillistesi.put(ds.child("email").getValue(String.class).toString(), ds.getKey());

                            databaseReference2.removeEventListener(this);

                        }
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
