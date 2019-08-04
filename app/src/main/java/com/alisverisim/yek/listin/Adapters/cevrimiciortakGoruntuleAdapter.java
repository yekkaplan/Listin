package com.alisverisim.yek.listin.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alisverisim.yek.listin.Activitys.MainActivity;
import com.alisverisim.yek.listin.Models.kullanicieklemodel;
import com.alisverisim.yek.listin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class cevrimiciortakGoruntuleAdapter extends BaseAdapter {

    List<kullanicieklemodel> list;
    Context context;
    String listeadi;
    FirebaseUser firebaseUser;
    String kuid;
    Activity activity;
    Boolean cevrimiciOrortak;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public cevrimiciortakGoruntuleAdapter(Activity activity, List<kullanicieklemodel> list, Context context, String listeadi, Boolean cevrimiciOrortak) {
        this.list = list;
        this.context = context;
        this.listeadi = listeadi;
        this.activity = activity;
        this.cevrimiciOrortak = cevrimiciOrortak;
        this.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        this.kuid = firebaseUser.getUid().toString();
        firebaseDatabase = FirebaseDatabase.getInstance();


    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.cevrimiciortaklarmodel, parent, false);
        final TextView ortakadi, ortakmail;

        final CircleImageView ortaklarimageview;
        final ImageView click;
        ortaklarimageview = convertView.findViewById(R.id.ortaklarimageview);
        ortakadi = convertView.findViewById(R.id.ortaklarisimsoyisim);
        ortakmail = convertView.findViewById(R.id.ortaklaremail);
        click = convertView.findViewById(R.id.ortaktemizleicon);
        ortakmail.setText(list.get(position).getEmail());
        final String ortakemail = list.get(position).getEmail();
        String ortakid = list.get(position).getUid();

        databaseReference = firebaseDatabase.getReference("Users").child(ortakid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {


                    String resim_url = dataSnapshot.child("resim").getValue(String.class).toString();


                    if (!resim_url.equals("")) {


                        Picasso.get()
                                .load(dataSnapshot.child("resim").getValue(String.class).toString())
                                .resize(60, 60)
                                .into(ortaklarimageview);

                    }

                    String isim = dataSnapshot.child("isim").getValue(String.class).toString();
                    ortakadi.setText(isim);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (!cevrimiciOrortak) {

            click.setVisibility(View.INVISIBLE);
        } else if (cevrimiciOrortak) {

            // ortak liste kaldırma işlemleri
            click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!MainActivity.mail.equals(ortakemail)) {
                        final TextView tamambutton, iptalbutton;
                        LayoutInflater ınflater = activity.getLayoutInflater();
                        View view = ınflater.inflate(R.layout.siliniyoralert, null);
                        tamambutton = view.findViewById(R.id.ortakgoruntuleuarıtamam);
                        iptalbutton = view.findViewById(R.id.ortakgoruntuleuyarıiptal);
                        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(activity);
                        alert.setView(view);
                        alert.setCancelable(false);
                        final android.support.v7.app.AlertDialog dialog = alert.create();
                        dialog.show();


                        tamambutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                databaseReference = firebaseDatabase.getReference("Users").child(kuid).child("lists").child(listeadi).child("listeortaklari").child(list.get(position).getUid());
                                databaseReference.removeValue();

                                databaseReference = firebaseDatabase.getReference("Users").child(list.get(position).getUid()).child("ortakListeler");

                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                        if (dataSnapshot.exists()) {

                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                if (ds.exists()) {

                                                    String gelenlisteadi, kullaniciuid;

                                                    if (ds.child("listeadi").getValue(String.class) != null) {
                                                        gelenlisteadi = ds.child("listeadi").getValue().toString();
                                                        kullaniciuid = ds.child("kullaniciuid").getValue().toString();

                                                        if (gelenlisteadi.equals(listeadi) && kullaniciuid.equals(kuid)) {
                                                            String key = ds.getKey();
                                                            DatabaseReference databaseReference1 = firebaseDatabase.getReference("Users").child(list.get(position).getUid()).child("ortakListeler").child(key);
                                                            databaseReference1.removeValue();
                                                            dialog.dismiss();
                                                        }
                                                    }
                                                }

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                        });

                        iptalbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();
                            }
                        });


                    } else {

                        Toast.makeText(context, "Kendi hesabınız listenizden silinemez.", Toast.LENGTH_SHORT).show();

                    }


                }
            });
        }

        return convertView;
    }
}
