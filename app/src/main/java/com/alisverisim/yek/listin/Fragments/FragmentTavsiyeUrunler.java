package com.alisverisim.yek.listin.Fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alisverisim.yek.listin.R;
import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pd.chocobar.ChocoBar;

import net.igenius.customcheckbox.CustomCheckBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class FragmentTavsiyeUrunler extends Fragment {
    View view;
    Toolbar toolbar;
    ExpandingList expandingList;
    View subItem;
    String kuid;
    String gelenListeAdi;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    RelativeLayout anarelativ;
    ArrayList<String> urunModelsList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tavsiyeurunler, container, false);
        gelenListeAdi = getArguments().getString("listeadi");

        kuid = getArguments().getString("kuid");

        Toast.makeText(getContext(), gelenListeAdi, Toast.LENGTH_SHORT).show();
        tanimla();
        firebaseTanimlanmasi();
        toolbarAction();
        return view;

    }


    private void firebaseTanimlanmasi() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    private void tanimla() {
        anarelativ = view.findViewById(R.id.anarelativ);
        toolbar = view.findViewById(R.id.tavsiyeUrunlerToolbar);
        toolbar.setTitleTextAppearance(getContext(), R.style.ToolbarTitleTextApperance);
        toolbar.setSubtitleTextAppearance(getContext(), R.style.ToolbarSubTitleTextApperance);
        toolbar.setTitle("Listin");
        toolbar.setSubtitle("Almak isteyebilceğiniz ürünler");
        urunModelsList = new ArrayList<>(); // null hatası kaçışı


        List<String> gidaUrunler = Arrays.asList("Kırmızı et", "Balık", "Tavuk Eti", "Yumurta", "Yeşil Zeytin", "Peynir", "Tereyağı", "Margarin", "Toz Şeker", "Tuz", "Ayçiçek yağı",
                "Zeytin yağı", "Domates Salçası", "Biber Salçası", "Kaşar Peynir", "Cheddar peynir", "Mozerella peynir", "Turşu");

        nodeCreate(gidaUrunler, "Çeşitli Gıdalar", (gidaUrunler.size()), R.color.pink, R.drawable.ic_tavsiyebaslik);

        List<String> icecekUrunler = Arrays.asList("Kola", "Ayran", "Limonata", "Sade soda", "Meyveli Soda", "Portakal Suyu", "Şeftali Suyu", "Ananas Suyu", "Meyve Suyu");

        nodeCreate(icecekUrunler, "Çeşitli İçecekler", (icecekUrunler.size()), R.color.holo_red_light, R.drawable.ic_drink);

        List<String> meyveSebzeUrunler = Arrays.asList("Elma", "Armut", "Kiraz", "Muz", "Kivi", "Domates", "Salatalık", "Soğan", "Maydonoz", "Portakal", "Mandalina", "Limon", "Lahana", "Marul");

        nodeCreate(meyveSebzeUrunler, "Meyve & Sebze Çeşitleri", (meyveSebzeUrunler.size()), R.color.orange, R.drawable.fruit_cherries);


        List<String> bakliyatUrunleri = Arrays.asList("Pirinç", "Bulgur", "Un", "Tarçın", "Mercimek", "Fasulye", "Makarna", "Pul Biber", "Kekik", "Karabiber", "Pasta Kreması");

        nodeCreate(bakliyatUrunleri, "Bakliyat & Un", (bakliyatUrunleri.size()), R.color.turkuaz, R.drawable.ic_bakliyat);


        List<String> temizlikUrunleri = Arrays.asList("Bulaşık Deterjanı", "Sıvı Sabun", "Çamaşır Suyu", "Cam temizleyici", "Yağ çözücü", "Çamaşır Deterjanı");

        nodeCreate(temizlikUrunleri, "Ev Temizliği", (temizlikUrunleri.size()), R.color.purple, R.drawable.ic_cut);


        List<String> kisiselBakımUrunler = Arrays.asList("Deodorant", "Parfüm", "Saç Jölesi", "Krem", "Jilet", "Saç boyası", "Diş Macunu", "Diş Fırçası");

        nodeCreate(kisiselBakımUrunler, "Kişisel Bakım Ürünleri", (kisiselBakımUrunler.size()), R.color.orange, R.drawable.ic_evtemizligi);


    }


    private void nodeCreate(List<String> urunPortfoyu, String Baslik, int Count, int color, int drawable) {

        expandingList = view.findViewById(R.id.expanding_list_main);
        final ExpandingItem item = expandingList.createNewItem(R.layout.expanding_layout);

        ((TextView) item.findViewById(R.id.title)).setText(Baslik);
        item.createSubItems(Count);
        for (int i = 0; i < Count; i++) {


            subItem = item.getSubItemView(i);
            ((TextView) subItem.findViewById(R.id.sub_title)).setText(urunPortfoyu.get(i));


        }

        item.setIndicatorColorRes(color);
        item.setIndicatorIconRes(drawable);

        for (int x = 0; x <= item.getSubItemsCount() - 1; x++) {


            final CustomCheckBox scb = item.getSubItemView(x).findViewById(R.id.checksubitem);

            final int finalX = x;
            scb.setOnCheckedChangeListener(new CustomCheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CustomCheckBox checkBox, boolean isChecked) {

                    String urunAdi = ((TextView) item.getSubItemView(finalX).findViewById(R.id.sub_title)).getText().toString();

                    if (isChecked) {

                        databaseReference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid()).child("lists").child(gelenListeAdi).child("Urunler").child(urunAdi);
                        HashMap<String, Object> map2 = returnMap();
                        databaseReference.updateChildren(map2);

                        ChocoBar.builder().setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                                .setTextSize(16)
                                .setTextColor(Color.parseColor("#FFFFFF"))
                                .setTextTypefaceStyle(Typeface.BOLD)
                                .setText("Ürün listenize eklendi.")
                                .setMaxLines(2)
                                .centerText()
                                .setIcon(R.drawable.ic_snackbar)
                                .setActivity(getActivity())
                                .setDuration(ChocoBar.LENGTH_SHORT)
                                .build()
                                .show();


                    } else if (!isChecked) {


                        databaseReference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid()).child("lists").child(gelenListeAdi).child("Urunler").child(urunAdi);
                        databaseReference.removeValue();

                        ChocoBar.builder().setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                                .setTextSize(16)
                                .setTextColor(Color.parseColor("#FFFFFF"))
                                .setTextTypefaceStyle(Typeface.BOLD)
                                .setText("Ürün listenizden çıkarıldı.")
                                .setMaxLines(2)
                                .centerText()
                                .setIcon(R.drawable.ic_snackbar)
                                .setActivity(getActivity())
                                .setDuration(ChocoBar.LENGTH_SHORT)
                                .build()
                                .show();
                    }

                }
            });


        }
    }


    private void toolbarAction() {


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getActivity().onBackPressed();

            }
        });
    }


    private HashMap<String, Object> returnMap() {

        HashMap<String, Object> map = new HashMap<>();

        map.put("Durum", false);

        return map;
    }
}


