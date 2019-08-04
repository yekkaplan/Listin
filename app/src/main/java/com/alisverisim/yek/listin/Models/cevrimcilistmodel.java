package com.alisverisim.yek.listin.Models;

import java.util.HashMap;

public class cevrimcilistmodel {

    String listeadi;
    Long ortaksayisi;
    Long urunsayisi;
    HashMap<String, Object> hashMap;

    public cevrimcilistmodel(String listeadi, Long ortaksayisi, Long urunsayisi, HashMap<String, Object> hashMap) {
        this.listeadi = listeadi;
        this.ortaksayisi = ortaksayisi;
        this.urunsayisi = urunsayisi;
        this.hashMap = hashMap;
    }

    public String getListeadi() {
        return listeadi;
    }

    public void setListeadi(String listeadi) {
        this.listeadi = listeadi;
    }

    public Long getOrtaksayisi() {
        return ortaksayisi;
    }

    public void setOrtaksayisi(Long ortaksayisi) {
        this.ortaksayisi = ortaksayisi;
    }

    public Long getUrunsayisi() {
        return urunsayisi;
    }

    public void setUrunsayisi(Long urunsayisi) {
        this.urunsayisi = urunsayisi;
    }

    public HashMap<String, Object> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, Object> hashMap) {
        this.hashMap = hashMap;
    }
}
