package com.alisverisim.yek.listin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alisverisim.yek.listin.Activitys.MainActivity;
import com.alisverisim.yek.listin.Adapters.cevrimiciOrtakListeAdapter;
import com.alisverisim.yek.listin.Interfaces.IOnBackPressed;
import com.alisverisim.yek.listin.Models.ortaklistmodel;
import com.alisverisim.yek.listin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class FragmentOrtakListe extends Fragment implements IOnBackPressed {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference1;
    FirebaseAuth firebaseAuth;
    cevrimiciOrtakListeAdapter cevrimiciOrtakListeAdapter;
    RecyclerView recyclerView;
    List<ortaklistmodel> listModels;
    ortaklistmodel ortaklistmodel;
    SpotsDialog profilProgress;
    FirebaseUser firebaseUser;
    TextView visibleortaklist;
    Toolbar toolbar;
    ChildEventListener mChildEventlistener;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragment_ortak_liste, container, false);
        tanimlama();
        cevrimicilistekontrol();
        tabAction();
        return view;

    }


    @Override
    public void onStop() {
        super.onStop();


    }

    public void tanimlama() {


        firebaseDatabase = FirebaseDatabase.getInstance();
        reference1 = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        recyclerView = view.findViewById(R.id.ortaklistelerrecylerview);
        RecyclerView.LayoutManager mng = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mng);
        visibleortaklist = view.findViewById(R.id.ortaklistevisibletext);

    }


    public void cevrimicilistekontrol() {

        profilProgress = new SpotsDialog(getActivity(), R.style.Custom);
        profilProgress.show();
        new Thread(new Runnable() {
            public void run() {

                reference1.child("Users").child(MainActivity.kuid).child("ortakListeler").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        listModels = new ArrayList<>();

                        if (dataSnapshot.exists()) {


                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                String listeadi = ds.child("listeadi").getValue(String.class);
                                String kullaniciuid = ds.child("kullaniciuid").getValue(String.class);
                                String listekey = ds.getKey().toString();
                                ortaklistmodel = new ortaklistmodel(listeadi, kullaniciuid, listekey);
                                listModels.add(ortaklistmodel);
                            }

                        }

                        if (listModels.size() > 0) {


                            cevrimiciOrtakListeAdapter = new cevrimiciOrtakListeAdapter(listModels, getActivity(), getContext());
                            recyclerView.setAdapter(cevrimiciOrtakListeAdapter);
                            cevrimiciOrtakListeAdapter.notifyDataSetChanged();
                            profilProgress.dismiss();
                            visibleortaklist.setVisibility(View.INVISIBLE);


                        } else if (listModels.size() == 0) {
                            cevrimiciOrtakListeAdapter = new cevrimiciOrtakListeAdapter(listModels, getActivity(), getContext());
                            recyclerView.setAdapter(cevrimiciOrtakListeAdapter);
                            cevrimiciOrtakListeAdapter.notifyDataSetChanged();

                            visibleortaklist.setVisibility(View.VISIBLE);
                            profilProgress.dismiss();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        }).start();

        Handler progressHandler = new Handler() {

            public void handleMessage(Message msg1) {

                profilProgress.dismiss();
            }
        };


    }


    @Override
    public boolean onBackPressed() {

        System.exit(0);
        return false;
    }

    private void tabAction() {


        toolbar = view.findViewById(R.id.ortaklistetoolbar);

        toolbar.setTitleTextAppearance(getContext(), R.style.ToolbarTitleTextApperance);
        toolbar.setSubtitleTextAppearance(getContext(), R.style.ToolbarSubTitleTextApperance);

        toolbar.setTitle("Listin");
        toolbar.setSubtitle("Ortak Listeler");
        toolbar.inflateMenu(R.menu.cevrimici_toolbar);


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.menu_item_share) {

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBodyText = "Yeni bir alışveriş uygulaması buldum. Bana katıl ve ortak listeler oluşturalım!\n" +
                            "İşte linki: https://play.google.com/store/apps/details?id=com.alisverisim.yek.alversim";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Listin & Çevrimiçi listeler");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                    startActivity(Intent.createChooser(sharingIntent, "Uygulamayı arkadaşların ile paylaş"));


                }


                return false;
            }
        });


    }

}
