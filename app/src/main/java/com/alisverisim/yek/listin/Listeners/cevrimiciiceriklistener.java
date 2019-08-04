package com.alisverisim.yek.listin.Listeners;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;

import com.alisverisim.yek.listin.AlertDialogs.notEkleAlertDialog;
import com.alisverisim.yek.listin.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class cevrimiciiceriklistener implements PopupMenu.OnMenuItemClickListener {
    Context context;
    String listeadi;
    String userUid;
    String urunAdi;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Activity activity;

    public cevrimiciiceriklistener(Context context, Activity activity, String listeAdi, String userUid, String urunAdi) {
        this.context = context;
        this.listeadi = listeAdi;
        this.activity = activity;
        this.userUid = userUid;
        this.urunAdi = urunAdi;
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.urunsilpop:

                urunsil();


                return true;


            case R.id.urunenotekle:

                notEkleAlertDialog notEkleAlertDialog = new notEkleAlertDialog(activity, listeadi, userUid, urunAdi);
                notEkleAlertDialog.ac();
                return true;
            default:
        }
        return false;
    }


    private boolean urunsil() {

        databaseReference = firebaseDatabase.getReference("Users").child(userUid).child("lists").child(listeadi).child("Urunler").child(urunAdi);
        databaseReference.removeValue();
        return true;

    }


}