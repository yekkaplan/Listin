package com.alisverisim.yek.listin.Listeners;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.widget.Toast;

import com.alisverisim.yek.listin.R;
import com.alisverisim.yek.listin.Utils.FirebaseServices;

public class ortaklistpopuplistener implements PopupMenu.OnMenuItemClickListener {
    Context context;
    String kuid;
    String listeadi;
    String listekey;

    public ortaklistpopuplistener(Context context, String kuid, String listeadi, String listekey) {
        this.context = context;
        this.kuid = kuid;
        this.listeadi = listeadi;
        this.listekey = listekey;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.ortakg:
                FirebaseServices.ortaklistekaldır(listekey);
                FirebaseServices.ortaklistekaldır(listekey, listeadi, kuid);
                Toast.makeText(context, "Listeden ayrıldınız.", Toast.LENGTH_SHORT).show();
                return true;
            default:
        }
        return false;
    }
}