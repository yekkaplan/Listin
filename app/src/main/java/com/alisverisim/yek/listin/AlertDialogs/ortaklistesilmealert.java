package com.alisverisim.yek.listin.AlertDialogs;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alisverisim.yek.listin.R;
import com.alisverisim.yek.listin.Utils.FirebaseServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ortaklistesilmealert {

    Activity activity;
    android.support.v7.app.AlertDialog dialog;

    String listeAdi;
    String yerelkuid;
    String listekey;
    FirebaseDatabase firebaseDatabase;

    public ortaklistesilmealert(Activity activity, String listeAdi, String yerelkuid, String listekey) {
        this.activity = activity;
        this.listeAdi = listeAdi;
        this.yerelkuid = yerelkuid;
        this.listekey = listekey;
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public void ac() {
        LayoutInflater ınflater = activity.getLayoutInflater();

        View view = ınflater.inflate(R.layout.ortaklistesilmealert, null);


        TextView tamam = view.findViewById(R.id.listesiltamam);
        TextView iptal = view.findViewById(R.id.listesiliptal);
        TextView baslik = view.findViewById(R.id.listesilbaslik);


        baslik.setText(listeAdi + " isimli listeyi silmek istediğinizden eminmisiniz?");

        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(activity);
        alert.setView(view);
        alert.setCancelable(false);
        dialog = alert.create();


        dialog.show();


        tamam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseServices.ortaklistekaldır(listekey);
                FirebaseServices.ortaklistekaldır(listekey, listeAdi, yerelkuid);
                dialog.dismiss();

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
