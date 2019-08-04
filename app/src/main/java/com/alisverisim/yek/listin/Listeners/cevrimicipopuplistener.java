package com.alisverisim.yek.listin.Listeners;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;

import com.alisverisim.yek.listin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class cevrimicipopuplistener implements PopupMenu.OnMenuItemClickListener {
    Context context;
    String listeadi;
    HashMap<String, Object> kullanicihashmap;
    String yerelkuid;
    FirebaseDatabase firebaseDatabase = null;
    DatabaseReference databaseReference;
    int index;

    public cevrimicipopuplistener(Context context, String listeadi, HashMap<String, Object> kullanicihashmap, String yerelkuid, int index) {
        this.context = context;
        this.listeadi = listeadi;
        this.kullanicihashmap = kullanicihashmap;
        this.yerelkuid = yerelkuid;
        firebaseDatabase = FirebaseDatabase.getInstance();
        this.index = index;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.listeyisil:

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
                                        Log.i("denemee", gelenlisteadi  + "  " + kullaniciuid);
                                        if (gelenlisteadi != null) {
                                            if (gelenlisteadi.equals(listeadi) && kullaniciuid.equals(yerelkuid)) {
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

                DatabaseReference databaseReference2 = firebaseDatabase.getReference("Users").child(yerelkuid).child("lists").child(listeadi);
                databaseReference2.removeValue();
                return true;
            default:
        }
        return false;
    }


    public void listesil() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();


    }


}