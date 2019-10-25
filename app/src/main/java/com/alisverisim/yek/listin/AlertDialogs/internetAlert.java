package com.alisverisim.yek.listin.AlertDialogs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.alisverisim.yek.listin.R;
public class internetAlert {

    Activity activity;
    android.support.v7.app.AlertDialog dialog;

    public internetAlert(Activity activity) {
        this.activity = activity;
    }

    public void ac() {
        LayoutInflater ınflater = activity.getLayoutInflater();

        View view = ınflater.inflate(R.layout.internetyokalert, null);


        TextView button = view.findViewById(R.id.internetkoptutamambtn);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(activity);
        alert.setView(view);
        alert.setCancelable(true);
        dialog = alert.create();


        dialog.show();


        // email alma ve kontrol işlemleri
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
            }
        });


    }

    public void kapat() {


        dialog.dismiss();

    }

}
