package com.alisverisim.yek.listin.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.alisverisim.yek.listin.Activitys.MainActivity;
import com.alisverisim.yek.listin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.pd.chocobar.ChocoBar;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;


public class FragmentKayit extends Fragment {

    SpotsDialog profilProgress;

    TextInputLayout isim_layout, pass_layout, dogum_layout, email_layout;
    EditText isim_edittext, pass_edittext, dogum_edittext, email_edittext;
    MaterialButton girisYapButton;
    ImageView backPressed;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, rootRef;

    View view;

    private static boolean ValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_fragment_kayit, container, false);


        tanimla();
        action();

        return view;
    }

    private void submitForm() {

        Log.i("buttonevent","submitformçalıştı");

        if (validateEmail() && validatePassword() && valideteName()) {

            final String email = email_edittext.getText().toString().trim();
            final String pass = pass_edittext.getText().toString().trim();
            final String isimSoyisim = isim_edittext.getText().toString().trim();
            profilProgress = new SpotsDialog(getContext(), R.style.Custom);
            profilProgress.show();
            new Thread(new Runnable() {
                public void run() {

                            kayit(email,pass,isimSoyisim);
                    //

                }
            }).start();

            Handler progressHandler = new Handler() {

                public void handleMessage(Message msg1) {

                    profilProgress.dismiss();
                }
            };


        }

    }

    private void action() {


        girisYapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                submitForm();
            }
        });


        backPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getActivity().onBackPressed();
            }
        });

    }

    private void tanimla() {

        isim_layout = view.findViewById(R.id.input_layout_isimsoyisim);
        isim_edittext = view.findViewById(R.id.input_isimsoyisim);
        firebaseAuth = FirebaseAuth.getInstance();
        pass_layout = view.findViewById(R.id.input_layout_pass);
        pass_edittext = view.findViewById(R.id.input_pass);



        email_edittext = view.findViewById(R.id.input_emailadresi);

        email_layout = view.findViewById(R.id.input_layout_emailadresi);

        girisYapButton = view.findViewById(R.id.kayitol_buttonn);
        backPressed = view.findViewById(R.id.kayit_backpressed);
        email_edittext.addTextChangedListener(new MyTextWatcher(email_edittext));
        isim_edittext.addTextChangedListener(new MyTextWatcher(isim_edittext));
        pass_edittext.addTextChangedListener(new MyTextWatcher(pass_edittext));

    }

    private boolean validateEmail() {

        if (!ValidEmail(email_edittext.getText().toString().trim()) && !email_edittext.getText().toString().trim().isEmpty()) {

            email_layout.setErrorEnabled(false);

            email_edittext.requestFocus();
            girisYapButton.setClickable(false);

            return false;
        } else if (email_edittext.getText().toString().trim().isEmpty()) {

            email_layout.setError("Email adresiniz boş");
            email_edittext.requestFocus();
            girisYapButton.setClickable(false);
            return false;

        } else {
            email_layout.setErrorEnabled(false);
            girisYapButton.setClickable(true);

        }

        girisYapButton.setClickable(true);
        return true;
    }

    private boolean valideteName() {

        if (isim_edittext.getText().toString().trim().isEmpty()) {
            isim_layout.setError("İsim ve soyisim boş olamaz.");
            isim_edittext.requestFocus();
            girisYapButton.setClickable(false);
            return false;
        } else {

            isim_layout.setErrorEnabled(false);
            girisYapButton.setClickable(true);

        }


        return true;
    }

    private boolean validatePassword() {
        if (pass_edittext.getText().toString().trim().isEmpty()) {
            pass_layout.setError("Şifreniz boş olamaz");
            pass_edittext.requestFocus();
            girisYapButton.setClickable(false);

            return false;
        } else if (pass_edittext.getText().toString().trim().length() < 8 || pass_edittext.getText().toString().trim().length() > 16) {
            pass_layout.setError("Şifreniz 8-16 karakter olmalı.");
            pass_edittext.requestFocus();
            girisYapButton.setClickable(false);

            return false;

        } else {
            pass_layout.setErrorEnabled(false);
            girisYapButton.setClickable(true);

        }

        girisYapButton.setClickable(true);

        return true;
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_emailadresi:
                    validateEmail();
                    break;
                case R.id.input_pass:
                    validatePassword();
                    break;
                case R.id.input_isimsoyisim:
                    valideteName();
                    break;
            }
        }
    }



    public void kayit(String email, String password, final String isim) {
        Log.i("buttonevent","submitformçalıştı");

        rootRef = FirebaseDatabase.getInstance().getReference();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {


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



                    databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid()).child("lists");

                    Map map2 = new HashMap();

                    map2.put("durum",true);

                    databaseReference.setValue(map2);

                    Intent ıntent = new Intent(getActivity(), MainActivity.class);
                    ıntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);


                    // Token işlemi başlangıc

                    String deviceToken = FirebaseInstanceId.getInstance().getToken();
                    String currentuserid = firebaseUser.getUid();
                    rootRef.child("Users").child(currentuserid).child("device_token")
                            .setValue(deviceToken);

                    profilProgress.dismiss();
                    startActivity(ıntent);
                    getActivity().finish();


                } else {



                    ChocoBar.builder().setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                            .setTextSize(16)
                            .setTextColor(Color.parseColor("#FFFFFF"))
                            .setTextTypefaceStyle(Typeface.NORMAL)
                            .setText("Böyle bir kullanıcı mevcut.")
                            .setMaxLines(2)
                            .centerText()
                            .setIcon(R.drawable.ic_snackbar)
                            .setActivity(getActivity())
                            .setDuration(ChocoBar.LENGTH_SHORT)
                            .build()
                            .show();

                    profilProgress.dismiss();
                }

            }
        });


    }
}
