package com.alisverisim.yek.listin.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alisverisim.yek.listin.Adapters.cevrimiciTumListeAdapter;
import com.alisverisim.yek.listin.AlertDialogs.cevrimiciListeEkleAlert;
import com.alisverisim.yek.listin.Interfaces.IOnBackPressed;
import com.alisverisim.yek.listin.Models.cevrimcilistmodel;
import com.alisverisim.yek.listin.R;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mlsdev.animatedrv.AnimatedRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class FragmentCevrimici extends Fragment implements IOnBackPressed {
    View view;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    AnimatedRecyclerView cevrimicitumlistelerRecylerview;
    cevrimiciTumListeAdapter cevrimiciTumListeAdapter;
    List<cevrimcilistmodel> listeler;
    String listeadi;
    TextView cevrimicivisibletext;
    SpotsDialog profilProgress;
    TextView tumlistelervisibletext;
    ImageView popup;
    Context context;
    ChildEventListener mSendChildEventListener;
    FloatingActionButton floatingActionButton;


    android.support.v7.widget.Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cevrimici_fragment, container, false);

        viewTanimlanması();
        firebaseTanımlanması();
        tabAction();
        fabAction();
        cevrimicilistelerkontrol();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onStop() {
        super.onStop();

        if (mSendChildEventListener != null) {

            reference.removeEventListener(mSendChildEventListener);
        }
    }

    public void firebaseTanımlanması() {


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }


    public void viewTanimlanması() {


        //  paylasbutton = view.findViewById(R.id.cevrimicipaylasbutton);

        floatingActionButton = view.findViewById(R.id.cevrimicifab);

        // popup = view.findViewById(R.id.cevrimicipopupTextview);

        cevrimicitumlistelerRecylerview = view.findViewById(R.id.recycler_view);
        cevrimicivisibletext = view.findViewById(R.id.cevrimicivisibletext);

        RecyclerView.LayoutManager mng = new GridLayoutManager(getContext(), 2);
        cevrimicitumlistelerRecylerview.setLayoutManager(mng);

        AnimatedRecyclerView cevrimicitumlistelerRecylerview = new AnimatedRecyclerView.Builder(getContext())
                .orientation(LinearLayoutManager.VERTICAL)
                .layoutManagerType(AnimatedRecyclerView.LayoutManagerType.GRID)
                .animation(R.anim.item_animation_from_bottom_scale)
                .animationDuration(600)
                .reverse(false)
                .build();

    }


    private void tabAction() {


        toolbar = view.findViewById(R.id.toolbar);

        toolbar.setTitleTextAppearance(getContext(), R.style.ToolbarTitleTextApperance);
        toolbar.setSubtitleTextAppearance(getContext(), R.style.ToolbarSubTitleTextApperance);
        toolbar.setTitle("Listin");
        toolbar.setSubtitle("Çevrimiçi Listeler");


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

    public void fabAction() {


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cevrimiciListeEkleAlert cevrimiciListeEkleAlert = new cevrimiciListeEkleAlert(getActivity(), listeler);
                cevrimiciListeEkleAlert.ac();


            }
        });
    }


    public void cevrimicilistelerkontrol() {

        listeler = new ArrayList<>();
        reference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid()).child("lists");

        profilProgress = new SpotsDialog(getContext(),R.style.Custom);

        profilProgress.show();
        ChildEventListener childEventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {

                    if (dataSnapshot.child("listeortaklari").getChildrenCount() >= 1) {


                        cevrimicivisibletext.setVisibility(View.INVISIBLE);
                        HashMap<String, Object> hashMap = new HashMap<>();
                        listeadi = dataSnapshot.getKey();
                        Long urunsayisi = dataSnapshot.child("Urunler").getChildrenCount();
                        Long ortaksayisi = dataSnapshot.child("listeortaklari").getChildrenCount();

                        for (DataSnapshot veri : dataSnapshot.child("listeortaklari").getChildren()) {

                            hashMap.put(veri.getKey(), veri.getValue());


                        }


                        cevrimcilistmodel cevrimcilistmodel = new cevrimcilistmodel(listeadi, ortaksayisi, urunsayisi, hashMap);

                        listeler.add(cevrimcilistmodel);


                    }

                    cevrimiciTumListeAdapter = new cevrimiciTumListeAdapter(listeler, getActivity(), getContext());
                    cevrimicitumlistelerRecylerview.setAdapter(cevrimiciTumListeAdapter);
                    cevrimiciTumListeAdapter.notifyDataSetChanged();
                    profilProgress.dismiss();

                    if(listeler.size() == 0){

                        cevrimicivisibletext.setVisibility(View.VISIBLE);
                        profilProgress.dismiss();


                    }

                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                for (int i = 0; i <= listeler.size() - 1; i++) {

                    if (listeler.get(i).getListeadi().equals(dataSnapshot.getKey())) {

                        listeler.remove(listeler.get(i));

                        cevrimiciTumListeAdapter.notifyItemRemoved(i);
                        cevrimiciTumListeAdapter.notifyItemRangeChanged(i, listeler.size());


                    }
                }

                if (listeler.size() == 0) {

                    cevrimicivisibletext.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        reference.addChildEventListener(childEventListener);
        mSendChildEventListener = childEventListener;


    }

    @Override
    public boolean onBackPressed() {

        System.exit(0);

        return false;
    }
}
