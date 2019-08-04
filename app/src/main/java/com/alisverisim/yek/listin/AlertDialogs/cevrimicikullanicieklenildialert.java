package com.alisverisim.yek.listin.AlertDialogs;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alisverisim.yek.listin.R;
import com.alisverisim.yek.listin.Utils.FirebaseServices;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class cevrimicikullanicieklenildialert {

    Activity activity;
    FirebaseDatabase firebaseDatabase2;
    DatabaseReference notificationRef;
    FirebaseUser firebaseUser2;
    FirebaseAuth firebaseAuth2;
    HashMap<String, Object> ortakmap;
    String uid, email, listeadi;

    public cevrimicikullanicieklenildialert(Activity activity, String email, String uid, String listeadi) {
        this.activity = activity;
        this.listeadi = listeadi;
        this.email = email;
        this.uid = uid;

        firebaseDatabase2 = FirebaseDatabase.getInstance();
        firebaseAuth2 = FirebaseAuth.getInstance();
        firebaseUser2 = firebaseAuth2.getCurrentUser();
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
                FirebaseServices.karsiyaOrtakEkle(uid, listeadi);
                String senderUserID = firebaseAuth2.getUid();
                dialog.cancel();


                HashMap<String, String> chatnotificationMap = new HashMap<>();
                chatnotificationMap.put("from", senderUserID);
                chatnotificationMap.put("type","request");

                notificationRef.child(uid).push()
                        .setValue(chatnotificationMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                                if(task.isSuccessful()){


                                    Log.i("succes","notification send is succesful");

                                }
                            }
                        });
            }
        });
    }
}
