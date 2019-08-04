package com.alisverisim.yek.listin.Utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.alisverisim.yek.listin.R;

import java.util.ArrayList;

public class ChangeFragment {

    private Context context;

    public ChangeFragment(Context context) {
        this.context = context;

    }


    public void change(Fragment fragment, String fragTag) {

        //  Burası activity içinde fragment tanımlaması için kullandıgım fonksiyon
        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_right, R.anim.exit_to_left)
                .addToBackStack(null)
                .replace(R.id.framelayout, fragment, "fragment")
                .commit();

    }


    public void veriGonder(Fragment fragment, String metin, String fragTag) {
        Bundle bundle = new Bundle();
        bundle.putString("metin", metin);
        fragment.setArguments(bundle);

        //  Burası activity içinde fragment tanımlaması için kullandıgım fonksiyon
        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_right, R.anim.exit_to_left)
                .addToBackStack(null)
                .replace(R.id.framelayout, fragment, "fragment")
                .commit();

    }

    public void ikiVeriGonder(Fragment fragment, String listeadi, String kuid, String fragtag) {
        Bundle bundle = new Bundle();
        bundle.putString("listeadi", listeadi);
        bundle.putString("kuid", kuid);
        fragment.setArguments(bundle);

        //  Burası activity içinde fragment tanımlaması için kullandıgım fonksiyon
        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_right, R.anim.exit_to_left)
                .addToBackStack(null)
                .replace(R.id.framelayout, fragment, "fragment")
                .commit();

    }




    public void arraysend(Fragment fragment, String listeadi,String uid) {
        Bundle bundle = new Bundle();
        bundle.putString("uid",uid);
        bundle.putString("listeadi", listeadi);
        fragment.setArguments(bundle);

        //  Burası activity içinde fragment tanımlaması için kullandıgım fonksiyon
        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_right, R.anim.exit_to_left)
                .addToBackStack(null)
                .replace(R.id.framelayout, fragment, "fragment")
                .commit();

    }
}
