package com.alisverisim.yek.listin.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alisverisim.yek.listin.Adapters.ortakListeİcerikAdapter;
import com.alisverisim.yek.listin.AlertDialogs.ortakListelisteyitemizlealert;
import com.alisverisim.yek.listin.Models.UrunModels;
import com.alisverisim.yek.listin.R;
import com.alisverisim.yek.listin.Utils.ChangeFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pd.chocobar.ChocoBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FragmentOrtakListeİcerik extends Fragment {
    String listeadi;
    String kuid;
    RecyclerView recyclerView;
    View view;
    ImageView ortaklisteIcerikUrunEkleImageview;


    LinearLayoutManager mng;
    ortakListeİcerikAdapter ortakListeİcerikAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, urunlisteleRef;
    List<UrunModels> urunModelsList;
    ChildEventListener mSendValuelistener;
    Context context;
    AutoCompleteTextView ortakAutoCompleteTextview;
    Toolbar toolbar;
    Typeface typeface;
    TextView ortaklistevisible, ortakBildirimGonder, ortakTavsiyeGoruntule;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment_ortak_liste_icerik, container, false);


        tanimla();
        listele();
        action();
        tabAction();
        return view;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mSendValuelistener != null) {

            urunlisteleRef.removeEventListener(mSendValuelistener);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @SuppressLint("WrongViewCast")
    public void tanimla() {

        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/yekfont.ttf");
        ortakBildirimGonder = view.findViewById(R.id.ortakBildirimGonder);
        ortakTavsiyeGoruntule = view.findViewById(R.id.ortakTavsiyeGoruntule);
        ortakAutoCompleteTextview = view.findViewById(R.id.ortakAutoCompleteTextview);
        ortaklisteIcerikUrunEkleImageview = view.findViewById(R.id.ortaklisteIcerikUrunEkleImageview);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        listeadi = getArguments().getString("listeadi").toString();
        kuid = getArguments().getString("kuid").toString();

        recyclerView = view.findViewById(R.id.ortaklisteIcerikRecylerview);

        mng = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(mng);

        recyclerView.setLayoutManager(mng);

        ortaklistevisible = view.findViewById(R.id.ortaklisteVisibleText);

    }


    public void listele() {
        urunModelsList = new ArrayList<>();
        if (listeadi != null) {
            urunlisteleRef = firebaseDatabase.getReference("Users").child(kuid).child("lists").child(listeadi).child("Urunler");


            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Log.i("triggerredd", "trigger onChildAdded");


                    if (dataSnapshot.exists()) {

                        if (ortaklistevisible.getVisibility() == view.VISIBLE) {

                            ortaklistevisible.setVisibility(View.INVISIBLE);

                        }

                        UrunModels urunModels = new UrunModels(dataSnapshot.getKey(), dataSnapshot.child("Durum").getValue(Boolean.class), dataSnapshot.child("notlar").getValue(String.class));

                        urunModelsList.add(0, urunModels);
                        ortakListeİcerikAdapter = new ortakListeİcerikAdapter(getActivity(), getContext(), urunModelsList, listeadi, kuid);
                        recyclerView.setAdapter(ortakListeİcerikAdapter);
                        ortakListeİcerikAdapter.notifyDataSetChanged();


                    }


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    Log.i("triggerredd", "trigger onChildChanged");

                    String urunadi = dataSnapshot.getKey();

                    for (UrunModels urunModels : urunModelsList) {

                        if (urunModels.getUrunAdi().equals(urunadi)) {

                            urunModels.setNot(dataSnapshot.child("notlar").getValue(String.class));
                            urunModels.setAlindimi(dataSnapshot.child("Durum").getValue(Boolean.class));
                            recyclerView.setAdapter(ortakListeİcerikAdapter);
                            ortakListeİcerikAdapter.notifyDataSetChanged();

                        }
                    }


                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    Log.i("triggerredd", "trigger onChildRemoved");


                    for (int i = 0; i <= urunModelsList.size() - 1; i++) {

                        if (urunModelsList.get(i).getUrunAdi().equals(dataSnapshot.getKey())) {

                            urunModelsList.remove(i);
                            ortakListeİcerikAdapter.notifyItemRemoved(i);
                            ortakListeİcerikAdapter.notifyItemRangeChanged(i, urunModelsList.size());

                        }
                    }

                    if (urunModelsList.size() == 0) {

                        ortaklistevisible.setVisibility(View.VISIBLE);

                    }


                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    Log.i("triggerredd", "trigger onChildMoved");


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {


                }
            };


            urunlisteleRef.addChildEventListener(childEventListener);
            mSendValuelistener = childEventListener;


        }


        if (urunModelsList.size() == 0) {

            ortaklistevisible.setVisibility(View.VISIBLE);
        } else if (urunModelsList.size() > 0) {

            ortaklistevisible.setVisibility(View.INVISIBLE);

        }


    }

    public void action() {


        // urun adi alıp adaptere' setleyen


        ortakTavsiyeGoruntule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSendValuelistener != null) {

                    urunlisteleRef.removeEventListener(mSendValuelistener);
                }

                ChangeFragment changeFragment = new ChangeFragment(getContext());
                changeFragment.ikiVeriGonder(new FragmentTavsiyeUrunler(), listeadi, kuid, "fragTavsiye");
            }
        });
        ortaklisteIcerikUrunEkleImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String urunadi = ortakAutoCompleteTextview.getText().toString();


                if (!urunadi.equals("")) {


                    if (urunadi.length() > 1) {

                        if (listeurunkontrol(urunadi)) {

                            databaseReference = firebaseDatabase.getReference("Users").child(kuid).child("lists").child(listeadi).child("Urunler").child(ortakAutoCompleteTextview.getText().toString());
                            HashMap<String, Object> map2 = returnMap(ortakAutoCompleteTextview.getText().toString());
                            ortakAutoCompleteTextview.setText("");
                            databaseReference.updateChildren(map2);


                        } else {
                            ChocoBar.builder().setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                                    .setTextSize(16)
                                    .setTextColor(Color.parseColor("#FFFFFF"))
                                    .setTextTypefaceStyle(Typeface.NORMAL)
                                    .setText("Böyle bir isimde ürün zaten mevcut.")
                                    .setMaxLines(2)
                                    .centerText()
                                    .setIcon(R.drawable.ic_snackbar)
                                    .setActivity(getActivity())
                                    .setDuration(ChocoBar.LENGTH_SHORT)
                                    .build()
                                    .show();


                        }


                    } else if (urunadi.length() <= 2) {


                        ChocoBar.builder().setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                                .setTextSize(16)
                                .setTextColor(Color.parseColor("#FFFFFF"))
                                .setTextTypefaceStyle(Typeface.NORMAL)
                                .setText("Ürün adı 1 karakterden uzun olmalı.")
                                .setMaxLines(2)
                                .centerText()
                                .setIcon(R.drawable.ic_snackbar)
                                .setActivity(getActivity())
                                .setDuration(ChocoBar.LENGTH_SHORT)
                                .build()
                                .show();


                    }


                } else if (urunadi.equals("")) {
                    ChocoBar.builder().setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                            .setTextSize(16)
                            .setTextColor(Color.parseColor("#FFFFFF"))
                            .setTextTypefaceStyle(Typeface.NORMAL)
                            .setText("Ürün adı boş girilemez")
                            .setMaxLines(2)
                            .centerText()
                            .setIcon(R.drawable.ic_snackbar)
                            .setActivity(getActivity())
                            .setDuration(ChocoBar.LENGTH_SHORT)
                            .build()
                            .show();


                }


            }
        });


        ortakBildirimGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSendValuelistener != null) {

                    urunlisteleRef.removeEventListener(mSendValuelistener);
                }

                ChangeFragment changeFragment = new ChangeFragment(getContext());
                changeFragment.ikiVeriGonder(new FragmentBildirim(), listeadi, kuid,"fragBildirim");

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


        toolbar = view.findViewById(R.id.ortakiceriktoolbar);


        toolbar.setTitleTextAppearance(getContext(), R.style.ToolbarTitleTextApperance);
        toolbar.setSubtitleTextAppearance(getContext(), R.style.ToolbarSubTitleTextApperance);
        toolbar.setTitle("Ortak Listeler");
        toolbar.setSubtitle(listeadi);
        toolbar.inflateMenu(R.menu.cevrimici_icerik_menu);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mSendValuelistener != null) {

                    urunlisteleRef.removeEventListener(mSendValuelistener);
                }
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
                    ortakAutoCompleteTextview.setText("");

                } else if (menuItem.getItemId() == R.id.menu_ortakarkadaslar) {

                    if (mSendValuelistener != null) {

                        urunlisteleRef.removeEventListener(mSendValuelistener);
                    }
                    ChangeFragment changeFragment = new ChangeFragment(getContext());
                    changeFragment.ikiVeriGonder(new FragmentOrtaklisteOrtakgoruntule(), listeadi, kuid, "ortaklisteortakgoruntuleFrag");

                }


                return true;
            }
        });

    }


}


