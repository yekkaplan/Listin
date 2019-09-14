package com.alisverisim.yek.listin.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alisverisim.yek.listin.AlertDialogs.ortaklistesilmealert;
import com.alisverisim.yek.listin.Fragments.FragmentOrtakListeİcerik;
import com.alisverisim.yek.listin.Listeners.ortaklistpopuplistener;
import com.alisverisim.yek.listin.Models.ortaklistmodel;
import com.alisverisim.yek.listin.R;
import com.alisverisim.yek.listin.Utils.ChangeFragment;
import com.alisverisim.yek.listin.Utils.FirebaseServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class cevrimiciOrtakListeAdapter extends RecyclerView.Adapter<cevrimiciOrtakListeAdapter.viewHolder> {

    String kuid;
    String listeadi;
    String listekey;
    List<ortaklistmodel> liste;
    Activity activity;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;


    public cevrimiciOrtakListeAdapter(List<ortaklistmodel> liste, Activity activity, Context context) {
        this.liste = liste;
        this.activity = activity;
        this.context = context;

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    // view tanımlaması burada yapılacak
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.ortaklistemodellayout, viewGroup, false);


        return new viewHolder(view); // bu layout'dan viewHolder'ı haberdar ettim.
    }


    // vievlara setlemeler yapılacak.
    @Override
    public void onBindViewHolder(@NonNull final viewHolder viewHolder, final int i) {

        viewHolder.baslikTextview.setText(liste.get(i).getListeadi());


        viewHolder.ortaklarliner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ChangeFragment changeFragment = new ChangeFragment(context);
                changeFragment.ikiVeriGonder(new FragmentOrtakListeİcerik(), liste.get(i).getListeadi(), liste.get(i).getKullaniciuid(), "ortaklisteIcerikFrag");


            }
        });


        viewHolder.ortaklarliner.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                kuid = liste.get(i).getKullaniciuid();
                listeadi = liste.get(i).getListeadi();
                listekey = liste.get(i).getListekey();
                ortaklistesilmealert ortaklistesilmealert = new ortaklistesilmealert(activity, listeadi, kuid, listekey);
                ortaklistesilmealert.ac();
                return false;
            }
        });
        /*

                Toast.makeText(context, "Listeden ayrıldınız.", Toast.LENGTH_SHORT).show();

                */

    }


    // adapteri oluşturulacak olan listenin size
    @Override
    public int getItemCount() {
        return liste.size();
    }


    // vievlerin tanımlanma işlemleri
    public class viewHolder extends RecyclerView.ViewHolder {


        // burada tanımlama sebebi altclass'da erişmesi yani global işte :)

        TextView baslikTextview;
        ImageView overflow;
        Typeface typeface;
        LinearLayout ortaklarliner;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/yekfont.ttf");
            ortaklarliner = itemView.findViewById(R.id.ortaklartumlisteliner);
            baslikTextview = itemView.findViewById(R.id.cortaktumlistebaslik);
        }
    }

}