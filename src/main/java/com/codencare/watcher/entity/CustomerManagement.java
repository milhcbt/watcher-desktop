package com.codencare.watcher.entity;

import java.io.Serializable;

public class CustomerManagement implements Serializable {

    private int id;
    private String nama;
    private String alamat;
    private String phone;
    private String email;

    public CustomerManagement() {
    }

    public CustomerManagement(int id, String nama, String alamat, String phone, String email) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.phone = phone;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
