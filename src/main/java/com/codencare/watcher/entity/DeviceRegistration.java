package com.codencare.watcher.entity;

import java.io.Serializable;

public class DeviceRegistration implements Serializable {

    private int id;
    private String device_id;
    private String device_ip;
    private String remarks;

    public DeviceRegistration() {
    }

    public DeviceRegistration(int id, String device_id, String device_ip, String remarks) {
        this.id = id;
        this.device_id = device_id;
        this.device_ip = device_ip;
        this.remarks = remarks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceId() {
        return device_id;
    }

    public void setDeviceId(String device_id) {
        this.device_id = device_id;
    }

    public String getDeviceIp() {
        return device_ip;
    }

    public void setDeviceIp(String device_ip) {
        this.device_ip = device_ip;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
