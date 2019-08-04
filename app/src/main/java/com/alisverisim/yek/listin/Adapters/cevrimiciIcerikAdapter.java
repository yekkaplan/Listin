package com.alisverisim.yek.listin.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alisverisim.yek.listin.Listeners.anamenupopuplistener;
import com.alisverisim.yek.listin.Listeners.cevrimiciiceriklistener;
import com.alisverisim.yek.listin.Models.UrunModels;
import com.alisverisim.yek.listin.Models.notModel;
import com.alisverisim.yek.listin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Type;
import java.util.List;

public class cevrimiciIcerikAdapter extends RecyclerView.Adapter<cevrimiciIcerikAdapter.viewHolder> {


    public String TAG = cevrimiciIcerikAdapter.class.getSimpleName();

    Activity activity;
    Context context;
    String listeadi;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    List<UrunModels> urunList;
    LinearLayout linearLayout;
    List<notModel> notlistesi;

    public cevrimiciIcerikAdapter(Activity activity, Context context, List<UrunModels> urunList, String listeadi) {
        this.activity = activity;
        this.listeadi = listeadi;
        this.context = context;
        this.urunList = urunList;
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

    }

    // view tanımlaması burada yapılacak
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.cevrimiciiceriklayoutmodel, viewGroup, false);

        return new viewHolder(view); // bu layout'dan viewHolder'ı haberdar ettim.
    }


    // vievlara setlemeler yapılacak.
    @Override
    public void onBindViewHolder(@NonNull final viewHolder viewHolder, final int i) {


        viewHolder.uruncheckbox.setText(urunList.get(i).getUrunAdi());
        if (urunList.get(i).getAlindimi().booleanValue()) {

            viewHolder.uruncheckbox.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.DEV_KERN_TEXT_FLAG);
            viewHolder.uruncheckbox.setText(urunList.get(i).getUrunAdi());
            viewHolder.uruncheckbox.setChecked(urunList.get(i).getAlindimi().booleanValue());

        }


        viewHolder.uruncheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (viewHolder.uruncheckbox.isChecked()) {

                    viewHolder.uruncheckbox.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.DEV_KERN_TEXT_FLAG);
                    viewHolder.uruncheckbox.setText(urunList.get(i).getUrunAdi());

                    databaseReference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid()).child("lists").child(listeadi).child("Urunler").child(urunList.get(i).getUrunAdi()).child("Durum");
                    databaseReference.setValue(true);


                } else if (!viewHolder.uruncheckbox.isChecked()) {

                    viewHolder.uruncheckbox.setPaintFlags(0);
                    viewHolder.uruncheckbox.setText(urunList.get(i).getUrunAdi());

                    databaseReference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid()).child("lists").child(listeadi).child("Urunler").child(urunList.get(i).getUrunAdi()).child("Durum");
                    databaseReference.setValue(false);
                }

            }
        });


        // not setleme işlemi :))
        if (urunList.get(i).getNot() != null) {
            viewHolder.visibleLiner.setVisibility(View.VISIBLE);
            viewHolder.not.setText(urunList.get(i).getNot());
        }


        viewHolder.popUpListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String urunAdi = urunList.get(i).getUrunAdi();
                String useruid = firebaseAuth.getUid();
                PopupMenu popup = new PopupMenu(context, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.cevrimiciicerik_popup, popup.getMenu());
                popup.setOnMenuItemClickListener(new cevrimiciiceriklistener(context, activity, listeadi, useruid, urunAdi));
                popup.show();
            }
        });


        viewHolder.not.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {

                databaseReference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid()).child("lists").child(listeadi).child("Urunler").child(urunList.get(i).getUrunAdi()).child("notlar");
                databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

                return true;
            }
        });
    }


    // adapteri oluşturulacak olan listenin size
    @Override
    public int getItemCount() {
        return urunList.size();
    }


    // vievlerin tanımlanma işlemleri
    public class viewHolder extends RecyclerView.ViewHolder {


        CheckBox uruncheckbox;
        ImageView popUpListener;
        TextView not;
        LinearLayout visibleLiner;


        public viewHolder(@NonNull View itemView) {
            super(itemView);


            uruncheckbox = itemView.findViewById(R.id.ccuruncheckbox);
            // urunSil = itemView.findViewById(R.id.ccurunsilimage);
            popUpListener = itemView.findViewById(R.id.cevrimiciicerikpopup);
            not = itemView.findViewById(R.id.not);
            visibleLiner = itemView.findViewById(R.id.notlinearlayout);


        }
    }

}