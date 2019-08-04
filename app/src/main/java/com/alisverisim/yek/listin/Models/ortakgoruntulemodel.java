package com.alisverisim.yek.listin.Models;

public class ortakgoruntulemodel {

    public String email;
    public String uid;
    public String listkey;

    public ortakgoruntulemodel(String email, String uid, String listkey) {
        this.email = email;
        this.uid = uid;
        this.listkey = listkey;
    }

    public ortakgoruntulemodel() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getListkey() {
        return listkey;
    }

    public void setListkey(String listkey) {
        this.listkey = listkey;
    }
}
