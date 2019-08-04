package com.alisverisim.yek.listin.Utils;

import android.content.Context;

import com.alisverisim.yek.listin.R;

import dmax.dialog.SpotsDialog;

public class progressClass {

    public static SpotsDialog profilProgress;


    public static void progressAktif(Context context) {
        profilProgress = new SpotsDialog(context, R.style.Custom);
        profilProgress.setCancelable(false);
        profilProgress.show();
    }

    public static  boolean progressPasif(){


        if(profilProgress == null){

            return  false;
        }else{

            profilProgress.dismiss();
            return  true;

        }


    }


}
