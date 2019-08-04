package com.alisverisim.yek.listin.Models;

public class notModel {

public String listeadi;

public String not;

    public notModel(String listeadi, String not) {
        this.listeadi = listeadi;
        this.not = not;
    }


    public  notModel(){



    }


    public String getListeadi() {
        return listeadi;
    }

    public void setListeadi(String listeadi) {
        this.listeadi = listeadi;
    }

    public String getNot() {
        return not;
    }

    public void setNot(String not) {
        this.not = not;
    }


    @Override
    public String toString() {
        return "notModel{" +
                "listeadi='" + listeadi + '\'' +
                ", not='" + not + '\'' +
                '}';
    }
}
