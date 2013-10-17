package com.codencare.watcher.entity;

import java.io.Serializable;

public class DeviceListManagement implements Serializable {

    private Integer no;
    private String device_id;
    private String device_ip;
    private String remarks;

    public DeviceListManagement() {
    }

    public DeviceListManagement(Integer no,String device_id, String device_ip, String remarks) {
        this.no = no;
        this.device_id = device_id;
        this.device_ip = device_ip;
        this.remarks = remarks;
    }
    
    public Integer getNo() {
        return no;
        
    }
    
    public void setNo(Integer no) {
        this.no = no;
    }
    
    public String getId() {
        return device_id;
    }

    public void setId(String device_id) {
        this.device_id = device_id;
    }

    public String getIp() {
        return device_ip;
    }

    public void setIp(String device_ip) {
        this.device_ip = device_ip;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
