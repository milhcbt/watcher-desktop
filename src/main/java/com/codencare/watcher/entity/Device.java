/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codencare.watcher.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author abah
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Device.findAll", query = "SELECT d FROM Device d"),
    @NamedQuery(name = "Device.findById", query = "SELECT d FROM Device d WHERE d.id = :id"),
    @NamedQuery(name = "Device.findByLocX", query = "SELECT d FROM Device d WHERE d.locX = :locX"),
    @NamedQuery(name = "Device.findByLocY", query = "SELECT d FROM Device d WHERE d.locY = :locY"),
    @NamedQuery(name = "Device.findByLastTime", query = "SELECT d FROM Device d WHERE d.lastTime = :lastTime"),
    @NamedQuery(name = "Device.findByResolve", query = "SELECT d FROM Device d WHERE d.resolve = :resolve"),
    @NamedQuery(name = "Device.findByDigit1", query = "SELECT d FROM Device d WHERE d.digit1 = :digit1"),
    @NamedQuery(name = "Device.findByDigit2", query = "SELECT d FROM Device d WHERE d.digit2 = :digit2"),
    @NamedQuery(name = "Device.findByDigit3", query = "SELECT d FROM Device d WHERE d.digit3 = :digit3"),
    @NamedQuery(name = "Device.findByDigit4", query = "SELECT d FROM Device d WHERE d.digit4 = :digit4"),
    @NamedQuery(name = "Device.findByAnalog1", query = "SELECT d FROM Device d WHERE d.analog1 = :analog1"),
    @NamedQuery(name = "Device.findByAnalog2", query = "SELECT d FROM Device d WHERE d.analog2 = :analog2"),
    @NamedQuery(name = "Device.findByAnalog3", query = "SELECT d FROM Device d WHERE d.analog3 = :analog3"),
    @NamedQuery(name = "Device.findByAnalog4", query = "SELECT d FROM Device d WHERE d.analog4 = :analog4")})
public class Device implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    private BigInteger id;
    @Basic(optional = false)
    private int locX;
    @Basic(optional = false)
    private int locY;
    @Basic(optional = false)
    @Column(name = "last_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastTime;
    @Basic(optional = false)
    private boolean resolve;
    @Basic(optional = false)
    private boolean digit1;
    @Basic(optional = false)
    private boolean digit2;
    @Basic(optional = false)
    private boolean digit3;
    @Basic(optional = false)
    private boolean digit4;
    @Basic(optional = false)
    private short analog1;
    @Basic(optional = false)
    private short analog2;
    @Basic(optional = false)
    private short analog3;
    @Basic(optional = false)
    private short analog4;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "deviceId", fetch = FetchType.LAZY)
    private Collection<Message> messageCollection;
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Customer customerId;

    public Device() {
    }

    public Device(BigInteger id) {
        this.id = id;
    }

    public Device(BigInteger id, int locX, int locY, Date lastTime, boolean resolve, boolean digit1, boolean digit2, boolean digit3, boolean digit4, short analog1, short analog2, short analog3, short analog4) {
        this.id = id;
        this.locX = locX;
        this.locY = locY;
        this.lastTime = lastTime;
        this.resolve = resolve;
        this.digit1 = digit1;
        this.digit2 = digit2;
        this.digit3 = digit3;
        this.digit4 = digit4;
        this.analog1 = analog1;
        this.analog2 = analog2;
        this.analog3 = analog3;
        this.analog4 = analog4;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
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

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public boolean getResolve() {
        return resolve;
    }

    public void setResolve(boolean resolve) {
        this.resolve = resolve;
    }

    public boolean getDigit1() {
        return digit1;
    }

    public void setDigit1(boolean digit1) {
        this.digit1 = digit1;
    }

    public boolean getDigit2() {
        return digit2;
    }

    public void setDigit2(boolean digit2) {
        this.digit2 = digit2;
    }

    public boolean getDigit3() {
        return digit3;
    }

    public void setDigit3(boolean digit3) {
        this.digit3 = digit3;
    }

    public boolean getDigit4() {
        return digit4;
    }

    public void setDigit4(boolean digit4) {
        this.digit4 = digit4;
    }

    public short getAnalog1() {
        return analog1;
    }

    public void setAnalog1(short analog1) {
        this.analog1 = analog1;
    }

    public short getAnalog2() {
        return analog2;
    }

    public void setAnalog2(short analog2) {
        this.analog2 = analog2;
    }

    public short getAnalog3() {
        return analog3;
    }

    public void setAnalog3(short analog3) {
        this.analog3 = analog3;
    }

    public short getAnalog4() {
        return analog4;
    }

    public void setAnalog4(short analog4) {
        this.analog4 = analog4;
    }

    @XmlTransient
    public Collection<Message> getMessageCollection() {
        return messageCollection;
    }

    public void setMessageCollection(Collection<Message> messageCollection) {
        this.messageCollection = messageCollection;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Device)) {
            return false;
        }
        Device other = (Device) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.codencare.watcher.entity.Device[ id=" + id + " ]";
    }
    
}
