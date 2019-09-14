package com.alisverisim.yek.listin.AlertDialogs;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alisverisim.yek.listin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class listesilmealert {

    Activity activity;
    android.support.v7.app.AlertDialog dialog;

    HashMap<String, Object> kullanicihashmap;
    String listeAdi;
    String yerelkuid;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    public listesilmealert(Activity activity, HashMap<String, Object> kullanicihasmap, String listeAdi, String yerelkuid) {
        this.activity = activity;
        this.kullanicihashmap = kullanicihasmap;
        this.listeAdi = listeAdi;
        this.yerelkuid = yerelkuid;
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public void ac() {
        LayoutInflater ınflater = activity.getLayoutInflater();

        View view = ınflater.inflate(R.layout.listesilmealert, null);


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


                if (kullanicihashmap != null) {

                    for (final String s : kullanicihashmap.keySet()) {
                        databaseReference = firebaseDatabase.getReference("Users").child(s).child("ortakListeler");
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                if (dataSnapshot.exists()) {

                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                        String gelenlisteadi, kullaniciuid;
                                        gelenlisteadi = ds.child("listeadi").getValue(String.class);
                                        kullaniciuid = ds.child("kullaniciuid").getValue(String.class);
                                        Log.i("denemee", gelenlisteadi + "  " + kullaniciuid);
                                        if (gelenlisteadi != null) {
                                            if (gelenlisteadi.equals(listeAdi) && kullaniciuid.equals(yerelkuid)) {
                                                String key = ds.getKey();
                                                DatabaseReference databaseReference1 = firebaseDatabase.getReference("Users").child(s).child("ortakListeler").child(key);
                                                databaseReference1.removeValue();
                                            }
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

                DatabaseReference databaseReference2 = firebaseDatabase.getReference("Users").child(yerelkuid).child("lists").child(listeAdi);
                databaseReference2.removeValue();
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
