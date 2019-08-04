package com.alisverisim.yek.listin.Models;

public class ortaklistmodel {

    String listeadi;
    String kullaniciuid;
    String listekey;

    // ortak liste işlemleri için oluşturduğum model.
    public ortaklistmodel(String listeadi, String kullaniciuid, String listekey) {
        this.listeadi = listeadi;
        this.kullaniciuid = kullaniciuid;
        this.listekey = listekey;
    }

    public ortaklistmodel() {

    }

    public String getListeadi() {
        return listeadi;
    }

    public void setListeadi(String listeadi) {
        this.listeadi = listeadi;
    }

    public String getKullaniciuid() {
        return kullaniciuid;
    }

    public void setKullaniciuid(String kullaniciuid) {
        this.kullaniciuid = kullaniciuid;
    }

    public String getListekey() {
        return listekey;
    }

    public void setListekey(String listekey) {
        this.listekey = listekey;
    }


}
