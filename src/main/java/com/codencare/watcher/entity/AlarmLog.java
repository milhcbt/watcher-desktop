/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Auto Generated Entity see database schema
 * @author Iman L Hakim <imanlhakim at gmail.com>
 */
@Entity
@Table(name = "alarm_log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AlarmLog.findAll", query = "SELECT a FROM AlarmLog a"),
    @NamedQuery(name = "AlarmLog.findById", query = "SELECT a FROM AlarmLog a WHERE a.id = :id"),
    @NamedQuery(name = "AlarmLog.findByNotes", query = "SELECT a FROM AlarmLog a WHERE a.notes = :notes"),
    @NamedQuery(name = "AlarmLog.findByTimeStamp", query = "SELECT a FROM AlarmLog a WHERE a.timeStamp = :timeStamp")})
public class AlarmLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "notes")
    private String notes;
    @Basic(optional = false)
    @Column(name = "time_stamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeStamp;
    @JoinColumn(name = "type", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LogType type;
    @JoinColumn(name = "device", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Device device;
    @JoinColumn(name = "user", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User user;

    public AlarmLog() {
    }

    public AlarmLog(Integer id) {
        this.id = id;
    }

    public AlarmLog(Integer id, String notes, Date timeStamp) {
        this.id = id;
        this.notes = notes;
        this.timeStamp = timeStamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public LogType getType() {
        return type;
    }

    public void setType(LogType type) {
        this.type = type;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        if (!(object instanceof AlarmLog)) {
            return false;
        }
        AlarmLog other = (AlarmLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.codencare.watcher.entity.AlarmLog[ id=" + id + " ]";
    }
    
}
