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
import com.codencare.watcher.entity.User;
import com.codencare.watcher.util.ComboPair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * TODO: documentation same as AlarmLog Controller
 * @author ImanLHakim@gmail.com
 */
public class UserJpaController implements Serializable {

    public UserJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(User user) {
        if (user.getAlarmLogCollection() == null) {
            user.setAlarmLogCollection(new ArrayList<AlarmLog>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<AlarmLog> attachedAlarmLogCollection = new ArrayList<AlarmLog>();
            for (AlarmLog alarmLogCollectionAlarmLogToAttach : user.getAlarmLogCollection()) {
                alarmLogCollectionAlarmLogToAttach = em.getReference(alarmLogCollectionAlarmLogToAttach.getClass(), alarmLogCollectionAlarmLogToAttach.getId());
                attachedAlarmLogCollection.add(alarmLogCollectionAlarmLogToAttach);
            }
            user.setAlarmLogCollection(attachedAlarmLogCollection);
            em.persist(user);
            for (AlarmLog alarmLogCollectionAlarmLog : user.getAlarmLogCollection()) {
                User oldUserOfAlarmLogCollectionAlarmLog = alarmLogCollectionAlarmLog.getUser();
                alarmLogCollectionAlarmLog.setUser(user);
                alarmLogCollectionAlarmLog = em.merge(alarmLogCollectionAlarmLog);
                if (oldUserOfAlarmLogCollectionAlarmLog != null) {
                    oldUserOfAlarmLogCollectionAlarmLog.getAlarmLogCollection().remove(alarmLogCollectionAlarmLog);
                    oldUserOfAlarmLogCollectionAlarmLog = em.merge(oldUserOfAlarmLogCollectionAlarmLog);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(User user) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User persistentUser = em.find(User.class, user.getId());
            Collection<AlarmLog> alarmLogCollectionOld = persistentUser.getAlarmLogCollection();
            Collection<AlarmLog> alarmLogCollectionNew = user.getAlarmLogCollection();
            List<String> illegalOrphanMessages = null;
            for (AlarmLog alarmLogCollectionOldAlarmLog : alarmLogCollectionOld) {
                if (!alarmLogCollectionNew.contains(alarmLogCollectionOldAlarmLog)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AlarmLog " + alarmLogCollectionOldAlarmLog + " since its user field is not nullable.");
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
            user.setAlarmLogCollection(alarmLogCollectionNew);
            user = em.merge(user);
            for (AlarmLog alarmLogCollectionNewAlarmLog : alarmLogCollectionNew) {
                if (!alarmLogCollectionOld.contains(alarmLogCollectionNewAlarmLog)) {
                    User oldUserOfAlarmLogCollectionNewAlarmLog = alarmLogCollectionNewAlarmLog.getUser();
                    alarmLogCollectionNewAlarmLog.setUser(user);
                    alarmLogCollectionNewAlarmLog = em.merge(alarmLogCollectionNewAlarmLog);
                    if (oldUserOfAlarmLogCollectionNewAlarmLog != null && !oldUserOfAlarmLogCollectionNewAlarmLog.equals(user)) {
                        oldUserOfAlarmLogCollectionNewAlarmLog.getAlarmLogCollection().remove(alarmLogCollectionNewAlarmLog);
                        oldUserOfAlarmLogCollectionNewAlarmLog = em.merge(oldUserOfAlarmLogCollectionNewAlarmLog);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = user.getId();
                if (findUser(id) == null) {
                    throw new NonexistentEntityException("The user with id " + id + " no longer exists.");
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
            User user;
            try {
                user = em.getReference(User.class, id);
                user.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The user with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<AlarmLog> alarmLogCollectionOrphanCheck = user.getAlarmLogCollection();
            for (AlarmLog alarmLogCollectionOrphanCheckAlarmLog : alarmLogCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the AlarmLog " + alarmLogCollectionOrphanCheckAlarmLog + " in its alarmLogCollection field has a non-nullable user field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(user);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<User> findUserEntities() {
        return findUserEntities(true, -1, -1);
    }

    public List<User> findUserEntities(int maxResults, int firstResult) {
        return findUserEntities(false, maxResults, firstResult);
    }

    private List<User> findUserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(User.class));
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

    public User findUser(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<User> rt = cq.from(User.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<User> findByNameLike(String name) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("User.findByNameLike");
            q.setParameter("name", "%" + name + "%");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
   
    public List<Object[]> listIdName() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("User.listIdName");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
     public  ObservableList<ComboPair<Long, String>> updateUser() {
        ObservableList<ComboPair<Long, String>> userOList;
        UserJpaController ujc = new UserJpaController(emf);
        userOList = FXCollections.observableArrayList();
        List<User> userList = ujc.findUserEntities();
        for (User user : userList) {
            ComboPair p = new ComboPair(user.getId(), user.getName());
            userOList.add(p);
        }
        return userOList;
    }
}
