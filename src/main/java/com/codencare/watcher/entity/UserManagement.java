package com.codencare.watcher.entity;

import java.io.Serializable;

public class UserManagement implements Serializable {

    private int id;
    private String nama;
    private String alamat;
    private int phone;
    private String email;

    public UserManagement() {
    }

    public UserManagement(int id, String nama, String alamat, int phone, String email) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.phone = phone;
        this.email = email;
    }

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public String getnama() {
        return nama;
    }

    public void setnama(String nama) {
        this.nama = nama;
    }

    public String getalamat() {
        return alamat;
    }

    public void setalamat(String surname) {
        this.alamat = alamat;
    }

    public int getphone() {
        return phone;
    }

    public void setphone(int phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
