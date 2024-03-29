package com.alisverisim.yek.listin.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.alisverisim.yek.listin.Activitys.LoginActivity;
import com.alisverisim.yek.listin.Interfaces.IOnBackPressed;
import com.alisverisim.yek.listin.R;
import com.alisverisim.yek.listin.Utils.randomStringGenerator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gregacucnik.EditTextView;
import com.pd.chocobar.ChocoBar;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class FragmentProfil extends Fragment implements IOnBackPressed {

    View view;
    Toolbar toolbar;
    EditTextView ad_soyad_edittext, dogum_gunu_edittext, email_edittext;
    TextView guncellebuton, cikisButton, kullanımButton;
    CircleImageView profilresmi;
    ImageView addresim;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    String imageUrl;
    FirebaseUser firebaseUser;
    SpotsDialog r1;
    DatabaseReference reference;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.profil_fragment, container, false);

        tanimla();
        getUsersValue();
        buttonAction();
        tabAction();


        return view;
    }


    public void tanimla() {

        addresim = view.findViewById(R.id.add_resim);
        profilresmi = view.findViewById(R.id.profile_image);
        // firebase tanımlamaları
        // galeri için storage işlemi

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        String imageUrl, ortakListeler;

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("Users").child(firebaseUser.getUid()); // users childinin altındaki userslara eriştik.


        dogum_gunu_edittext = view.findViewById(R.id.dogum_gunu_edittext);


        email_edittext = view.findViewById(R.id.email_edittext);
        ad_soyad_edittext = view.findViewById(R.id.ad_soyad_edittext);
        guncellebuton = view.findViewById(R.id.guncellebuton);
        cikisButton = view.findViewById(R.id.cikisButton);
        kullanımButton = view.findViewById(R.id.kullanim_gizlilik);


    }


    public void buttonAction() {


        cikisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                Intent ıntent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(ıntent);
                getActivity().finishAffinity();


            }
        });


        guncellebuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                updateUsersValue();
            }
        });


        profilresmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galeriAc();
            }
        });




            dogum_gunu_edittext.setEditTextViewListener(new EditTextView.EditTextViewListener() {
                @Override
                public void onEditTextViewEditModeStart() {

                    Calendar mcurrentTime = Calendar.getInstance();
                    int year = mcurrentTime.get(Calendar.YEAR);//Güncel Yılı alıyoruz
                    int month = mcurrentTime.get(Calendar.MONTH);//Güncel Ayı alıyoruz
                    int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);//Güncel Günü alıyoruz

                    DatePickerDialog datePicker;//Datepicker objemiz
                    datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            // TODO Auto-generated method stub
                            dogum_gunu_edittext.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);//Ayarla butonu tıklandığında textview'a yazdırıyoruz

                        }
                    }, year, month, day);//başlarken set edilcek değerlerimizi atıyoruz
                    datePicker.setTitle("Tarih Seçiniz");
                    datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", datePicker);
                    datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePicker);

                    datePicker.show();

                }

                @Override
                public void onEditTextViewEditModeFinish(String text) {




                }
            });



        kullanımButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String url = "https://www.yunusemrekaplan.com/yunus-emre-kaplan-mobil-android-uygulamalari-gizlilik-politikasi/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }

    public void updateUsersValue() {


        final String isim = ad_soyad_edittext.getText().toString();
        final String dogumtarihi = dogum_gunu_edittext.getText().toString();


        if (isim.equals("") && dogumtarihi.equals("")) {


            ChocoBar.builder().setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                    .setTextSize(16)
                    .setTextColor(Color.parseColor("#FFFFFF"))
                    .setTextTypefaceStyle(Typeface.NORMAL)
                    .setText("Lütfen boş alan bırakmayınız.")
                    .setMaxLines(2)
                    .centerText()
                    .setIcon(R.drawable.ic_snackbar)
                    .setActivity(getActivity())
                    .setDuration(ChocoBar.LENGTH_SHORT)
                    .build()
                    .show();



        } else if (!isim.equals("") && !dogumtarihi.equals("")) {
            r1 = new SpotsDialog(getActivity(), R.style.Custom);
            r1.show();


            new Thread(new Runnable() {
                public void run() {

                    Map map = new HashMap();
                    map.put("email", email_edittext.getText().toString());
                    map.put("isim", isim);
                    map.put("dogumtarihi", dogumtarihi);


                    reference.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {


                            ad_soyad_edittext.setText(isim);
                            dogum_gunu_edittext.setText(dogumtarihi);

                            ChocoBar.builder().setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                                    .setTextSize(16)
                                    .setTextColor(Color.parseColor("#FFFFFF"))
                                    .setTextTypefaceStyle(Typeface.NORMAL)
                                    .setText("Bilgileriniz güncellendi.")
                                    .setMaxLines(2)
                                    .centerText()
                                    .setIcon(R.drawable.ic_snackbar)
                                    .setActivity(getActivity())
                                    .setDuration(ChocoBar.LENGTH_SHORT)
                                    .build()
                                    .show();

                            r1.dismiss();

                        }
                    });


                }
            }).start();

            Handler progressHandler = new Handler() {

                public void handleMessage(Message msg1) {

                    r1.dismiss();
                }
            };


        }

    }


    public void getUsersValue() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                ad_soyad_edittext.setText(dataSnapshot.child("isim").getValue().toString());
                email_edittext.setText(dataSnapshot.child("email").getValue().toString());
                dogum_gunu_edittext.setText(dataSnapshot.child("dogumtarihi").getValue().toString());

                if (!dataSnapshot.child("resim").getValue().toString().equals("")) {

                    Picasso.get()
                            .load(dataSnapshot.child("resim").getValue().toString())
                            .resize(140, 140)
                            .into(profilresmi);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public void galeriAc() {


        Intent ıntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(ıntent, 5); // seçtiği resmi göstermek için bunu kullanıyoruz.


    }


    // galeriden çekilen resmin pathini alıp firebase e upload eden method
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        File filepathh = null;
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == 5) {


            Uri selectedImage = data.getData();

            InputStream imageStream = null;
            try {
                imageStream = getActivity().getContentResolver().openInputStream(
                        selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Bitmap bmp = BitmapFactory.decodeStream(imageStream);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 20, stream);
            byte[] byteArray = stream.toByteArray();
            try {
                stream.close();
                stream = null;
            } catch (IOException e) {

                e.printStackTrace();
            }




            ChocoBar.builder().setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                    .setTextSize(16)
                    .setTextColor(Color.parseColor("#FFFFFF"))
                    .setTextTypefaceStyle(Typeface.NORMAL)
                    .setText("Profil fotoğrafı güncelleniyor.")
                    .setMaxLines(2)
                    .centerText()
                    .setIcon(R.drawable.ic_snackbar)
                    .setActivity(getActivity())
                    .setDuration(ChocoBar.LENGTH_SHORT)
                    .build()
                    .show();



            final StorageReference ref = storageReference.child("kullaniciresimleri").child(randomStringGenerator.generateString() + ".jpg");
            ref.putBytes(byteArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageUrl = uri.toString();
                            String mail = email_edittext.getText().toString();
                            String kadi = ad_soyad_edittext.getText().toString();
                            String dogumTarih = dogum_gunu_edittext.getText().toString();
                            reference = firebaseDatabase.getReference().child("Users").child(firebaseUser.getUid());
                            Map map = new HashMap();
                            map.put("isim", kadi);
                            map.put("email", mail);
                            map.put("dogumtarihi", dogumTarih);

                            if (imageUrl == null) {

                                imageUrl = "";
                            }

                            map.put("resim", imageUrl.toString());
                            reference.updateChildren(map);

                        }
                    });
                }
            });

        }

    }


    @Override
    public boolean onBackPressed() {
        System.exit(0);
        return false;
    }


    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


    private void tabAction() {


        toolbar = view.findViewById(R.id.profiltoolbar);

        toolbar.setTitleTextAppearance(getContext(), R.style.ToolbarTitleTextApperance);
        toolbar.setSubtitleTextAppearance(getContext(), R.style.ToolbarSubTitleTextApperance);

        toolbar.setTitle("Listin");
        toolbar.setSubtitle("Profilim");
        toolbar.inflateMenu(R.menu.cevrimici_toolbar);


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.menu_item_share) {

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBodyText = "Yeni bir alışveriş uygulaması buldum. Bana katıl ve ortak listeler oluşturalım!\n" +
                            "İşte linki: https://play.google.com/store/apps/details?id=com.alisverisim.yek.alversim";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Listin & Çevrimiçi listeler");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                    startActivity(Intent.createChooser(sharingIntent, "Uygulamayı arkadaşların ile paylaş"));
                }

                return false;
            }
        });


    }


}
