package com.alisverisim.yek.listin.Activitys;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.alisverisim.yek.listin.R;
import com.alisverisim.yek.listin.Utils.NetworkChangeReceiver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    TextView textView;
    Typeface tf1;

    private static final String LOG_TAG = "Otomatik internet Kontrol¸";
    private NetworkChangeReceiver receiver;//Network dinleyen receiver objemizin referans˝

    @Override
    protected void onDestroy() { //Activity Kapatıldığı zaman receiver durduralacak.Uygulama arka plana alındığı zamanda receiver çalışmaya devam eder
        super.onDestroy();

        unregisterReceiver(receiver);//receiver durduruluyor

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        tanimla();



        tf1 = Typeface.createFromAsset(getAssets(), "fonts/yekfont.ttf");
        textView.setTypeface(tf1);


        //Receiverımızı register ediyoruz
        //Yani Çalıştırıyoruz
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver(SplashScreen.this);
        registerReceiver(receiver, filter);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        final Thread timerThread = new Thread() {
            public void run() {

                try {
                    sleep(2000);


                    if (firebaseUser == null) {


                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();

                    } else if (firebaseUser != null) {


                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();


                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        timerThread.start();


    }


    public void tanimla() {

        textView = findViewById(R.id.splashscreentext);
    }


}



