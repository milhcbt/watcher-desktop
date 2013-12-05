/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Auto-generated entity class, @see database schema.
 * @author Iman L Hakim <imanlhakim at gmail.com>
 */
@Entity
@Table(name = "device")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Device.findAll", query = "SELECT d FROM Device d"),
    @NamedQuery(name = "Device.findById", query = "SELECT d FROM Device d WHERE d.id = :id"),
    @NamedQuery(name = "Device.findByLocX", query = "SELECT d FROM Device d WHERE d.locX = :locX"),
    @NamedQuery(name = "Device.findByLocY", query = "SELECT d FROM Device d WHERE d.locY = :locY"),
    @NamedQuery(name = "Device.findByLastTime", query = "SELECT d FROM Device d WHERE d.lastTime = :lastTime"),
    @NamedQuery(name = "Device.findByMode", query = "SELECT d FROM Device d WHERE d.mode = :mode"),
    @NamedQuery(name = "Device.findByDigit1", query = "SELECT d FROM Device d WHERE d.digit1 = :digit1"),
    @NamedQuery(name = "Device.findByDigit2", query = "SELECT d FROM Device d WHERE d.digit2 = :digit2"),
    @NamedQuery(name = "Device.findByDigit3", query = "SELECT d FROM Device d WHERE d.digit3 = :digit3"),
    @NamedQuery(name = "Device.findByDigit4", query = "SELECT d FROM Device d WHERE d.digit4 = :digit4"),
    @NamedQuery(name = "Device.findByAnalog1", query = "SELECT d FROM Device d WHERE d.analog1 = :analog1"),
    @NamedQuery(name = "Device.findByAnalog2", query = "SELECT d FROM Device d WHERE d.analog2 = :analog2"),
    @NamedQuery(name = "Device.findByAnalog3", query = "SELECT d FROM Device d WHERE d.analog3 = :analog3"),
    @NamedQuery(name = "Device.findByAnalog4", query = "SELECT d FROM Device d WHERE d.analog4 = :analog4"),
    @NamedQuery(name = "Device.findByAddress", query = "SELECT d FROM Device d WHERE d.address = :address")})
public class Device implements Serializable {
    private static final long serialVersionUID = 1L;
//    public static final byte MODE_RESOLVED = 1;
//    public static final byte MODE_NORMAL = 1;
//    public static final byte MODE_UNRESOLVED= -1;
    public static final short MODE_ACTIVE_ON_AC=0;
    public static final short MODE_ALARMED=1;
    public static final short MODE_ACTIVE_ON_BATTERY=2;
    public static final short MODE_INACTIVE=3;
    public static final byte DIGIT_HIGH = 1;
    public static final byte DIGIT_ONKNOW = 0;
    public static final byte DIGIT_LOW = -1;
    
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "loc_x")
    private int locX;
    @Basic(optional = false)
    @Column(name = "loc_y")
    private int locY;
    @Basic(optional = false)
    @Column(name = "last_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastTime;
    @Basic(optional = false)
    @Column(name = "mode")
    private short mode;
    @Basic(optional = false)
    @Column(name = "digit1")
    private short digit1;
    @Basic(optional = false)
    @Column(name = "digit2")
    private short digit2;
    @Basic(optional = false)
    @Column(name = "digit3")
    private short digit3;
    @Basic(optional = false)
    @Column(name = "digit4")
    private short digit4;
    @Basic(optional = false)
    @Column(name = "analog1")
    private short analog1;
    @Basic(optional = false)
    @Column(name = "analog2")
    private short analog2;
    @Basic(optional = false)
    @Column(name = "analog3")
    private short analog3;
    @Basic(optional = false)
    @Column(name = "analog4")
    private short analog4;
    @Basic(optional = false)
    @Column(name = "address")
    private String address;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "device")
    private Collection<Message> messageCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "device")
    private Collection<AlarmLog> alarmLogCollection;
    @JoinColumn(name = "customer", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Customer customer;
    @JoinColumn(name = "city", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private City city;

    public Device() {
    }

    public Device(Long id) {
        this.id = id;
    }

    public Device(Long id, int locX, int locY, Date lastTime, short mode, short digit1, short digit2, short digit3, short digit4, short analog1, short analog2, short analog3, short analog4, String address) {
        this.id = id;
        this.locX = locX;
        this.locY = locY;
        this.lastTime = lastTime;
        this.mode = mode;
        this.digit1 = digit1;
        this.digit2 = digit2;
        this.digit3 = digit3;
        this.digit4 = digit4;
        this.analog1 = analog1;
        this.analog2 = analog2;
        this.analog3 = analog3;
        this.analog4 = analog4;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public short getMode() {
        return mode;
    }

    public void setMode(short mode) {
        this.mode = mode;
    }

    public short getDigit1() {
        return digit1;
    }

    public void setDigit1(short digit1) {
        this.digit1 = digit1;
    }

    public short getDigit2() {
        return digit2;
    }

    public void setDigit2(short digit2) {
        this.digit2 = digit2;
    }

    public short getDigit3() {
        return digit3;
    }

    public void setDigit3(short digit3) {
        this.digit3 = digit3;
    }

    public short getDigit4() {
        return digit4;
    }

    public void setDigit4(short digit4) {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @XmlTransient
    public Collection<Message> getMessageCollection() {
        return messageCollection;
    }

    public void setMessageCollection(Collection<Message> messageCollection) {
        this.messageCollection = messageCollection;
    }

    @XmlTransient
    public Collection<AlarmLog> getAlarmLogCollection() {
        return alarmLogCollection;
    }

    public void setAlarmLogCollection(Collection<AlarmLog> alarmLogCollection) {
        this.alarmLogCollection = alarmLogCollection;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
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
        return "Device{" + "id=" + id + ", locX=" + locX + ", locY=" 
                + locY + ", lastTime=" + lastTime + ", mode=" + mode 
                + ", digit1=" + digit1 + ", digit2=" + digit2 + ", digit3=" 
                + digit3 + ", digit4=" + digit4 + ", analog1=" + analog1 
                + ", analog2=" + analog2 + ", analog3=" + analog3 
                + ", analog4=" + analog4 + ", address=" + address 
                + ", messageCollection=" + messageCollection 
                + ", alarmLogCollection=" + alarmLogCollection 
                + ", customer=" + customer + ", city=" + city + '}';
    }    
}
