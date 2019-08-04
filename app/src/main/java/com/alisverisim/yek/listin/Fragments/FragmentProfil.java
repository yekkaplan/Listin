package com.alisverisim.yek.listin.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alisverisim.yek.listin.Activitys.LoginActivity;
import com.alisverisim.yek.listin.Interfaces.IOnBackPressed;
import com.alisverisim.yek.listin.Listeners.anamenupopuplistener;
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
import com.iceteck.silicompressorr.SiliCompressor;
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
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class FragmentProfil extends Fragment implements IOnBackPressed {

    View view;

    ExtendedEditText mailAdresi, kAdi, dogumExtendted;
    Button guncellebuton, cikisButton;
    CircleImageView profilresmi;
    ImageView paylasButton, popupButton;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    String imageUrl;
    FirebaseUser firebaseUser;
    SpotsDialog r1;
    DatabaseReference reference;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    TextView profiltextbaslik;
    TextFieldBoxes emailTextFieldBoxes, kadiEmailTextFieldBoxes, dogumTextfieldboxes;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.profil_fragment, container, false);
        tanimla();
        getUsersValue();
        buttonAction();
        barAction();
        return view;
    }

    public void tanimla() {

        profiltextbaslik = view.findViewById(R.id.kisiselbilgilertextview);

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


        dogumTextfieldboxes = view.findViewById(R.id.dogumGunuTextfield);
        dogumExtendted = view.findViewById(R.id.dogumGunuedittext);
        mailAdresi = view.findViewById(R.id.emailProfileEdittext);
        kAdi = view.findViewById(R.id.ProfilAdınveSoyadınEdit);
        guncellebuton = view.findViewById(R.id.profilBilgiGuncelle);
        cikisButton = view.findViewById(R.id.cikisyapButton);
        emailTextFieldBoxes = view.findViewById(R.id.emailProfileTextfield);
        kadiEmailTextFieldBoxes = view.findViewById(R.id.profilAdınveSoyadınTextfield);


        paylasButton = view.findViewById(R.id.profilepaylas);

        popupButton = view.findViewById(R.id.profilepopup);
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


        dogumTextfieldboxes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // TODO Auto-generated method stub
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
                        dogumExtendted.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);//Ayarla butonu tıklandığında textview'a yazdırıyoruz

                    }
                }, year, month, day);//başlarken set edilcek değerlerimizi atıyoruz
                datePicker.setTitle("Tarih Seçiniz");
                datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", datePicker);
                datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePicker);

                datePicker.show();

            }
        });


        dogumExtendted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
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
                        dogumExtendted.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);//Ayarla butonu tıklandığında textview'a yazdırıyoruz

                    }
                }, year, month, day);//başlarken set edilcek değerlerimizi atıyoruz
                datePicker.setTitle("Tarih Seçiniz");
                datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", datePicker);
                datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePicker);

                datePicker.show();

            }
        });

    }

    public void updateUsersValue() {


        final String isim = kAdi.getText().toString();
        final String dogumtarihi = dogumExtendted.getText().toString();


        if (isim.equals("") && dogumtarihi.equals("")) {


            Toast.makeText(getContext(), "Lütfen boş alan bırakmayınız.", Toast.LENGTH_SHORT).show();
        } else if (!isim.equals("") && !dogumtarihi.equals("")) {
            r1 = new SpotsDialog(getActivity(), R.style.Custom);
            r1.show();


            new Thread(new Runnable() {
                public void run() {

                    Map map = new HashMap();
                    map.put("email", mailAdresi.getText().toString());
                    map.put("isim", isim);
                    map.put("dogumtarihi", dogumtarihi);


                    reference.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {


                            kAdi.setText(isim);
                            dogumExtendted.setText(dogumtarihi);
                            Toast.makeText(getContext(), "Bilgileriniz Güncellendi.", Toast.LENGTH_SHORT).show();
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

                kAdi.setText(dataSnapshot.child("isim").getValue().toString());
                mailAdresi.setText(dataSnapshot.child("email").getValue().toString());
                dogumExtendted.setText(dataSnapshot.child("dogumtarihi").getValue().toString());

                if (!dataSnapshot.child("resim").getValue().toString().equals("")) {

                    Picasso.get()
                            .load(dataSnapshot.child("resim").getValue().toString())
                            .resize(150, 150)
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




            Toast.makeText(getContext(), "Resim Yükleniyor..", Toast.LENGTH_LONG).show();

            final StorageReference ref = storageReference.child("kullaniciresimleri").child(randomStringGenerator.generateString() + ".jpg");
            ref.putBytes(byteArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageUrl = uri.toString();
                            String mail = mailAdresi.getText().toString();
                            String kadi = kAdi.getText().toString();
                            String dogumTarih = dogumExtendted.getText().toString();
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


    public void barAction() {

        paylasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "Yeni bir alışveriş uygulaması buldum. Bana katıl ve ortak listeler oluşturalım!\n" +
                        "İşte linki: https://play.google.com/store/apps/details?id=com.alisverisim.yek.alversim";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Listin & Çevrimiçi listeler");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Uygulamayı arkadaşların ile paylaş"));

            }
        });


        popupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PopupMenu popup = new PopupMenu(getContext(), v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.anamenu_popup, popup.getMenu());
                popup.setOnMenuItemClickListener(new anamenupopuplistener(getContext()));
                popup.show();

            }
        });
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
}
