package com.alisverisim.yek.listin.AlertDialogs;

import android.app.Activity;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alisverisim.yek.listin.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.internal.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class bildirimAlert {

    Activity activity;
    android.support.v7.app.AlertDialog dialog;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<String> uidList;
    String listeadi;
    int i;


    public bildirimAlert(Activity activity, ArrayList<String> uidList, String listeadi) {
        this.activity = activity;
        this.uidList = uidList;
        this.listeadi = listeadi;
    }

    public void ac() {
        LayoutInflater ınflater = activity.getLayoutInflater();
        View view = ınflater.inflate(R.layout.bildirimgonderalert, null);
        TextView sendBildirim = view.findViewById(R.id.onaylıyorumButton);
        TextView iptal = view.findViewById(R.id.iptalbuttonbildirim);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(activity);
        alert.setView(view);
        alert.setCancelable(true);
        dialog = alert.create();

        dialog.show();
        sendBildirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendPush();
                dialog.dismiss();
            }
        });


        iptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


    private void sendPush() {


        firebaseDatabase = FirebaseDatabase.getInstance();
        if (uidList.size() > 0) {

                for(final String s : uidList){

                databaseReference = firebaseDatabase.getReference().child("Users").child(s).child("device_token");

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        if (dataSnapshot.getValue(String.class) != null) {

                            sendFCMPush(dataSnapshot.getValue(String.class));

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        }
    }

    private void sendFCMPush(String phonetoken) {

        final String Legacy_SERVER_KEY = "AIzaSyCFlyOwjjlYUUcK31MoyxBY4jALIGK_gjg";
        String msg = "'" + listeadi + "' adlı listenize yeni ürün eklendi.";
        String title = "Yeni ürün eklendi.";
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
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);
    }

}
