package com.codencare.watcher.entity;

import java.io.Serializable;
import java.math.BigInteger;

public class MappingManagement implements Serializable {
 
    private String nama;
    private int locX;
    private int locY;

    public MappingManagement() {
    }

    public MappingManagement(String nama, int locX,int locY) {
        this.nama = nama;
        this.locX = locX;
        this.locX = locY;
    }

    public String getName() {
        return nama;
    }

    public void setName(String name) {
        this.nama = nama;
    }

    public int getLocX() {
        return locX;
    }

    public void setLocX(int locX) {
        this.locX = locX;
    }
    public int getLocY() {
        return locY;
    }

    public void setLocY(int locY) {
        this.locY = locY;
    }

}
