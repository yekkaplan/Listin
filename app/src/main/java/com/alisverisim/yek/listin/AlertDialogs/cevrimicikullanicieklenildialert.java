package com.alisverisim.yek.listin.AlertDialogs;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.alisverisim.yek.listin.R;
import com.alisverisim.yek.listin.Utils.FirebaseServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;


public class cevrimicikullanicieklenildialert {

    Activity activity;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference notificationRef;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference, databaseReference2, databaseReference3;
    HashMap<String, Object> ortakmap;
    String uid, email, listeadi;

    public cevrimicikullanicieklenildialert(Activity activity, String email, String uid, String listeadi) {
        this.activity = activity;
        this.listeadi = listeadi;
        this.email = email;
        this.uid = uid;
        firebaseDatabase = FirebaseDatabase.getInstance();
        notificationRef = FirebaseDatabase.getInstance().getReference().child("Notifications");
    }


    public void ac() {

        LayoutInflater ınflater = activity.getLayoutInflater();
        View view = ınflater.inflate(R.layout.kontrolalert, null);
        TextView devamtext = view.findViewById(R.id.devamtext);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(activity);
        alert.setView(view);
        alert.setCancelable(false);
        final android.support.v7.app.AlertDialog dialog = alert.create();
        dialog.show();
        ortakmap = new HashMap<>();


        devamtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseServices.ortakEkle(uid, email, listeadi);


                // karşı kullanıcıya ortak ekleme işlemi başlangıç


                HashMap<String, Object> map1 = new HashMap<>();
                HashMap<String, Object> map2 = new HashMap<>();


                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                databaseReference2 = firebaseDatabase.getReference("Users").child(uid).child("ortakListeler").push();
                String push = databaseReference2.getKey();
                databaseReference = firebaseDatabase.getReference("Users").child(uid).child("ortakListeler").child(push);

                map1.put("listeadi", listeadi);
                map2.put("kullaniciuid", firebaseUser.getUid());
                databaseReference.updateChildren(map1);
                databaseReference.updateChildren(map2);

                // karşı kullanıcıya ortak ekleme işlemi bitiş


                String senderUserID = firebaseUser.getUid();
                dialog.cancel();
                HashMap<String, String> chatnotificationMap = new HashMap<>();
                chatnotificationMap.put("from", senderUserID);
                chatnotificationMap.put("type", "request");

                notificationRef.child(uid).push()
                        .setValue(chatnotificationMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                                if (task.isSuccessful()) {


                                    Log.i("succes", "notification send is succesful");

                                }
                            }
                        });
            }
        });
    }
}
