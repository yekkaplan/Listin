package com.alisverisim.yek.listin.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alisverisim.yek.listin.Activitys.LoginActivity;
import com.alisverisim.yek.listin.Activitys.MainActivity;
import com.alisverisim.yek.listin.Adapters.cevrimiciTumListeAdapter;
import com.alisverisim.yek.listin.AlertDialogs.cevrimiciListeEkleAlert;
import com.alisverisim.yek.listin.Interfaces.IOnBackPressed;
import com.alisverisim.yek.listin.Listeners.anamenupopuplistener;
import com.alisverisim.yek.listin.Listeners.cevrimicipopuplistener;
import com.alisverisim.yek.listin.Models.cevrimcilistmodel;
import com.alisverisim.yek.listin.R;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    cevrimcilistmodel cevrimcilistmodel;
    String listeadi;
    SpotsDialog profilProgress;
    TextView tumlistelervisibletext;
    ImageView popup;
    Context context;
    ImageView paylasbutton;
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

    public void firebaseTanımlanması() {


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        tumlistelervisibletext = view.findViewById(R.id.tumlistelervisibletext);
    }


    public void viewTanimlanması() {


        //  paylasbutton = view.findViewById(R.id.cevrimicipaylasbutton);

        floatingActionButton = view.findViewById(R.id.cevrimicifab);

        // popup = view.findViewById(R.id.cevrimicipopupTextview);

        cevrimicitumlistelerRecylerview = view.findViewById(R.id.recycler_view);


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


        toolbar.setTitle("Listin & Çevrimiçi Listeler");
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


        profilProgress = new SpotsDialog(getActivity(), R.style.Custom);
        profilProgress.show();
        new Thread(new Runnable() {
            public void run() {
                reference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid()).child("lists");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        listeler = new ArrayList<>();

                        if (dataSnapshot.exists()) {

                            if (dataSnapshot.getChildren() != null) {

                                if (cevrimicitumlistelerRecylerview.getVisibility() != View.VISIBLE) {

                                    cevrimicitumlistelerRecylerview.setVisibility(View.VISIBLE);
                                    tumlistelervisibletext.setVisibility(View.INVISIBLE);

                                }


                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    HashMap<String, Object> hashMap = new HashMap<>();

                                    listeadi = ds.getKey();
                                    Long urunsayisi = ds.child("Urunler").getChildrenCount();
                                    Long ortaksayisi = ds.child("listeortaklari").getChildrenCount();
                                    for (DataSnapshot veri : ds.child("listeortaklari").getChildren()) {

                                        hashMap.put(veri.getKey(), veri.getValue());
                                    }
                                    cevrimcilistmodel = new cevrimcilistmodel(listeadi, ortaksayisi, urunsayisi, hashMap);
                                    listeler.add(cevrimcilistmodel);


                                }
                            }
                        }

                        if (listeler.size() > 0) {

                            cevrimiciTumListeAdapter = new cevrimiciTumListeAdapter(listeler, getActivity(), getContext());
                            cevrimiciTumListeAdapter.notifyDataSetChanged();
                            cevrimicitumlistelerRecylerview.setAdapter(cevrimiciTumListeAdapter);
                            cevrimicitumlistelerRecylerview.notifyDataSetChanged();
                            profilProgress.dismiss();


                        } else if (listeler.size() == 0) {
                            cevrimicitumlistelerRecylerview.setVisibility(View.INVISIBLE);
                            tumlistelervisibletext.setVisibility(View.VISIBLE);

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
}
