package com.alisverisim.yek.listin.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alisverisim.yek.listin.Activitys.LoginActivity;
import com.alisverisim.yek.listin.Activitys.MainActivity;
import com.alisverisim.yek.listin.Models.kullanicieklemodel;
import com.alisverisim.yek.listin.R;
import com.alisverisim.yek.listin.Utils.FirebaseServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pd.chocobar.ChocoBar;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class bildirimAdapter extends BaseAdapter {

    List<String> list;
    Context context;
    Activity activity;
    FirebaseDatabase firebaseDatabase;
    String Listeadi;
    String gemail;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference,databaseReference2;
    public bildirimAdapter(List<String> list, Context context, Activity activity, String Listeadi) {
        this.list = list;
        this.context = context;
        this.activity = activity;
        this.Listeadi = Listeadi;
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

        convertView = LayoutInflater.from(context).inflate(R.layout.bildirimadapter, parent, false);


        final TextView email, isim;
        final CircleImageView profil;
        final LinearLayout linear;
        email = convertView.findViewById(R.id.bildirimemail);
        isim = convertView.findViewById(R.id.bildirimisimvesoyisim);
        profil = convertView.findViewById(R.id.bildirimimageview);
        linear = convertView.findViewById(R.id.bildirimlinear);


        databaseReference2 = firebaseDatabase.getReference("Users").child(list.get(position));
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {


                    String resim_url = dataSnapshot.child("resim").getValue(String.class).toString();


                    if (!resim_url.equals("")) {


                        Picasso.get()
                                .load(dataSnapshot.child("resim").getValue(String.class).toString())
                                .resize(60, 60)
                                .into(profil);

                    }

                    String gisim = dataSnapshot.child("isim").getValue(String.class).toString();
                    isim.setText(gisim);

                    gemail = dataSnapshot.child("email").getValue(String.class);
                    email.setText(gemail);
                    databaseReference2.removeEventListener(this);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth = FirebaseAuth.getInstance();
                firebaseUser = firebaseAuth.getCurrentUser();

                if (!list.get(position).equals(firebaseUser.getUid())) {


                    databaseReference = firebaseDatabase.getReference().child("Users").child(list.get(position)).child("device_token");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            if (dataSnapshot.getValue(String.class) != null) {

                                FirebaseServices.sendFCMPush(dataSnapshot.getValue(String.class), Listeadi, context);

                                ChocoBar.builder().setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary))
                                        .setTextSize(16)
                                        .setTextColor(Color.parseColor("#FFFFFF"))
                                        .setTextTypefaceStyle(Typeface.NORMAL)
                                        .setText("Bildirim gönderildi.")
                                        .setMaxLines(2)
                                        .centerText()
                                        .setIcon(R.drawable.ic_snackbar)
                                        .setActivity(activity)
                                        .setDuration(ChocoBar.LENGTH_SHORT)
                                        .build()
                                        .show();


                                databaseReference.removeEventListener(this);
                                linear.setClickable(false);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else if (list.get(position).equals(firebaseUser.getUid())) {


                    ChocoBar.builder().setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary))
                            .setTextSize(16)
                            .setTextColor(Color.parseColor("#FFFFFF"))
                            .setTextTypefaceStyle(Typeface.NORMAL)
                            .setText("Kendi hesabınıza bildirim yollayamazsınız.")
                            .setMaxLines(2)
                            .centerText()
                            .setIcon(R.drawable.ic_snackbar)
                            .setActivity(activity)
                            .setDuration(ChocoBar.LENGTH_SHORT)
                            .build()
                            .show();

                }

            }
        });


        return convertView;


    }


}
