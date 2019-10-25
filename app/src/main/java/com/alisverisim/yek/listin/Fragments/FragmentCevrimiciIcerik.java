package com.alisverisim.yek.listin.Fragments;

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
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alisverisim.yek.listin.Adapters.cevrimiciIcerikAdapter;
import com.alisverisim.yek.listin.AlertDialogs.cevrimiciKullanicialert;
import com.alisverisim.yek.listin.AlertDialogs.cevrimicilisteyitemizlealert;
import com.alisverisim.yek.listin.Models.UrunModels;
import com.alisverisim.yek.listin.R;
import com.alisverisim.yek.listin.Utils.ChangeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pd.chocobar.ChocoBar;

import java.util.ArrayList;
import java.util.HashMap;


public class FragmentCevrimiciIcerik extends Fragment {

    View view, Contextview;
    Toolbar toolbar;
    String listeadi;
    ArrayList<UrunModels> urunModelsList;
    cevrimiciIcerikAdapter cevrimiciIcerikAdapter;
    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    TextView tavsiyeGoruntule;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, urunlisteleRef;
    FirebaseUser firebaseUser;
    LinearLayoutManager mng;
    Context context;
    ArrayList<String> tavsiyeUrunlist;
    TextView cevrimicivisibletext;
    AutoCompleteTextView multiAutoCompleteTextView;
    ImageView urunEkleImageview;
    ChildEventListener mSendValuelistener;
    TextView urunAldim, arkadasEkle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_cevrimicilisteicerikfragment, container, false);


        tanimla();
        firebasetanimla();
        listele();
        tabAction();
        action();

        return view;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();


        Log.i("trigerred", "onDestroy triggerland.");

        if (mSendValuelistener != null) {

            urunlisteleRef.removeEventListener(mSendValuelistener);


        }
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void tanimla() {


        Contextview = view.findViewById(R.id.contextView);


        recyclerView = view.findViewById(R.id.cevrimicirecylericerik);
        mng = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(mng);

        cevrimicivisibletext = view.findViewById(R.id.cevrimicivisibletext);
        multiAutoCompleteTextView = view.findViewById(R.id.urunAutoCompleteTextview);
        tavsiyeGoruntule = view.findViewById(R.id.tavsiyeGoruntule);
        urunEkleImageview = view.findViewById(R.id.urunEkleImageview);
        listeadi = getArguments().getString("metin").toString();
        urunAldim = view.findViewById(R.id.urunAldim);
        arkadasEkle = view.findViewById(R.id.arkadasEkle);

        // auto textview başlangıç


        String[] countries = getResources().getStringArray(R.array.countries_array);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, countries);
        multiAutoCompleteTextView.setAdapter(adapter);
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(36);
        multiAutoCompleteTextView.setFilters(filterArray);

        // auto textview bitiş

        tavsiyeUrunlist = new ArrayList<>();
        tavsiyeUrunlist = getArguments().getStringArrayList("tavsiyeurunlistesi");


    }

    public void firebasetanimla() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


    }

    public void listele() {
        urunModelsList = new ArrayList<>();
        if (listeadi != null) {
            urunlisteleRef = firebaseDatabase.getReference("Users").child(firebaseUser.getUid()).child("lists").child(listeadi).child("Urunler");


            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Log.i("triggerredd", "trigger onChildAdded");


                    if (dataSnapshot.exists()) {



                        UrunModels urunModels = new UrunModels(dataSnapshot.getKey(), dataSnapshot.child("Durum").getValue(Boolean.class), dataSnapshot.child("notlar").getValue(String.class));

                        urunModelsList.add(0, urunModels);
                        cevrimiciIcerikAdapter = new cevrimiciIcerikAdapter(getActivity(), getContext(), urunModelsList, listeadi);
                        recyclerView.setAdapter(cevrimiciIcerikAdapter);
                        cevrimiciIcerikAdapter.notifyDataSetChanged();




                    }


                    if(urunModelsList.size() == 0){


                        cevrimicivisibletext.setVisibility(View.VISIBLE);

                    } else if(urunModelsList.size() > 0){


                        cevrimicivisibletext.setVisibility(View.INVISIBLE);

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
                            recyclerView.setAdapter(cevrimiciIcerikAdapter);
                            cevrimiciIcerikAdapter.notifyDataSetChanged();

                        }
                    }


                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    Log.i("triggerredd", "trigger onChildRemoved");


                    for (int i = 0; i <= urunModelsList.size() - 1; i++) {

                        if (urunModelsList.get(i).getUrunAdi().equals(dataSnapshot.getKey())) {

                            urunModelsList.remove(i);
                            cevrimiciIcerikAdapter.notifyItemRemoved(i);
                            cevrimiciIcerikAdapter.notifyItemRangeChanged(i, urunModelsList.size());

                        }
                    }

                    if (urunModelsList.size() == 0) {

                        cevrimicivisibletext.setVisibility(View.VISIBLE);

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

            cevrimicivisibletext.setVisibility(View.VISIBLE);
        } else if (urunModelsList.size() > 0) {

            cevrimicivisibletext.setVisibility(View.INVISIBLE);

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

    // tab buttonlarının aksiyonlarının yer aldığı method.
    private void tabAction() {

        // toolbar config
        toolbar = view.findViewById(R.id.cevrimiciiceriktoolbar);
        toolbar.setTitleTextAppearance(getContext(), R.style.ToolbarTitleTextApperance);
        toolbar.setSubtitleTextAppearance(getContext(), R.style.ToolbarSubTitleTextApperance);
        toolbar.setTitle("Çevrimiçi Listeler");
        toolbar.setSubtitle(listeadi);
        toolbar.inflateMenu(R.menu.cevrimici_icerik_menu);
        // toolbar config

        tavsiyeGoruntule.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if (mSendValuelistener != null) {

                    urunlisteleRef.removeEventListener(mSendValuelistener);
                }

                ChangeFragment changeFragment = new ChangeFragment(getContext());
                changeFragment.ikiVeriGonder(new FragmentTavsiyeUrunler(), listeadi, firebaseAuth.getUid(), "fragTavsiye");
            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // back button pressed
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.menu_listeyitemizle) {

                    cevrimicilisteyitemizlealert cevrimicilisteyitemizlealert = new cevrimicilisteyitemizlealert(getActivity(), listeadi, firebaseUser.getUid());
                    cevrimicilisteyitemizlealert.ac();

                } else if (menuItem.getItemId() == R.id.menu_ortakarkadaslar) {

                    ChangeFragment changeFragment = new ChangeFragment(context);


                    if (mSendValuelistener != null) {

                        urunlisteleRef.removeEventListener(mSendValuelistener);
                    }


                    changeFragment.veriGonder(new FragmentOrtakGoruntule(), listeadi, "ortakGoruntuleFrag");


                }


                return true;
            }
        });

    }


    // -- button fonksiyonlarının yer aldığı method
    public void action() {


        urunAldim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listeadi != null && mSendValuelistener != null) {

                    ChangeFragment changeFragment = new ChangeFragment(getActivity());
                    changeFragment.ikiVeriGonder(new FragmentBildirim(), listeadi, firebaseAuth.getUid(), "FragmentBildirim");

                    urunlisteleRef.removeEventListener(mSendValuelistener);

                } else if (listeadi != null && mSendValuelistener == null) {

                    urunlisteleRef.removeEventListener(mSendValuelistener);


                }


            }
        });

        arkadasEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                cevrimiciKullanicialert cevrimiciKullanicialert = new cevrimiciKullanicialert(getActivity(), listeadi);

                cevrimiciKullanicialert.ac();


            }
        });
        urunEkleImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String urunadi = multiAutoCompleteTextView.getText().toString();


                if (!urunadi.equals("")) {


                    if (urunadi.length() > 1) {


                        if (listeurunkontrol(urunadi)) {


                            databaseReference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid()).child("lists").child(listeadi).child("Urunler").child(multiAutoCompleteTextView.getText().toString());
                            HashMap<String, Object> map2 = returnMap(multiAutoCompleteTextView.getText().toString());
                            multiAutoCompleteTextView.setText("");
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


                    } else if (urunadi.length() <= 1) {




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
                            .setText("Ürün adı boş girilemez.")
                            .setMaxLines(2)
                            .centerText()
                            .setIcon(R.drawable.ic_snackbar)
                            .setActivity(getActivity())
                            .setDuration(ChocoBar.LENGTH_SHORT)
                            .build()
                            .show();                }
            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getActivity().onBackPressed();
            }
        });
    }


}


