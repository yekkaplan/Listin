package com.alisverisim.yek.listin.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.alisverisim.yek.listin.Fragments.FragmentCevrimici;
import com.alisverisim.yek.listin.Fragments.FragmentOrtakListe;
import com.alisverisim.yek.listin.Fragments.FragmentProfil;
import com.alisverisim.yek.listin.Interfaces.IOnBackPressed;
import com.alisverisim.yek.listin.R;
import com.alisverisim.yek.listin.Utils.ChangeFragment;
import com.alisverisim.yek.listin.Utils.NetworkChangeReceiver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    ChangeFragment changeFragment;
    FirebaseDatabase firebaseDatabase;
    public static BottomBar bottomBar;
    public static String kuid, mail;
    public static int state = R.id.tab_cevrimici;


    private static final String LOG_TAG = "Otomatik internet Kontrol¸";
    private NetworkChangeReceiver receiver;//Network dinleyen receiver objemizin referans˝

    @Override
    protected void onDestroy() { //Activity Kapatıldığı zaman receiver durduralacak.Uygulama arka plana alındığı zamanda receiver çalışmaya devam eder
        super.onDestroy();

        unregisterReceiver(receiver);//receiver durduruluyor

    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.framelayout);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver(MainActivity.this);
        registerReceiver(receiver, filter);

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_cevrimici) {

                    changeFragment = new ChangeFragment(MainActivity.this);
                    changeFragment.change(new FragmentCevrimici(), "cevrimiciFrag");

                } else if (tabId == R.id.tab_ortak) {
                    changeFragment = new ChangeFragment(MainActivity.this);
                    changeFragment.change(new FragmentOrtakListe(), "ortakFrag");

                } else if (tabId == R.id.tab_profil) {

                    changeFragment = new ChangeFragment(MainActivity.this);
                    changeFragment.change(new FragmentProfil(), "profilFrag");
                }
            }
        });

        bottomBar.setDefaultTab(state);
        bottomBar.setSaveEnabled(true);
        defineFirebase();
        getUsersValue();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }


    public void defineFirebase() {


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        if (firebaseUser.getUid().toString() != null) {

            kuid = firebaseUser.getUid().toString();

        }
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public void getUsersValue() {

        reference = firebaseDatabase.getReference().child("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mail = dataSnapshot.child("email").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

