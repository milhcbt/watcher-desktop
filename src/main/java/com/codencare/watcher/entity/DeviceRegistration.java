package com.codencare.watcher.entity;

import java.io.Serializable;

public class DeviceRegistration implements Serializable {

    private int id;
    private String deviceid;
    private String deviceip;
    private String remarks;

    public DeviceRegistration() {
    }

    public DeviceRegistration(int id, String deviceid, String deviceip, String remarks) {
        this.id = id;
        this.deviceid = deviceid;
        this.deviceip = deviceip;
        this.remarks = remarks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceid;
    }

    public void setDeviceId(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getDeviceIp() {
        return deviceip;
    }

    public void setDeviceIp(String dip) {
        this.deviceip = deviceip;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
