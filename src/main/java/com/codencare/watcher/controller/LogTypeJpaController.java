/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.controller;

import com.codencare.watcher.controller.exceptions.IllegalOrphanException;
import com.codencare.watcher.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.codencare.watcher.entity.AlarmLog;
import com.codencare.watcher.entity.LogType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * TODO: documentation same as AlarmLog Controller
 * @author ImanLHakim@gmail.com
 */
public class LogTypeJpaController implements Serializable {

    public LogTypeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(LogType logType) {
        if (logType.getAlarmLogCollection() == null) {
            logType.setAlarmLogCollection(new ArrayList<AlarmLog>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<AlarmLog> attachedAlarmLogCollection = new ArrayList<AlarmLog>();
            for (AlarmLog alarmLogCollectionAlarmLogToAttach : logType.getAlarmLogCollection()) {
                alarmLogCollectionAlarmLogToAttach = em.getReference(alarmLogCollectionAlarmLogToAttach.getClass(), alarmLogCollectionAlarmLogToAttach.getId());
                attachedAlarmLogCollection.add(alarmLogCollectionAlarmLogToAttach);
            }
            logType.setAlarmLogCollection(attachedAlarmLogCollection);
            em.persist(logType);
            for (AlarmLog alarmLogCollectionAlarmLog : logType.getAlarmLogCollection()) {
                LogType oldTypeOfAlarmLogCollectionAlarmLog = alarmLogCollectionAlarmLog.getType();
                alarmLogCollectionAlarmLog.setType(logType);
                alarmLogCollectionAlarmLog = em.merge(alarmLogCollectionAlarmLog);
                if (oldTypeOfAlarmLogCollectionAlarmLog != null) {
                    oldTypeOfAlarmLogCollectionAlarmLog.getAlarmLogCollection().remove(alarmLogCollectionAlarmLog);
                    oldTypeOfAlarmLogCollectionAlarmLog = em.merge(oldTypeOfAlarmLogCollectionAlarmLog);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(LogType logType) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LogType persistentLogType = em.find(LogType.class, logType.getId());
            Collection<AlarmLog> alarmLogCollectionOld = persistentLogType.getAlarmLogCollection();
            Collection<AlarmLog> alarmLogCollectionNew = logType.getAlarmLogCollection();
            List<String> illegalOrphanMessages = null;
            for (AlarmLog alarmLogCollectionOldAlarmLog : alarmLogCollectionOld) {
                if (!alarmLogCollectionNew.contains(alarmLogCollectionOldAlarmLog)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AlarmLog " + alarmLogCollectionOldAlarmLog + " since its type field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<AlarmLog> attachedAlarmLogCollectionNew = new ArrayList<AlarmLog>();
            for (AlarmLog alarmLogCollectionNewAlarmLogToAttach : alarmLogCollectionNew) {
                alarmLogCollectionNewAlarmLogToAttach = em.getReference(alarmLogCollectionNewAlarmLogToAttach.getClass(), alarmLogCollectionNewAlarmLogToAttach.getId());
                attachedAlarmLogCollectionNew.add(alarmLogCollectionNewAlarmLogToAttach);
            }
            alarmLogCollectionNew = attachedAlarmLogCollectionNew;
            logType.setAlarmLogCollection(alarmLogCollectionNew);
            logType = em.merge(logType);
            for (AlarmLog alarmLogCollectionNewAlarmLog : alarmLogCollectionNew) {
                if (!alarmLogCollectionOld.contains(alarmLogCollectionNewAlarmLog)) {
                    LogType oldTypeOfAlarmLogCollectionNewAlarmLog = alarmLogCollectionNewAlarmLog.getType();
                    alarmLogCollectionNewAlarmLog.setType(logType);
                    alarmLogCollectionNewAlarmLog = em.merge(alarmLogCollectionNewAlarmLog);
                    if (oldTypeOfAlarmLogCollectionNewAlarmLog != null && !oldTypeOfAlarmLogCollectionNewAlarmLog.equals(logType)) {
                        oldTypeOfAlarmLogCollectionNewAlarmLog.getAlarmLogCollection().remove(alarmLogCollectionNewAlarmLog);
                        oldTypeOfAlarmLogCollectionNewAlarmLog = em.merge(oldTypeOfAlarmLogCollectionNewAlarmLog);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = logType.getId();
                if (findLogType(id) == null) {
                    throw new NonexistentEntityException("The logType with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LogType logType;
            try {
                logType = em.getReference(LogType.class, id);
                logType.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The logType with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<AlarmLog> alarmLogCollectionOrphanCheck = logType.getAlarmLogCollection();
            for (AlarmLog alarmLogCollectionOrphanCheckAlarmLog : alarmLogCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This LogType (" + logType + ") cannot be destroyed since the AlarmLog " + alarmLogCollectionOrphanCheckAlarmLog + " in its alarmLogCollection field has a non-nullable type field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(logType);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<LogType> findLogTypeEntities() {
        return findLogTypeEntities(true, -1, -1);
    }

    public List<LogType> findLogTypeEntities(int maxResults, int firstResult) {
        return findLogTypeEntities(false, maxResults, firstResult);
    }

    private List<LogType> findLogTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(LogType.class));
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

    public LogType findLogType(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LogType.class, id);
        } finally {
            em.close();
        }
    }

    public int getLogTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LogType> rt = cq.from(LogType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
