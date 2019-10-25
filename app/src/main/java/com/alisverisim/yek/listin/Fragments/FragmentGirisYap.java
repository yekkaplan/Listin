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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alisverisim.yek.listin.Activitys.MainActivity;
import com.alisverisim.yek.listin.AlertDialogs.sifrenimiunuttunalert;
import com.alisverisim.yek.listin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.pd.chocobar.ChocoBar;

import dmax.dialog.SpotsDialog;

public class FragmentGirisYap extends Fragment {
    View view;
    SpotsDialog profilProgress;
    ImageView back_pressed;
    FirebaseAuth mAuth;
    FirebaseAuth firebaseAuth;
    DatabaseReference userref;
    private EditText inputEmail, inputPassword;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword;
    private MaterialButton btnSignUp;
    TextView sifremiunuttum;

    private static boolean ValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_fragment_giris_yap, container, false);


        tanimla();

        action();

        return view;

    }

    private void tanimla() {

        back_pressed = view.findViewById(R.id.back_pressed);
        inputEmail = view.findViewById(R.id.input_email);
        inputLayoutEmail = view.findViewById(R.id.input_layout_email);
        inputPassword = view.findViewById(R.id.input_pass);
        inputLayoutPassword = view.findViewById(R.id.input_layout_pass);
        btnSignUp = view.findViewById(R.id.login_button);
        firebaseAuth = FirebaseAuth.getInstance();
        sifremiunuttum = view.findViewById(R.id.sifrenimi_unuttun);
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));


    }


    private void action() {

        sifremiunuttum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                sifrenimiunuttunalert sifrenimiunutunalert = new sifrenimiunuttunalert(getActivity());

                sifrenimiunutunalert.ac();


            }
        });

        back_pressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getActivity().onBackPressed();


            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitForm();

            }
        });


    }

    private void submitForm() {

        if (validateEmail() && validatePassword()) {

            final String email = inputEmail.getText().toString().trim();
            final String pass = inputPassword.getText().toString().trim();

            profilProgress = new SpotsDialog(getContext(), R.style.Custom);
            profilProgress.show();
            new Thread(new Runnable() {
                public void run() {


                    login(email, pass);

                }
            }).start();

            Handler progressHandler = new Handler() {

                public void handleMessage(Message msg1) {

                    profilProgress.dismiss();
                }
            };


        }

    }


    private boolean validateEmail() {

        if (!ValidEmail(inputEmail.getText().toString().trim()) && !inputEmail.getText().toString().trim().isEmpty()) {

            inputLayoutEmail.setErrorEnabled(false);

            inputEmail.requestFocus();
            btnSignUp.setClickable(false);

            return false;
        } else if (inputEmail.getText().toString().trim().isEmpty()) {

            inputLayoutEmail.setError("Email adresiniz boş");
            inputEmail.requestFocus();
            btnSignUp.setClickable(false);
            return false;

        } else {
            inputLayoutEmail.setErrorEnabled(false);
            btnSignUp.setClickable(true);

        }

        btnSignUp.setClickable(true);
        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError("Şifreniz boş olamaz");
            inputPassword.requestFocus();
            btnSignUp.setClickable(false);

            return false;
        } else if (inputPassword.getText().toString().trim().length() < 8 || inputPassword.getText().toString().trim().length() > 16) {
            inputLayoutPassword.setError("Şifreniz 8-16 karakter olmalı.");
            inputPassword.requestFocus();
            btnSignUp.setClickable(false);

            return false;

        } else {
            inputLayoutPassword.setErrorEnabled(false);
            btnSignUp.setClickable(true);

        }

        btnSignUp.setClickable(true);

        return true;
    }

    public void login(final String gmail, final String password) {
        firebaseAuth.signInWithEmailAndPassword(gmail, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mAuth = FirebaseAuth.getInstance();
                            userref = FirebaseDatabase.getInstance().getReference("/Users");
                            String currentUser = mAuth.getCurrentUser().getUid();
                            String deviceToken = FirebaseInstanceId.getInstance().getToken();
                            userref.child(currentUser).child("device_token")
                                    .setValue(deviceToken)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {


                                            if (task.isSuccessful()) {

                                                profilProgress.dismiss();
                                                Intent myIntent = new Intent(getActivity(), MainActivity.class);
                                                getActivity().startActivity(myIntent);
                                                getActivity().finish();
                                            }
                                        }
                                    });


                        } else if (task.getException().getMessage().equals("The password is invalid or the user does not have a password.")) {
                            profilProgress.dismiss();
                            ChocoBar.builder().setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                                    .setTextSize(16)
                                    .setTextColor(Color.parseColor("#FFFFFF"))
                                    .setTextTypefaceStyle(Typeface.NORMAL)
                                    .setText("Şifreniz hatalı")
                                    .setMaxLines(2)
                                    .centerText()
                                    .setIcon(R.drawable.ic_snackbar)
                                    .setActivity(getActivity())
                                    .setDuration(ChocoBar.LENGTH_SHORT)
                                    .build()
                                    .show();


                        } else if (task.getException().getMessage().equals("There is no user record corresponding to this identifier. The user may have been deleted.")) {
                            profilProgress.dismiss();

                            ChocoBar.builder().setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                                    .setTextSize(16)
                                    .setTextColor(Color.parseColor("#FFFFFF"))
                                    .setTextTypefaceStyle(Typeface.NORMAL)
                                    .setText("Böyle bir kullanıcı bulunamadı")
                                    .setMaxLines(2)
                                    .centerText()
                                    .setIcon(R.drawable.ic_snackbar)
                                    .setActivity(getActivity())
                                    .setDuration(ChocoBar.LENGTH_SHORT)
                                    .build()
                                    .show();


                        } else {

                            profilProgress.dismiss();

                            Toast.makeText(getContext(), "Bu cihaz desteklenmiyor.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
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
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_pass:
                    validatePassword();
                    break;
            }
        }
    }
}
