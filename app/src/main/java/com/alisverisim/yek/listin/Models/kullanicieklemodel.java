package com.alisverisim.yek.listin.Models;

public class kullanicieklemodel {

    public String email;
    public String uid;


    public kullanicieklemodel(String email, String uid) {
        this.email = email;
        this.uid = uid;
    }

    public kullanicieklemodel(){

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
}
