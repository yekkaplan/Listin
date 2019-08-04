package com.alisverisim.yek.listin.AlertDialogs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alisverisim.yek.listin.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class cevrimicilisteyitemizlealert {

    Activity activity;
    String listeadi;
    String kuid;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    public cevrimicilisteyitemizlealert(Activity activity, String listeadi, String kuid) {
        this.activity = activity;
        this.listeadi = listeadi;
        this.kuid = kuid;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void ac() {
        LayoutInflater ınflater = activity.getLayoutInflater();

        final View view = ınflater.inflate(R.layout.cevrimicilisteyitemizlealert, null);

        TextView listeyitemizle, iptal;

        listeyitemizle = view.findViewById(R.id.ctemizle);
        iptal = view.findViewById(R.id.ciptaltemizle);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(activity);
        alert.setView(view);
        alert.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog = alert.create();


        listeyitemizle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                databaseReference = firebaseDatabase.getReference("Users").child(kuid).child("lists").child(listeadi).child("Urunler");
                databaseReference.removeValue();
                dialog.cancel();
            }
        });


        iptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

            }
        });

        dialog.show();

    }
}
