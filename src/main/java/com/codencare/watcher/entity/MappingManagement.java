package com.codencare.watcher.entity;

import java.io.Serializable;

public class MappingManagement implements Serializable {

    private int id_cus;
    private String name;
    private int locX;
    private int locY;

    public MappingManagement() {
    }

    public MappingManagement(int id_cus, String name, int locX,int locY) {
        this.id_cus=id_cus;
        this.name = name;
        this.locX = locX;
        this.locX = locY;
    }

    
    public int getId_cus() {
        return id_cus;
    }

    public void setId_cus(int id_cus) {
        this.id_cus = id_cus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
