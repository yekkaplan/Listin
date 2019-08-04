package com.alisverisim.yek.listin.AlertDialogs;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alisverisim.yek.listin.R;
import com.alisverisim.yek.listin.Utils.progressClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sifrenimiunuttunalert {

    Activity activity;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    progressClass progressClass;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference, databaseReference2;


    public sifrenimiunuttunalert(Activity activity) {
        this.activity = activity;
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference();


    }

    public void ac() {



        LayoutInflater ınflater = activity.getLayoutInflater();

        View view = ınflater.inflate(R.layout.sifrenimiunuttun, null);


        final EditText editText = view.findViewById(R.id.sifresifirlaedittext);
        TextView button = view.findViewById(R.id.sifresifirlatext);
        TextView iptalbutton = view.findViewById(R.id.sifresifirlaiptal);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(activity);
        alert.setView(view);
        alert.setCancelable(false);
        final android.support.v7.app.AlertDialog dialog = alert.create();


        dialog.show();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressClass.progressAktif(activity);


                String email = editText.getText().toString();

                FirebaseAuth auth = FirebaseAuth.getInstance();

                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(activity, "Şifre sıfırlama e-mail'i gönderildi! E-mail adresinizi kontrol edin.", Toast.LENGTH_LONG).show();
                                    progressClass.progressPasif();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(activity, "E-mail adresinizi kontrol edin.", Toast.LENGTH_LONG).show();
                                    progressClass.progressPasif();

                                }
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

    }


}
