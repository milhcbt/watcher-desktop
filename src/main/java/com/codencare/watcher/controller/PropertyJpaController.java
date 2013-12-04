/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.controller;

import com.codencare.watcher.controller.exceptions.NonexistentEntityException;
import com.codencare.watcher.controller.exceptions.PreexistingEntityException;
import com.codencare.watcher.entity.Property;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * TODO: documentation same as AlarmLog Controller
 * @author ImanLHakim@gmail.com
 */
public class PropertyJpaController implements Serializable {

    public PropertyJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Property property) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(property);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProperty(property.getKey()) != null) {
                throw new PreexistingEntityException("Property " + property + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Property property) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            property = em.merge(property);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = property.getKey();
                if (findProperty(id) == null) {
                    throw new NonexistentEntityException("The property with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Property property;
            try {
                property = em.getReference(Property.class, id);
                property.getKey();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The property with id " + id + " no longer exists.", enfe);
            }
            em.remove(property);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Property> findPropertyEntities() {
        return findPropertyEntities(true, -1, -1);
    }

    public List<Property> findPropertyEntities(int maxResults, int firstResult) {
        return findPropertyEntities(false, maxResults, firstResult);
    }

    private List<Property> findPropertyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Property.class));
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

    public Property findProperty(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Property.class, id);
        } finally {
            em.close();
        }
    }

    public int getPropertyCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Property> rt = cq.from(Property.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
