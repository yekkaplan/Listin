package com.alisverisim.yek.listin.Models;

public class UrunModels {


    private String urunAdi;
    private Boolean alindimi;
    private String not;


    public UrunModels(String urunAdi, Boolean alindimi, String not) {
        this.urunAdi = urunAdi;
        this.alindimi = alindimi;
        this.not = not;

    }


    public UrunModels() {


    }

    public String getNot() {
        return not;
    }

    public void setNot(String not) {
        this.not = not;
    }

    public String getUrunAdi() {
        return urunAdi;
    }

    public void setUrunAdi(String urunAdi) {
        this.urunAdi = urunAdi;
    }

    public Boolean getAlindimi() {
        return alindimi;
    }

    public void setAlindimi(Boolean alindimi) {
        this.alindimi = alindimi;
    }


}
