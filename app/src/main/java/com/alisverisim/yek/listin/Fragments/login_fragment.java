package com.alisverisim.yek.listin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alisverisim.yek.listin.Activitys.LoginActivity;
import com.alisverisim.yek.listin.Activitys.MainActivity;
import com.alisverisim.yek.listin.R;
import com.alisverisim.yek.listin.Utils.ChangeFragment;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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

import static com.alisverisim.yek.listin.Utils.progressClass.profilProgress;


public class login_fragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 67;
    MaterialButton googleWithLogin, mailWithLogin;
    TextView kayitTextview;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    View view;
    ImageView back_pressed;
    DatabaseReference databaseReference, databaseReference2;
    FirebaseDatabase firebaseDatabase, firebaseDatabase2;
    FirebaseAuth mAuth;
    TextView register_text;
    private DatabaseReference userref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_login_fragment, container, false);


        tanimla();

        action();

        return view;


    }


    private void tanimla() {
        back_pressed = view.findViewById(R.id.back_pressed);
        mAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        googleWithLogin = view.findViewById(R.id.google_with_login);
        mailWithLogin = view.findViewById(R.id.mail_with_login);
        register_text = view.findViewById(R.id.register_text);

    }

    private void action() {


        mailWithLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChangeFragment changeFragment = new ChangeFragment(getContext());
                changeFragment.change(new FragmentGirisYap(), "fragGirisYap");

            }
        });
        googleWithLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();
                googleWithLogin.setClickable(false);


            }
        });

        register_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChangeFragment changeFragment = new ChangeFragment(getContext());
                changeFragment.change(new FragmentKayit(), "fragKayit");

            }
        });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn() {

        profilProgress = new SpotsDialog(getActivity(), R.style.Custom);
        profilProgress.show();
        new Thread(new Runnable() {
            public void run() {


                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(LoginActivity.mGoogleApiClient);
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
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                Log.e("Google Login", "Oturum Açılıyor..");
                GoogleSignInAccount account = result.getSignInAccount();
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
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


                                                                Log.i("googleloginn", "başarılı");
                                                                // giriş yapıldı

                                                                profilProgress.dismiss();
                                                            }
                                                        }
                                                    });


                                            if (dataSnapshot.exists()) {

                                                firebaseDatabase = FirebaseDatabase.getInstance();
                                                firebaseUser = firebaseAuth.getCurrentUser();
                                                Intent ıntent = new Intent(getContext(), MainActivity.class);
                                                startActivity(ıntent);
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

                                                databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid()).child("lists");

                                                Map map2 = new HashMap();

                                                map2.put("durum",true);

                                                databaseReference.setValue(map2);


                                                Intent ıntent = new Intent(getContext(), MainActivity.class);
                                                startActivity(ıntent);
                                                profilProgress.dismiss();

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                } else {
                                    Log.e("Google Login", "Oturum Açılamadı.", task.getException());
                                    Toast.makeText(getContext(), "Giriş hatası, lütfen bize bildirin..",
                                            Toast.LENGTH_SHORT).show();
                                    googleWithLogin.setClickable(true);
                                    profilProgress.dismiss();

                                }
                            }
                        });
            } else {
                Log.e("Google Login", "Google hesabıyla oturum açma isteği yapılamadı.");
                googleWithLogin.setClickable(true);
                profilProgress.dismiss();


            }
        }
    }
}
