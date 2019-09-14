package com.alisverisim.yek.listin.Activitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alisverisim.yek.listin.AlertDialogs.sifrenimiunuttunalert;
import com.alisverisim.yek.listin.R;
import com.alisverisim.yek.listin.Utils.progressClass;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    EditText mail, password;
    Button girisyapButton, googleilegirisyap;
    TextView yeniHesap, sifreunuttumtextview;
    Typeface tf1;
    SpotsDialog profilProgress;

    private FirebaseAuth mAuth;
    DatabaseReference databaseReference, databaseReference2;
    FirebaseDatabase firebaseDatabase, firebaseDatabase2;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 67;
    private DatabaseReference userref;
    TextView textView;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        define();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


    }

    public void define() {

        googleilegirisyap = findViewById(R.id.googlebutton);
        mail = findViewById(R.id.email_edittext);
        password = findViewById(R.id.password_edittext);
        girisyapButton = findViewById(R.id.LoginGirisYapButton);
        yeniHesap = findViewById(R.id.yenihesaptextview);
        textView = findViewById(R.id.logintext);
        sifreunuttumtextview = findViewById(R.id.sifresıfırla);
        tf1 = Typeface.createFromAsset(getAssets(), "fonts/yekfont.ttf");
        textView.setTypeface(tf1);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        yeniHesap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressClass.progressAktif(LoginActivity.this);
                Intent ıntent = new Intent(LoginActivity.this, KayitActivity.class);
                startActivity(ıntent);

            }
        });


        girisyapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String kullaniciMail, kullanıcıPassword;
                kullaniciMail = mail.getText().toString();
                kullanıcıPassword = password.getText().toString();

                if (isValidEmail(kullaniciMail)) {

                    if (kullanıcıPassword.length() < 8 || kullanıcıPassword.length() > 12 || kullanıcıPassword.equals("")) {

                        Toast.makeText(getApplicationContext(), "Şifren 8'den küçük 12'den büyük olmamalı.", Toast.LENGTH_SHORT).show();

                    } else {


                        profilProgress = new SpotsDialog(LoginActivity.this, R.style.Custom);
                        profilProgress.show();
                        new Thread(new Runnable() {
                            public void run() {


                                login(kullaniciMail, kullanıcıPassword);

                            }
                        }).start();

                        Handler progressHandler = new Handler() {

                            public void handleMessage(Message msg1) {

                                profilProgress.dismiss();
                            }
                        };


                    }

                } else {

                    Toast.makeText(getApplicationContext(), "Geçerli bir email adresi girmelisin.", Toast.LENGTH_SHORT).show();
                }


            }
        });


        googleilegirisyap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                signIn(); // Login işlemi
                googleilegirisyap.setClickable(false);

            }
        });

        sifreunuttumtextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sifrenimiunuttunalert sifrenimiunuttunalert = new sifrenimiunuttunalert(LoginActivity.this);
                sifrenimiunuttunalert.ac();


            }
        });
    }


    public void login(final String gmail, final String password) {
        firebaseAuth.signInWithEmailAndPassword(gmail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            userref = FirebaseDatabase.getInstance().getReference("/Users");
                            String currentUser = mAuth.getCurrentUser().getUid();
                            String deviceToken = FirebaseInstanceId.getInstance().getToken();
                            userref.child(currentUser).child("device_token")
                                    .setValue(deviceToken)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {


                                            if (task.isSuccessful()) {

                                                Log.i("success", "device token send to firebase is succesfull");
                                                Intent ıntent = new Intent(LoginActivity.this, MainActivity.class);
                                                startActivity(ıntent);
                                                finish();
                                                profilProgress.dismiss();
                                            }
                                        }
                                    });


                        } else if (task.getException().getMessage().equals("The password is invalid or the user does not have a password.")) {

                            Toast.makeText(getApplicationContext(), "Şifreniz hatalı!", Toast.LENGTH_SHORT).show();
                            profilProgress.dismiss();

                        } else if (task.getException().getMessage().equals("There is no user record corresponding to this identifier. The user may have been deleted.")) {

                            Toast.makeText(getApplicationContext(), "Böyle bir kullanıcı bulunamadı..", Toast.LENGTH_SHORT).show();
                            profilProgress.dismiss();

                        } else {
                            Toast.makeText(getApplicationContext(), "Bu cihaz desteklenmiyor.", Toast.LENGTH_SHORT).show();
                            profilProgress.dismiss();

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

    private void signIn() {

        profilProgress = new SpotsDialog(LoginActivity.this, R.style.Custom);
        profilProgress.show();
        new Thread(new Runnable() {
            public void run() {


                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        }).start();

        Handler progressHandler = new Handler() {

            public void handleMessage(Message msg1) {

                profilProgress.dismiss();
            }
        };


    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                Log.e("Google Login", "Oturum Açılıyor..");
                GoogleSignInAccount account = result.getSignInAccount();
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    firebaseDatabase2 = FirebaseDatabase.getInstance();
                                    databaseReference = firebaseDatabase2.getReference("Users").child(firebaseAuth.getUid());
                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                            userref = FirebaseDatabase.getInstance().getReference("/Users");
                                            String currentUser = mAuth.getCurrentUser().getUid();
                                            String deviceToken = FirebaseInstanceId.getInstance().getToken();
                                            userref.child(currentUser).child("device_token")
                                                    .setValue(deviceToken)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {


                                                            if (task.isSuccessful()) {

                                                                Log.i("success", "device token send to firebase is succesfull");
                                                                Intent ıntent = new Intent(LoginActivity.this, MainActivity.class);
                                                                startActivity(ıntent);
                                                                finish();
                                                                profilProgress.dismiss();
                                                            }
                                                        }
                                                    });


                                            if (dataSnapshot.exists()) {

                                                firebaseDatabase = FirebaseDatabase.getInstance();
                                                firebaseUser = firebaseAuth.getCurrentUser();
                                                Intent ıntent = new Intent(getApplicationContext(), MainActivity.class);
                                                startActivity(ıntent);
                                                finish();
                                                profilProgress.dismiss();


                                            } else {


                                                firebaseDatabase = FirebaseDatabase.getInstance();
                                                firebaseUser = firebaseAuth.getCurrentUser();
                                                databaseReference2 = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid());

                                                Map map = new HashMap();
                                                map.put("isim", "");
                                                map.put("email", firebaseUser.getEmail());
                                                map.put("resim", "");
                                                map.put("ortakListeler", "");
                                                map.put("dogumtarihi", "");
                                                databaseReference.setValue(map);
                                                Intent ıntent = new Intent(getApplicationContext(), MainActivity.class);
                                                startActivity(ıntent);
                                                finish();
                                                profilProgress.dismiss();

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                } else {
                                    Log.e("Google Login", "Oturum Açılamadı.", task.getException());
                                    Toast.makeText(LoginActivity.this, "Giriş hatası, lütfen bize bildirin..",
                                            Toast.LENGTH_SHORT).show();
                                    googleilegirisyap.setClickable(true);
                                    profilProgress.dismiss();

                                }
                            }
                        });
            } else {
                Log.e("Google Login", "Google hesabıyla oturum açma isteği yapılamadı.");
                googleilegirisyap.setClickable(true);
                profilProgress.dismiss();


            }
        }
    }


}


