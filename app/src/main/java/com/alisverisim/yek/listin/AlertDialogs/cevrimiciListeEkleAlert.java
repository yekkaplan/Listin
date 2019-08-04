package com.alisverisim.yek.listin.AlertDialogs;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alisverisim.yek.listin.Activitys.MainActivity;
import com.alisverisim.yek.listin.Fragments.FragmentCevrimiciIcerik;
import com.alisverisim.yek.listin.Models.cevrimcilistmodel;
import com.alisverisim.yek.listin.R;
import com.alisverisim.yek.listin.Utils.ChangeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class cevrimiciListeEkleAlert {

    Activity activity;
    String listeAdi;
    List<cevrimcilistmodel> list;
    DatabaseReference reference, reference2;
    FirebaseDatabase database;
    FirebaseAuth auth;
    String kendiemail;
    HashMap<String, Boolean> hashMap;


    public cevrimiciListeEkleAlert(Activity activity, List<cevrimcilistmodel> list) {

        this.activity = activity;
        this.list = list;
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public void ac() {


        LayoutInflater ınflater = activity.getLayoutInflater();

        View view = ınflater.inflate(R.layout.cevrimicilistealert, null);


        final EditText editText = view.findViewById(R.id.menualertedittext);
        TextView button = view.findViewById(R.id.menualertbutton);
        TextView iptalbutton = view.findViewById(R.id.iptalbuttonyenidenadlandır);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(activity);
        alert.setView(view);
        alert.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog = alert.create();


        dialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listeAdi = editText.getText().toString();
                if (!listeAdi.equals("")) {


                    if (listisimkontrol(listeAdi)) {

                        // liste oluşturma işlemi başlangıcı
                        Map mapliste = new HashMap();
                        reference = database.getReference().child("Users").child(auth.getUid()).child("lists").child(listeAdi);
                        Map map = new HashMap();

                        Map mailMap = new HashMap();
                        mailMap.put(auth.getUid(), MainActivity.mail);
                        map.put("listeortaklari",mailMap);
                        map.put("Urunler", mapliste);
                        reference.updateChildren(map);
                        // liste oluşturma işlemi sonu

                        dialog.dismiss();
                        ChangeFragment changeFragment = new ChangeFragment(activity);
                        changeFragment.veriGonder(new FragmentCevrimiciIcerik(), listeAdi, "cevrimiciIcerikFrag");


                    } else {


                        Toast.makeText(activity.getApplicationContext(), "Böyle bir liste zaten var.", Toast.LENGTH_SHORT).show();

                    }

                } else {

                    Toast.makeText(activity.getApplicationContext(), "Liste adı boş bırakılamaz.", Toast.LENGTH_SHORT).show();
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


    public boolean listisimkontrol(String listeAdi) {

        if (list != null) {
            for (cevrimcilistmodel c : list) {

                if (c.getListeadi().equals(listeAdi)) {

                    return false;

                }
            }
            return true;
        }
        return true;

    }


}
