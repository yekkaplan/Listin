package com.alisverisim.yek.listin.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alisverisim.yek.listin.AlertDialogs.listesilmealert;
import com.alisverisim.yek.listin.Fragments.FragmentCevrimiciIcerik;
import com.alisverisim.yek.listin.Listeners.cevrimicipopuplistener;
import com.alisverisim.yek.listin.Models.cevrimcilistmodel;
import com.alisverisim.yek.listin.R;
import com.alisverisim.yek.listin.Utils.ChangeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class cevrimiciTumListeAdapter extends RecyclerView.Adapter<cevrimiciTumListeAdapter.viewHolder> {


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
                changeFragment.veriGonder(new FragmentCevrimiciIcerik(), listemodel.get(i).getListeadi(), "cevrimiciIcerikFrag");

            }
        });


        viewHolder.cevrimicitumliner.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                HashMap<String, Object> kullanicihashmap = listemodel.get(i).getHashMap();
                final String listeadi = listemodel.get(i).getListeadi();


                listesilmealert listesilmealert = new listesilmealert(activity, kullanicihashmap, listeadi, kuid);

                listesilmealert.ac();

                return true;
            }
        });


    }


    // adapteri oluşturulacak olan listenin size
    @Override
    public int getItemCount() {
        return listemodel.size();
    }


    // vievlerin tanımlanma işlemleri
    public class viewHolder extends RecyclerView.ViewHolder {


        // burada tanımlama sebebi altclass'da erişmesi yani global işte :)
        LinearLayout cevrimicitumliner;
        ImageView icerigir, overflow;
        TextView listeadi;
        Typeface typeface;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/yekfont.ttf");
            listeadi = itemView.findViewById(R.id.cevrimicitumlistebaslik);
            icerigir = itemView.findViewById(R.id.cevrimiciimageview);
            cevrimicitumliner = itemView.findViewById(R.id.cevrimicitumlinearlayout);
        }
    }


}

