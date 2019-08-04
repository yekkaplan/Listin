package com.alisverisim.yek.listin.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
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

import com.alisverisim.yek.listin.Listeners.cevrimiciiceriklistener;
import com.alisverisim.yek.listin.Models.UrunModels;
import com.alisverisim.yek.listin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ortakListeİcerikAdapter extends RecyclerView.Adapter<ortakListeİcerikAdapter.viewHolder> {


    Activity activity;
    Context context;
    String listeadi;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String Kuid;
    List<UrunModels> urunList;


    public ortakListeİcerikAdapter(Activity activity, Context context, List<UrunModels> urunList, String listeadi, String Kuid) {
        this.activity = activity;
        this.listeadi = listeadi;
        this.context = context;
        this.urunList = urunList;
        this.Kuid = Kuid;
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

    }

    // view tanımlaması burada yapılacak
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.ortaklistmodellayout, viewGroup, false);


        return new viewHolder(view); // bu layout'dan viewHolder'ı haberdar ettim.
    }


    // vievlara setlemeler yapılacak.
    @Override
    public void onBindViewHolder(@NonNull final viewHolder viewHolder, final int i) {


        viewHolder.ortaklistcheckbox.setText(urunList.get(i).getUrunAdi().toString());
        if (urunList.get(i).getAlindimi().booleanValue()) {

            viewHolder.ortaklistcheckbox.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.DEV_KERN_TEXT_FLAG);
            viewHolder.ortaklistcheckbox.setText(urunList.get(i).getUrunAdi());
            viewHolder.ortaklistcheckbox.setChecked(urunList.get(i).getAlindimi().booleanValue());

        }


        viewHolder.ortaklistcheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (viewHolder.ortaklistcheckbox.isChecked()) {


                    viewHolder.ortaklistcheckbox.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.DEV_KERN_TEXT_FLAG);
                    viewHolder.ortaklistcheckbox.setText(urunList.get(i).getUrunAdi());

                    databaseReference = firebaseDatabase.getReference("Users").child(Kuid).child("lists").child(listeadi).child("Urunler").child(urunList.get(i).getUrunAdi()).child("Durum");
                    databaseReference.setValue(true);


                } else if (!viewHolder.ortaklistcheckbox.isChecked()) {
                    viewHolder.ortaklistcheckbox.setPaintFlags(0);
                    viewHolder.ortaklistcheckbox.setText(urunList.get(i).getUrunAdi());
                    databaseReference = firebaseDatabase.getReference("Users").child(Kuid).child("lists").child(listeadi).child("Urunler").child(urunList.get(i).getUrunAdi()).child("Durum");
                    databaseReference.setValue(false);
                }

            }
        });

        viewHolder.ortaklistepopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String urunAdi = urunList.get(i).getUrunAdi();
                PopupMenu popup = new PopupMenu(context, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.cevrimiciicerik_popup, popup.getMenu());
                popup.setOnMenuItemClickListener(new cevrimiciiceriklistener(context, activity, listeadi, Kuid, urunAdi));
                popup.show();
            }
        });

        if (urunList.get(i).getNot() != null) {
            viewHolder.ortakliner.setVisibility(View.VISIBLE);
            viewHolder.not.setText(urunList.get(i).getNot());
        }


        viewHolder.not.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {

                databaseReference = firebaseDatabase.getReference("Users").child(Kuid).child("lists").child(listeadi).child("Urunler").child(urunList.get(i).getUrunAdi()).child("notlar");
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


        // burada tanımlama sebebi altclass'da erişmesi yani global işte :)

        CheckBox ortaklistcheckbox;
        ImageView ortaklistepopup;
        LinearLayout ortakliner;
        Typeface typeface;
        TextView not;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            ortaklistcheckbox = itemView.findViewById(R.id.ortaklistcheckbox);
            typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/yekfont.ttf");
            ortaklistepopup = itemView.findViewById(R.id.ortaklisteiceriklistener);
            ortakliner = itemView.findViewById(R.id.ortaklistenotlinear);
            not = itemView.findViewById(R.id.ortaklistenot);

        }
    }

}