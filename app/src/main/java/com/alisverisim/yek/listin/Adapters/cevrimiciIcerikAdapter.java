package com.alisverisim.yek.listin.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alisverisim.yek.listin.AlertDialogs.notEkleAlertDialog;
import com.alisverisim.yek.listin.Models.UrunModels;
import com.alisverisim.yek.listin.R;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class cevrimiciIcerikAdapter extends RecyclerView.Adapter<cevrimiciIcerikAdapter.viewHolder> {

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public String TAG = cevrimiciIcerikAdapter.class.getSimpleName();

    Activity activity;
    Context context;
    String listeadi;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    ArrayList<UrunModels> urunList;


    public cevrimiciIcerikAdapter(Activity activity, Context context, ArrayList<UrunModels> urunList, String listeadi) {
        this.activity = activity;
        this.listeadi = listeadi;
        this.context = context;
        this.urunList = urunList;
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference;
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


        viewBinderHelper.bind(viewHolder.swipeRevealLayout, urunList.get(i).getUrunAdi());
        viewHolder.bind(urunList.get(i));

        viewHolder.urunAdi.setText(urunList.get(i).getUrunAdi());
        if (urunList.get(i).getAlindimi().booleanValue()) {


            viewHolder.uruncheckbox.setChecked(urunList.get(i).getAlindimi().booleanValue());

        }


        viewHolder.uruncheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {

                    databaseReference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid()).child("lists").child(listeadi).child("Urunler").child(urunList.get(i).getUrunAdi()).child("Durum");
                    databaseReference.setValue(true);

                } else if (!isChecked) {


                    databaseReference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid()).child("lists").child(listeadi).child("Urunler").child(urunList.get(i).getUrunAdi()).child("Durum");
                    databaseReference.setValue(false);
                }
            }
        });


        // not setleme işlemi :))
        if (urunList.get(i).getNot() != null) {
            viewHolder.not.setVisibility(View.VISIBLE);
            viewHolder.not.setText(urunList.get(i).getNot());
        } else {

            viewHolder.not.setVisibility(View.INVISIBLE);
        }



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



    public void saveStates(Bundle outState) {
        viewBinderHelper.saveStates(outState);
    }

    public void restoreStates(Bundle inState) {
        viewBinderHelper.restoreStates(inState);
    }

    // vievlerin tanımlanma işlemleri
    public class viewHolder extends RecyclerView.ViewHolder {

        AppCompatCheckBox uruncheckbox;
        TextView not, silSwipe, notSwipe,notDuzenleSwipe;
        TextView urunAdi;
        SwipeRevealLayout swipeRevealLayout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            uruncheckbox = itemView.findViewById(R.id.ccuruncheckbox);
            not = itemView.findViewById(R.id.not);
            silSwipe = itemView.findViewById(R.id.silSwipe);
            notSwipe = itemView.findViewById(R.id.notEkleSwipe);
            notDuzenleSwipe = itemView.findViewById(R.id.notDuzenleSwipe);
            urunAdi = itemView.findViewById(R.id.urunadiTextfield);
            swipeRevealLayout = itemView.findViewById(R.id.swipe_layout);

        }


        private void bind(final UrunModels u){



            silSwipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    databaseReference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid()).child("lists").child(listeadi).child("Urunler").child(u.getUrunAdi());
                    databaseReference.removeValue();
                }
            });

            notSwipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    notEkle(u);




                }
            });

            notDuzenleSwipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    notDuzenle(u);


                }
            });

        }




        private void notEkle(final UrunModels u){


            LayoutInflater ınflater = activity.getLayoutInflater();
            View view = ınflater.inflate(R.layout.noteklealert, null);
            TextView notekle, iptal;
            final EditText notedit;
            notekle = view.findViewById(R.id.noteklebutton);
            iptal = view.findViewById(R.id.notekleiptalbutton);
            notedit = view.findViewById(R.id.notedittext);
            android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(activity);
            alert.setView(view);
            alert.setCancelable(true);
            final android.support.v7.app.AlertDialog dialog = alert.create();

            dialog.show();


            notekle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    String not = notedit.getText().toString();
                    databaseReference = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("lists").child(listeadi).child("Urunler").child(u.getUrunAdi()).child("notlar");
                    databaseReference.setValue(not);
                    dialog.dismiss();
                    swipeRevealLayout.close(true);

                }
            });


            iptal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    dialog.dismiss();
                }
            });



        }


        private void notDuzenle(final UrunModels u){


            LayoutInflater ınflater = activity.getLayoutInflater();
            View view = ınflater.inflate(R.layout.notduzenlealert, null);
            TextView notekle, iptal;
            final EditText notedit;
            notekle = view.findViewById(R.id.noteklebutton);
            iptal = view.findViewById(R.id.notekleiptalbutton);
            notedit = view.findViewById(R.id.notedittext);
            android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(activity);
            alert.setView(view);
            alert.setCancelable(true);
            final android.support.v7.app.AlertDialog dialog = alert.create();

            dialog.show();


            notekle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    String not = notedit.getText().toString();
                    databaseReference = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("lists").child(listeadi).child("Urunler").child(u.getUrunAdi()).child("notlar");
                    databaseReference.setValue(not);
                    dialog.dismiss();
                    swipeRevealLayout.close(true);

                }
            });


            iptal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    dialog.dismiss();
                }
            });



        }


    }

}