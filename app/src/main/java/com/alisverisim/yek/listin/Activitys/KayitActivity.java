package com.alisverisim.yek.listin.Activitys;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;


import android.support.v7.app.AppCompatActivity;


import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alisverisim.yek.listin.R;
import com.alisverisim.yek.listin.Utils.progressClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;


public class KayitActivity extends AppCompatActivity {
    EditText isim_Edittext, yenimail_Edittext, yenipassword_edittext;
    Button kayitOlbutton;
    TextView kayitbasliktext;
    Typeface typeface;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);
        tanimla();
    }

    @Override
    protected void onStart() {
        super.onStart();

        progressClass.progressPasif();
    }

    public void tanimla() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        typeface = Typeface.createFromAsset(getAssets(), "fonts/yekfont.ttf");

        isim_Edittext = findViewById(R.id.isim_Edittext);
        yenimail_Edittext = findViewById(R.id.yenimail_Edittext);
        yenipassword_edittext = findViewById(R.id.yenipassword_edittext);
        kayitOlbutton = findViewById(R.id.KayitOlButton);
        kayitbasliktext = findViewById(R.id.kayittext);


        kayitOlbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressClass.progressAktif(KayitActivity.this);
                String isim, mail, password;
                isim = isim_Edittext.getText().toString();
                mail = yenimail_Edittext.getText().toString();
                password = yenipassword_edittext.getText().toString();


                if (isValidEmail(mail)) {

                    if (password.length() < 8 || password.length() > 12 || password.equals("")) {

                        Toast.makeText(getApplicationContext(), "Şifren 8 karakterden küçük 12 karakterden büyük olmamalı. :)", Toast.LENGTH_SHORT).show();
                        progressClass.progressPasif();
                    } else if (isim.length() == 0) {

                        Toast.makeText(getApplicationContext(), "Lütfen isminiz boş bırakmayınız.", Toast.LENGTH_SHORT).show();
                        progressClass.progressPasif();
                    } else {

                        kayit(mail, password, isim);


                    }

                } else {

                    Toast.makeText(getApplicationContext(), "Geçerli bir email adresi girmelisin. :)", Toast.LENGTH_SHORT).show();
                    progressClass.progressPasif();
                }


            }
        });

    }

    public void kayit(String email, String password, final String isim) {

        rootRef = firebaseDatabase.getReference();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {


                    progressClass.progressAktif(KayitActivity.this);
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    firebaseUser = firebaseAuth.getCurrentUser();
                    databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid());


                    Map map = new HashMap();
                    map.put("isim", isim);
                    map.put("email", firebaseUser.getEmail());
                    map.put("resim", "");
                    map.put("ortakListeler", "");
                    map.put("dogumtarihi", "");
                    databaseReference.setValue(map);
                    Intent ıntent = new Intent(KayitActivity.this, MainActivity.class);
                    ıntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);


                    // Token işlemi başlangıc

                    String deviceToken = FirebaseInstanceId.getInstance().getToken();
                    String currentuserid = firebaseUser.getUid();
                    rootRef.child("Users").child(currentuserid).child("device_token")
                            .setValue(deviceToken);


                    startActivity(ıntent);
                    progressClass.progressPasif();
                    finish();


                } else {

                    Toast.makeText(getApplicationContext(), "Böyle bir kullanıcı mevcut.", Toast.LENGTH_SHORT).show();
                    progressClass.progressPasif();
                }

            }
        });


    }

    // mail kontroller
    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


}
