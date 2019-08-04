package com.alisverisim.yek.listin.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseServices {
    private static FirebaseDatabase firebaseDatabase, firebaseDatabase2;
    private static FirebaseAuth firebaseAuth, firebaseAuth2;
    private static DatabaseReference databaseReference, databaseReference2, databaseReference3;
    private static FirebaseUser firebaseUser, firebaseUser2;
    private static HashMap<String, Object> ortakmap;


    public static void ortakEkle(String uid, String email, String listeadi) {

        // Log.i("xxxxx", "Eklenecek uid" + uid + "  email" + email + "   kullaniciuid" + firebaseUser.getUid() + "  listeadi" + listeadi);


        ortakmap = new HashMap<String, Object>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference();
        databaseReference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid()).child("lists").child(listeadi).child("listeortaklari");
        ortakmap.put(uid, email);
        databaseReference.updateChildren(ortakmap);

    }

    public static void karsiyaOrtakEkle(String uid, String listeadi) {


        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference2 = firebaseDatabase.getReference("Users").child(uid).child("ortakListeler").push();
        String push = databaseReference2.getKey();
        databaseReference = firebaseDatabase.getReference("Users").child(uid).child("ortakListeler").child(push).child("kullaniciuid");
        databaseReference3 = firebaseDatabase.getReference("Users").child(uid).child("ortakListeler").child(push).child("listeadi");
        databaseReference.setValue(firebaseUser.getUid().toString());
        databaseReference3.setValue(listeadi);

    }


    // tek paremetreli olan kendi vt alanına müdahale edicek.
    public static boolean ortaklistekaldır(String listekey) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid()).child("ortakListeler").child(listekey);
        if (databaseReference.removeValue().isSuccessful()) {

            return true;

        }

        return false;
    }

    // tek paremetreli olan kendi vt alanına müdahale edicek.
    public static boolean ortaklistekaldır(String listekey, String listeadi, String kuid) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users").child(kuid).child("lists").child(listeadi).child("listeortaklari").child(firebaseUser.getUid());
        if (databaseReference.removeValue().isSuccessful()) {

            return true;

        }

        return false;
    }

    // tek paremetreli olan kendi vt alanına müdahale edicek.
    public static void listesilortaktemizle(String listeadi) {
        final List<String> liste = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String firebaseuser = firebaseUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users").child(firebaseuser).child("lists").child(listeadi).child("listeortaklari");

        databaseReference2 = firebaseDatabase.getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        String key = ds.getKey().toString();

                        databaseReference2.child("Users").child(key).child("kullaniciuid").orderByChild(firebaseuser).getRef().removeValue();

                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public static void sendFCMPush(String phonetoken, String listeadi, Context context) {

        final String Legacy_SERVER_KEY = "AIzaSyCFlyOwjjlYUUcK31MoyxBY4jALIGK_gjg";
        String msg = "'" + listeadi + "' adlı listenizde yeni bir güncelleme var.";
        String title = "Listenizde güncelleme var.";
        String token = phonetoken;

        JSONObject obj = null;
        JSONObject objData = null;
        JSONObject dataobjData = null;

        try {
            obj = new JSONObject();
            objData = new JSONObject();

            objData.put("body", msg);
            objData.put("title", title);
            objData.put("sound", "default");
            objData.put("icon", "default"); //   icon_name image must be there in drawable
            objData.put("tag", token);
            objData.put("priority", "high");

            dataobjData = new JSONObject();
            dataobjData.put("text", msg);
            dataobjData.put("title", title);

            obj.put("to", token);
            //obj.put("priority", "high");

            obj.put("notification", objData);
            obj.put("data", dataobjData);
            Log.e("!_@rj@_@@_PASS:>", obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, "https://fcm.googleapis.com/fcm/send", obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("!_@@_SUCESS", response + "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("!_@@_Errors--", error + "");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "key=" + Legacy_SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);
    }


}


