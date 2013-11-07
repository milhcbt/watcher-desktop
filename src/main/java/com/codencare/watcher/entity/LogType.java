/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codencare.watcher.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author abah
 */
@Entity
@Table(name = "log_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LogType.findAll", query = "SELECT l FROM LogType l"),
    @NamedQuery(name = "LogType.findById", query = "SELECT l FROM LogType l WHERE l.id = :id"),
    @NamedQuery(name = "LogType.findByDesc", query = "SELECT l FROM LogType l WHERE l.desc = :desc")})
public class LogType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    private String desc;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "type")
    private Collection<AlarmLog> alarmLogCollection;

    public LogType() {
    }

    public LogType(Integer id) {
        this.id = id;
    }

    public LogType(Integer id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<AlarmLog> getAlarmLogCollection() {
        return alarmLogCollection;
    }

    public void setAlarmLogCollection(Collection<AlarmLog> alarmLogCollection) {
        this.alarmLogCollection = alarmLogCollection;
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
        if (!(object instanceof LogType)) {
            return false;
        }
        LogType other = (LogType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.codencare.watcher.entity.LogType[ id=" + id + " ]";
    }
    
}
