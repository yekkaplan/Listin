package com.alisverisim.yek.listin.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alisverisim.yek.listin.Activitys.MainActivity;
import com.alisverisim.yek.listin.Adapters.ortakListeİcerikAdapter;
import com.alisverisim.yek.listin.AlertDialogs.cevrimicilisteyitemizlealert;
import com.alisverisim.yek.listin.AlertDialogs.ortakListelisteyitemizlealert;
import com.alisverisim.yek.listin.Models.UrunModels;
import com.alisverisim.yek.listin.R;
import com.alisverisim.yek.listin.Utils.ChangeFragment;
import com.github.clans.fab.FloatingActionButton;
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


public class FragmentOrtakListeİcerik extends Fragment {
    String listeadi;
    String kuid;
    RecyclerView recyclerView;
    View view;
    ExtendedEditText editText;
    ImageView back;
    TextView listeAdiTextview;
    FloatingActionButton bildirimfab;
    ortakListeİcerikAdapter ortakListeİcerikAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<UrunModels> urunModelsList;
    ImageView ortakgoruntule, listeyitemizle;
    HashMap<String, Boolean> hashMap;
    Context context;
    Toolbar toolbar;
    Typeface typeface;
    TextView ortaklistevisible;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment_ortak_liste_icerik, container, false);


        tanimla();
        listeerisim();
        action();
        tabAction();
        return view;


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @SuppressLint("WrongViewCast")
    public void tanimla() {

        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/yekfont.ttf");

        bildirimfab = view.findViewById(R.id.ortakbildirimfab);
        ortakgoruntule = view.findViewById(R.id.ortakgoruntule);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        listeadi = getArguments().getString("listeadi").toString();
        kuid = getArguments().getString("kuid").toString();
        recyclerView = view.findViewById(R.id.ortaklisteurunlerrecyler);
        RecyclerView.LayoutManager mng = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mng);
        editText = view.findViewById(R.id.ortaklisteurunadiedittext);
        ortaklistevisible = view.findViewById(R.id.ortaklistevisible);

    }


    public void listeerisim() {


        databaseReference = firebaseDatabase.getReference("Users").child(kuid).child("lists").child(listeadi).child("Urunler");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {

                    if (recyclerView.getVisibility() != View.VISIBLE) {

                        recyclerView.setVisibility(View.VISIBLE);
                        ortaklistevisible.setVisibility(View.INVISIBLE);
                    }
                    urunModelsList = new ArrayList<>();
                    hashMap = (HashMap<String, Boolean>) dataSnapshot.getValue();


                    for (DataSnapshot sh : dataSnapshot.getChildren()) {

                        Log.i("denemee", sh.child("Durum").getValue(Boolean.class) + "   " + sh.getKey() + "  ");

                        UrunModels urunModels = new UrunModels(sh.getKey(), sh.child("Durum").getValue(Boolean.class), sh.child("notlar").getValue(String.class));

                        urunModelsList.add(urunModels);
                    }
                    ortakListeİcerikAdapter = new ortakListeİcerikAdapter(getActivity(), getContext(), urunModelsList, listeadi, kuid);
                    recyclerView.setAdapter(ortakListeİcerikAdapter);
                    ortakListeİcerikAdapter.notifyDataSetChanged();
                } else {
                    recyclerView.setVisibility(View.INVISIBLE);
                    ortaklistevisible.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void action() {


        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {


                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    String urunadi = editText.getText().toString();


                    if (!urunadi.equals("")) {


                        if (urunadi.length() > 1) {


                            if (listeurunkontrol(urunadi)) {

                                databaseReference = firebaseDatabase.getReference("Users").child(kuid).child("lists").child(listeadi).child("Urunler");

                                HashMap<String, Object> map2 = returnMap(editText.getText().toString());

                                databaseReference.updateChildren(map2);
                                editText.setText("");


                            } else {

                                Toast.makeText(context, "Böyle bir isimde ürün zaten mevcut.", Toast.LENGTH_SHORT).show();

                            }


                        } else if (urunadi.length() <= 1) {

                            Toast.makeText(context, "Ürün adı 1 karakterden uzun olmalı.", Toast.LENGTH_SHORT).show();

                        }


                    } else if (urunadi.equals("")) {

                        Toast.makeText(context, "Ürün adı boş girilemez", Toast.LENGTH_SHORT).show();
                    }


                    return true;
                }

                return false;
            }
        });

        // urun adi alıp adaptere' setleyen
        final TextFieldBoxes textFieldBoxes = view.findViewById(R.id.ortaklisteurunaditextfield);
        textFieldBoxes.getEndIconImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String urunadi = editText.getText().toString();


                if (!urunadi.equals("")) {


                    if (urunadi.length() > 1) {

                        if (listeurunkontrol(urunadi)) {

                            databaseReference = firebaseDatabase.getReference("Users").child(kuid).child("lists").child(listeadi).child("Urunler").child(editText.getText().toString());
                            HashMap<String, Object> map2 = returnMap(editText.getText().toString());
                            editText.setText("");
                            databaseReference.updateChildren(map2);


                        } else {

                            Toast.makeText(context, "Böyle bir isimde ürün zaten mevcut.", Toast.LENGTH_SHORT).show();

                        }


                    } else if (urunadi.length() <= 2) {

                        Toast.makeText(context, "Ürün adı 1 karakterden uzun olmalı.", Toast.LENGTH_SHORT).show();

                    }


                } else if (urunadi.equals("")) {

                    Toast.makeText(context, "Ürün adı boş girilemez", Toast.LENGTH_SHORT).show();
                }


            }
        });


        bildirimfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChangeFragment changeFragment = new ChangeFragment(getContext());
                changeFragment.arraysend(new FragmentBildirim(), listeadi, kuid);

            }
        });

    }


    public HashMap<String, Object> returnMap(String urunAdi) {

        HashMap<String, Object> map = new HashMap<>();

        map.put("Durum", false);

        return map;

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

    private void tabAction() {


        toolbar = view.findViewById(R.id.ortaklisteiceriktoolbar);


        toolbar.setTitle("Ortak Listeler");
        toolbar.setSubtitle(listeadi);
        toolbar.inflateMenu(R.menu.cevrimici_icerik_menu);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getActivity().onBackPressed();
                // back button pressed
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.menu_listeyitemizle) {

                    ortakListelisteyitemizlealert ortaklistelisteyitemizlealert = new ortakListelisteyitemizlealert(getActivity(), listeadi, kuid);
                    ortaklistelisteyitemizlealert.ac();
                    editText.setText("");

                } else if (menuItem.getItemId() == R.id.menu_ortakarkadaslar) {

                    ChangeFragment changeFragment = new ChangeFragment(getContext());
                    changeFragment.ikiVeriGonder(new FragmentOrtaklisteOrtakgoruntule(), listeadi, kuid, "ortaklisteortakgoruntuleFrag");

                }


                return true;
            }
        });

    }


}


