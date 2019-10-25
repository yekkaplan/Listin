package com.alisverisim.yek.listin.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alisverisim.yek.listin.AlertDialogs.listesilmealert;
import com.alisverisim.yek.listin.Fragments.FragmentCevrimiciIcerik;
import com.alisverisim.yek.listin.Models.cevrimcilistmodel;
import com.alisverisim.yek.listin.R;
import com.alisverisim.yek.listin.Utils.ChangeFragment;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class cevrimiciTumListeAdapter extends RecyclerView.Adapter<cevrimiciTumListeAdapter.viewHolder> {
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();


    List<cevrimcilistmodel> listemodel;
    Activity activity;
    Context context;
    String kuid;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    public cevrimiciTumListeAdapter(List<cevrimcilistmodel> listemodel, Activity activity, Context context) {
        this.listemodel = listemodel;
        this.activity = activity;
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        kuid = firebaseAuth.getCurrentUser().getUid();
    }

    // view tanımlaması burada yapılacak
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.cevrimicilistelermodellayout, viewGroup, false);


        return new viewHolder(view); // bu layout'dan viewHolder'ı haberdar ettim.
    }


    // vievlara setlemeler yapılacak.
    @Override
    public void onBindViewHolder(@NonNull final viewHolder viewHolder, final int i) {

        viewHolder.listeadi.setText(listemodel.get(i).getListeadi().toString());


        viewHolder.cevrimicitumliner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // fragment değiştirdim ve liste adını yolladım :)
                ChangeFragment changeFragment = new ChangeFragment(context);
                changeFragment.veriGonder(new FragmentCevrimiciIcerik(), listemodel.get(i).getListeadi(), "Fragment");

            }
        });


        HashMap<String, Object> kullanicihashmap = listemodel.get(i).getHashMap();
        final String listeadi = listemodel.get(i).getListeadi();


        viewHolder.onBind(kullanicihashmap, listeadi);


    }


    // adapteri oluşturulacak olan listenin size
    @Override
    public int getItemCount() {
        return listemodel.size();
    }


    public void saveStates(Bundle outState) {
        viewBinderHelper.saveStates(outState);
    }

    public void restoreStates(Bundle inState) {
        viewBinderHelper.restoreStates(inState);
    }

    // vievlerin tanımlanma işlemleri
    public class viewHolder extends RecyclerView.ViewHolder {


        // burada tanımlama sebebi altclass'da erişmesi yani global işte :)
        RelativeLayout cevrimicitumliner;
        ImageView icerigir;
        TextView listeadi;
        Typeface typeface;
        TextView listesilSwipe;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/yekfont.ttf");
            listeadi = itemView.findViewById(R.id.cevrimicitumlistebaslik);
            icerigir = itemView.findViewById(R.id.cevrimiciimageview);
            listesilSwipe = itemView.findViewById(R.id.liste_silswipe);
            cevrimicitumliner = itemView.findViewById(R.id.cevrimic_front_layout);

        }

        private void onBind(final HashMap<String, Object> kullanicihashmap, final String listeadi) {


            listesilSwipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listesilmealert listesilmealert = new listesilmealert(activity, kullanicihashmap, listeadi, kuid);

                    listesilmealert.ac();

                }
            });

        }

    }


}

