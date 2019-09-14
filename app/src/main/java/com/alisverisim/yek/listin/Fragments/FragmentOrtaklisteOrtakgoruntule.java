package com.alisverisim.yek.listin.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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


public class FragmentOrtaklisteOrtakgoruntule extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String kuid;
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
        view = inflater.inflate(R.layout.fragment_ortaklisteortakgoruntule, container, false);

        listeadi = getArguments().getString("listeadi");
        kuid = getArguments().getString(("kuid"));
        firebaseTanimla();

        toolbarAction();
        ortakkontrol();
        return view;

    }

    public void firebaseTanimla() {


        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        listView = view.findViewById(R.id.ortaklistortakgoruntulelistview);
        visibletext = view.findViewById(R.id.ortaklarvisibletext);
        txt = view.findViewById(R.id.ortakgoruntuletxt);
    }



    public void ortakkontrol() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users").child(kuid).child("lists").child(listeadi).child("listeortaklari");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    final List<kullanicieklemodel> kullanicilist = new ArrayList<>();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        String kuid = ds.getKey().toString();
                        String email = ds.getValue(String.class).toString();

                        kullanicieklemodel kullanicieklemodel = new kullanicieklemodel(email, kuid);
                        kullanicilist.add(kullanicieklemodel);
                    }


                    if (kullanicilist.size() > 0) {

                        cevrimiciortakGoruntuleAdapter = new cevrimiciortakGoruntuleAdapter(getActivity(), kullanicilist, getContext(), listeadi, false);
                        cevrimiciortakGoruntuleAdapter.notifyDataSetChanged();
                        listView.setAdapter(cevrimiciortakGoruntuleAdapter);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void toolbarAction() {

        toolbar = view.findViewById(R.id.ortaklisteortakgoruntuletoolbar);
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



