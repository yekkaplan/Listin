package com.alisverisim.yek.listin.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alisverisim.yek.listin.Adapters.bildirimAdapter;
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


public class FragmentBildirim extends Fragment {
    String listeadi;
    View view;
    ArrayList<String> kuidList;
    ListView listView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    TextView listebaslik;
    ImageView backbutton;
    String uid;
    TextView visibletext, ipucutext, altbasliktext;
    com.alisverisim.yek.listin.Adapters.bildirimAdapter bildirimAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bildirimgonder, container, false);

        listeadi = getArguments().getString("listeadi");
        uid = getArguments().getString("uid");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        tanimla();
        listele();
        action();
        return view;

    }

    public void tanimla() {


        backbutton = view.findViewById(R.id.bildirimback);
        listView = view.findViewById(R.id.bildirimgonderlistview);
        visibletext = view.findViewById(R.id.bildirimvisibletext);
        ipucutext = view.findViewById(R.id.ipucutext);
        altbasliktext = view.findViewById(R.id.bildirimtxt);
    }


    public void action() {

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().onBackPressed();

            }
        });


    }

    public void listele() {


        databaseReference = firebaseDatabase.getReference().child("Users").child(uid).child("lists").child(listeadi).child("listeortaklari");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<String> uidList = new ArrayList<>();
                if (dataSnapshot.exists()) {


                    if (listView.getVisibility() != View.VISIBLE) {

                        listView.setVisibility(View.VISIBLE);
                        altbasliktext.setVisibility(View.VISIBLE);
                        visibletext.setVisibility(View.INVISIBLE);
                        ipucutext.setVisibility(View.VISIBLE);
                    }

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        uidList.add(ds.getKey());


                    }

                    if (uidList.size() > 0) {


                        listebaslik = view.findViewById(R.id.bildirimlisteadi);
                        listebaslik.setText(listeadi);
                        bildirimAdapter = new bildirimAdapter(uidList, getContext(), getActivity(), listeadi);
                        listView.setAdapter(bildirimAdapter);
                        bildirimAdapter.notifyDataSetChanged();
                    } else if (uidList.size() == 0) {

                        listView.setVisibility(View.INVISIBLE);
                        altbasliktext.setVisibility(View.INVISIBLE);
                        visibletext.setVisibility(View.VISIBLE);
                        ipucutext.setVisibility(View.INVISIBLE);
                    }
                } else {

                    listView.setVisibility(View.INVISIBLE);
                    altbasliktext.setVisibility(View.INVISIBLE);
                    visibletext.setVisibility(View.VISIBLE);
                    ipucutext.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}


