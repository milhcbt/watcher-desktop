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
 * Auto-generated entity class. @see database schema.
 * @author Iman L Hakim <imanlhakim at gmail.com>
 */
@Entity
@Table(name = "message")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Message.findAll", query = "SELECT m FROM Message m"),
    @NamedQuery(name = "Message.findById", query = "SELECT m FROM Message m WHERE m.id = :id"),
    @NamedQuery(name = "Message.findByMsgtime", query = "SELECT m FROM Message m WHERE m.msgtime = :msgtime"),
    @NamedQuery(name = "Message.findByMessage", query = "SELECT m FROM Message m WHERE m.message = :message"),
    @NamedQuery(name = "Message.findByMsgType", query = "SELECT m FROM Message m WHERE m.msgType = :msgType")})
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "msgtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date msgtime;
    @Basic(optional = false)
    @Column(name = "message")
    private String message;
    @Basic(optional = false)
    @Column(name = "msg_type")
    private boolean msgType;
    @JoinColumn(name = "device", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Device device;

    public Message() {
    }

    public Message(Long id) {
        this.id = id;
    }

    public Message(Long id, Date msgtime, String message, boolean msgType) {
        this.id = id;
        this.msgtime = msgtime;
        this.message = message;
        this.msgType = msgType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getMsgtime() {
        return msgtime;
    }

    public void setMsgtime(Date msgtime) {
        this.msgtime = msgtime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getMsgType() {
        return msgType;
    }

    public void setMsgType(boolean msgType) {
        this.msgType = msgType;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
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
        if (!(object instanceof Message)) {
            return false;
        }
        Message other = (Message) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.codencare.watcher.entity.Message[ id=" + id + " ]";
    }
    
}
