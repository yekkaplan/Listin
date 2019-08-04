package com.alisverisim.yek.listin.Models;

public class UsersValue {

    private String email;
    private String isim;
    private String resim;
    private String ortakListeler;
    private String dogumtarihi;

    public UsersValue(String email, String isim, String resim, String ortakListeler, String dogumtarihi) {
        this.email = email;
        this.isim = isim;
        this.resim = resim;
        this.ortakListeler = ortakListeler;
        this.dogumtarihi = dogumtarihi;
    }

    public String getDogumtarihi() {
        return dogumtarihi;
    }

    public void setDogumtarihi(String dogumtarihi) {
        this.dogumtarihi = dogumtarihi;
    }

    public String getOrtakListeler() {
        return ortakListeler;
    }

    public void setOrtakListeler(String ortakListeler) {
        this.ortakListeler = ortakListeler;
    }

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }

    public UsersValue() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }
}
