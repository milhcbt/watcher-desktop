/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codencare.watcher.controller;

import com.codencare.watcher.controller.exceptions.NonexistentEntityException;
import com.codencare.watcher.entity.AlarmLog;
import com.codencare.watcher.entity.Device;
import com.codencare.watcher.entity.LogType;
import com.codencare.watcher.entity.User;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author abah
 */
public class AlarmLogJpaController implements Serializable {

    public AlarmLogJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AlarmLog alarmLog) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LogType type = alarmLog.getType();
            if (type != null) {
                type = em.getReference(type.getClass(), type.getId());
                alarmLog.setType(type);
            }
            Device device = alarmLog.getDevice();
            if (device != null) {
                device = em.getReference(device.getClass(), device.getId());
                alarmLog.setDevice(device);
            }
            User user = alarmLog.getUser();
            if (user != null) {
                user = em.getReference(user.getClass(), user.getId());
                alarmLog.setUser(user);
            }
            em.persist(alarmLog);
            if (type != null) {
                type.getAlarmLogCollection().add(alarmLog);
                type = em.merge(type);
            }
            if (device != null) {
                device.getAlarmLogCollection().add(alarmLog);
                device = em.merge(device);
            }
            if (user != null) {
                user.getAlarmLogCollection().add(alarmLog);
                user = em.merge(user);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AlarmLog alarmLog) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AlarmLog persistentAlarmLog = em.find(AlarmLog.class, alarmLog.getId());
            LogType typeOld = persistentAlarmLog.getType();
            LogType typeNew = alarmLog.getType();
            Device deviceOld = persistentAlarmLog.getDevice();
            Device deviceNew = alarmLog.getDevice();
            User userOld = persistentAlarmLog.getUser();
            User userNew = alarmLog.getUser();
            if (typeNew != null) {
                typeNew = em.getReference(typeNew.getClass(), typeNew.getId());
                alarmLog.setType(typeNew);
            }
            if (deviceNew != null) {
                deviceNew = em.getReference(deviceNew.getClass(), deviceNew.getId());
                alarmLog.setDevice(deviceNew);
            }
            if (userNew != null) {
                userNew = em.getReference(userNew.getClass(), userNew.getId());
                alarmLog.setUser(userNew);
            }
            alarmLog = em.merge(alarmLog);
            if (typeOld != null && !typeOld.equals(typeNew)) {
                typeOld.getAlarmLogCollection().remove(alarmLog);
                typeOld = em.merge(typeOld);
            }
            if (typeNew != null && !typeNew.equals(typeOld)) {
                typeNew.getAlarmLogCollection().add(alarmLog);
                typeNew = em.merge(typeNew);
            }
            if (deviceOld != null && !deviceOld.equals(deviceNew)) {
                deviceOld.getAlarmLogCollection().remove(alarmLog);
                deviceOld = em.merge(deviceOld);
            }
            if (deviceNew != null && !deviceNew.equals(deviceOld)) {
                deviceNew.getAlarmLogCollection().add(alarmLog);
                deviceNew = em.merge(deviceNew);
            }
            if (userOld != null && !userOld.equals(userNew)) {
                userOld.getAlarmLogCollection().remove(alarmLog);
                userOld = em.merge(userOld);
            }
            if (userNew != null && !userNew.equals(userOld)) {
                userNew.getAlarmLogCollection().add(alarmLog);
                userNew = em.merge(userNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = alarmLog.getId();
                if (findAlarmLog(id) == null) {
                    throw new NonexistentEntityException("The alarmLog with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AlarmLog alarmLog;
            try {
                alarmLog = em.getReference(AlarmLog.class, id);
                alarmLog.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The alarmLog with id " + id + " no longer exists.", enfe);
            }
            LogType type = alarmLog.getType();
            if (type != null) {
                type.getAlarmLogCollection().remove(alarmLog);
                type = em.merge(type);
            }
            Device device = alarmLog.getDevice();
            if (device != null) {
                device.getAlarmLogCollection().remove(alarmLog);
                device = em.merge(device);
            }
            User user = alarmLog.getUser();
            if (user != null) {
                user.getAlarmLogCollection().remove(alarmLog);
                user = em.merge(user);
            }
            em.remove(alarmLog);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AlarmLog> findAlarmLogEntities() {
        return findAlarmLogEntities(true, -1, -1);
    }

    public List<AlarmLog> findAlarmLogEntities(int maxResults, int firstResult) {
        return findAlarmLogEntities(false, maxResults, firstResult);
    }

    private List<AlarmLog> findAlarmLogEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AlarmLog.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public AlarmLog findAlarmLog(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AlarmLog.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlarmLogCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AlarmLog> rt = cq.from(AlarmLog.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
