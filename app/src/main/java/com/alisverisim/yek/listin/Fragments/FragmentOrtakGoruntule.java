package com.alisverisim.yek.listin.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alisverisim.yek.listin.Activitys.MainActivity;
import com.alisverisim.yek.listin.Adapters.cevrimiciortakGoruntuleAdapter;
import com.alisverisim.yek.listin.Models.kullanicieklemodel;
import com.alisverisim.yek.listin.R;
import com.alisverisim.yek.listin.Utils.ChangeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FragmentOrtakGoruntule extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    ListView listView;
    FirebaseUser firebaseUser;
    String listeadi;
    ImageView back;
    TextView visibletext;
    TextView goruntulelisteadi;
    TextView txt;
    Toolbar toolbar;
    View view;
    cevrimiciortakGoruntuleAdapter cevrimiciortakGoruntuleAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ortakgoruntule, container, false);

        listeadi = getArguments().getString("metin");


        firebaseTanimla();
        ortakkontrol();
        toolbarAction();
        return view;

    }

    public void firebaseTanimla() {


        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        listView = view.findViewById(R.id.ortakgoruntulelistview);
        visibletext = view.findViewById(R.id.visibletext);
        txt = view.findViewById(R.id.txt);
    }


    public void ortakkontrol() {

        if (listeadi != null) {


            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Users").child(MainActivity.kuid).child("lists").child(listeadi).child("listeortaklari");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {


                        if (listView.getVisibility() != View.VISIBLE) {

                            listView.setVisibility(View.VISIBLE);
                            txt.setVisibility(View.VISIBLE);
                            visibletext.setVisibility(View.INVISIBLE);

                        }

                        final List<kullanicieklemodel> kullanicilist = new ArrayList<>();


                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            String kuid = ds.getKey().toString();
                            String email = ds.getValue().toString();
                            kullanicieklemodel kullanicieklemodel = new kullanicieklemodel(email, kuid);
                            kullanicilist.add(kullanicieklemodel);

                        }

                        if (kullanicilist.size() > 0) {

                            cevrimiciortakGoruntuleAdapter = new cevrimiciortakGoruntuleAdapter(getActivity(), kullanicilist, getContext(), listeadi, true);
                            cevrimiciortakGoruntuleAdapter.notifyDataSetChanged();
                            listView.setAdapter(cevrimiciortakGoruntuleAdapter);
                        } else if (kullanicilist.size() == 0) {

                            listView.setVisibility(View.INVISIBLE);
                            txt.setVisibility(View.INVISIBLE);
                            visibletext.setVisibility(View.VISIBLE);
                        }

                    } else {

                        listView.setVisibility(View.INVISIBLE);
                        txt.setVisibility(View.INVISIBLE);
                        visibletext.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }


    private void toolbarAction() {

        toolbar = view.findViewById(R.id.ortakiceriktoolbar);
        toolbar.setTitle("Liste Ortakları");
        toolbar.setSubtitle(listeadi);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getActivity().onBackPressed();
                // back button pressed
            }
        });
    }


}
