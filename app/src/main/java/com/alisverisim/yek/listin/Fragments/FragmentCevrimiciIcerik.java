package com.alisverisim.yek.listin.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alisverisim.yek.listin.Activitys.MainActivity;
import com.alisverisim.yek.listin.Adapters.cevrimiciIcerikAdapter;
import com.alisverisim.yek.listin.AlertDialogs.bildirimAlert;
import com.alisverisim.yek.listin.AlertDialogs.cevrimiciKullanicialert;
import com.alisverisim.yek.listin.AlertDialogs.cevrimicilisteyitemizlealert;
import com.alisverisim.yek.listin.Interfaces.IOnBackPressed;
import com.alisverisim.yek.listin.Models.UrunModels;
import com.alisverisim.yek.listin.Models.notModel;
import com.alisverisim.yek.listin.R;
import com.alisverisim.yek.listin.Utils.ChangeFragment;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;


public class FragmentCevrimiciIcerik extends Fragment {

    View view;
    HashMap<String, Boolean> hashMap;
    HashMap<String, String> notHashmap;
    String listeadi;
    TextView icerikbaslik;
    List<UrunModels> urunModelsList;
    ImageView icerikBack;
    cevrimiciIcerikAdapter cevrimiciIcerikAdapter;
    RecyclerView recyclerView;
    ValueEventListener datavalueevent = null;
    FirebaseAuth firebaseAuth;
    TextFieldBoxes urunTextFieldBoxes;
    ExtendedEditText urunEditText;
    FloatingActionButton fabBildirimGonder;
    FloatingActionButton fabKullaniciEkle;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    ImageView listeyitemizle;
    ImageView ortakgoruntule;
    Context context;
    ArrayList<notModel> notlistesi;
    TextView cevrimicivisibletext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_cevrimicilisteicerikfragment, container, false);


        tanimla();
        firebasetanimla();
        listele();
        action();
        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void tanimla() {

        urunEditText = view.findViewById(R.id.cevrimiciurunedittext);
        urunTextFieldBoxes = view.findViewById(R.id.cevrimiciicerikurunekletextfieldboxes);
        recyclerView = view.findViewById(R.id.cevrimicirecylericerik);
        RecyclerView.LayoutManager mng = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mng);
        fabKullaniciEkle = view.findViewById(R.id.yeniarkadasfab);
        fabBildirimGonder = view.findViewById(R.id.bildirimgonderfab);
        listeadi = getArguments().getString("metin").toString();
        icerikbaslik = view.findViewById(R.id.cevrimicilistebasligi);
        icerikBack = view.findViewById(R.id.cevrimiciicerikback);
        icerikbaslik.setText(listeadi);
        listeyitemizle = view.findViewById(R.id.cevrimicilisteyitemizle);
        ortakgoruntule = view.findViewById(R.id.cevrimiciortakgoruntule);
        cevrimicivisibletext = view.findViewById(R.id.cevrimicivisibletext);
    }

    public void firebasetanimla() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


    }

    public void action() {


        urunTextFieldBoxes.getEndIconImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String urunadi = urunEditText.getText().toString();


                if (!urunadi.equals("")) {


                    if (urunadi.length() > 1) {


                        if (listeurunkontrol(urunadi)) {


                            databaseReference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid()).child("lists").child(listeadi).child("Urunler").child(urunEditText.getText().toString());
                            HashMap<String, Object> map2 = returnMap(urunEditText.getText().toString());
                            urunEditText.setText("");
                            databaseReference.updateChildren(map2);


                            // en son urun eklenmesini düzenledim. çekmeleride düzenlemen gerekiyor

                        } else {

                            Toast.makeText(getActivity(), "Böyle bir isimde ürün zaten mevcut.", Toast.LENGTH_SHORT).show();

                        }


                    } else if (urunadi.length() <= 1) {

                        Toast.makeText(getActivity(), "Ürün adı 1 karakterden uzun olmalı.", Toast.LENGTH_SHORT).show();

                    }


                } else if (urunadi.equals("")) {

                    Toast.makeText(getActivity(), "Ürün adı boş girilemez", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // back Action
        icerikBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.state = R.id.tab_cevrimici;

                getActivity().onBackPressed();

            }
        });


        fabKullaniciEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cevrimiciKullanicialert cevrimiciKullanicialert = new cevrimiciKullanicialert(getActivity(), listeadi);


                cevrimiciKullanicialert.ac();
            }
        });


        listeyitemizle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cevrimicilisteyitemizlealert cevrimicilisteyitemizlealert = new cevrimicilisteyitemizlealert(getActivity(), listeadi, firebaseUser.getUid());
                cevrimicilisteyitemizlealert.ac();
            }
        });

        ortakgoruntule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChangeFragment changeFragment = new ChangeFragment(context);
                changeFragment.veriGonder(new FragmentOrtakGoruntule(), listeadi, "ortakGoruntuleFrag");
            }
        });


        fabBildirimGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ChangeFragment changeFragment = new ChangeFragment(getActivity());
                changeFragment.arraysend(new FragmentBildirim(), listeadi,firebaseUser.getUid());



            }
        });

    }

    public void listele() {


        // şuraya dikkat et burada tüm bilgimi konuşturdum
        if (listeadi != null) {
            databaseReference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid()).child("lists").child(listeadi).child("Urunler");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {


                        if (recyclerView.getVisibility() != View.VISIBLE) {

                            recyclerView.setVisibility(View.VISIBLE);
                            cevrimicivisibletext.setVisibility(View.INVISIBLE);

                        }
                        urunModelsList = new ArrayList<>();


                        for (DataSnapshot sh : dataSnapshot.getChildren()) {

                            UrunModels urunModels = new UrunModels(sh.getKey(), sh.child("Durum").getValue(Boolean.class), sh.child("notlar").getValue(String.class));

                            urunModelsList.add(urunModels);
                        }
                        cevrimiciIcerikAdapter = new cevrimiciIcerikAdapter(getActivity(), getContext(), urunModelsList, listeadi);
                        recyclerView.setAdapter(cevrimiciIcerikAdapter);
                        cevrimiciIcerikAdapter.notifyDataSetChanged();

                    } else {

                        recyclerView.setVisibility(View.INVISIBLE);
                        cevrimicivisibletext.setVisibility(View.VISIBLE);

                    }

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }


    public boolean listeurunkontrol(String urunadi) {

        if (urunModelsList != null) {


            for (UrunModels u : urunModelsList) {

                if (u.getUrunAdi().equals(urunadi)) {

                    return false;
                }
            }

            return true;
        }

        return true;
    }


    public HashMap<String, Object> returnMap(String urunAdi) {

        HashMap<String, Object> map = new HashMap<>();

        map.put("Durum", false);

        return map;

    }

}


