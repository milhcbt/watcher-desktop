/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.controller;

import com.codencare.watcher.controller.exceptions.NonexistentEntityException;
import com.codencare.watcher.entity.Contact;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.codencare.watcher.entity.ContactGroup;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * TODO: documentation same as AlarmLog Controller
 * @author ImanLHakim@gmail.com
 */
public class ContactJpaController implements Serializable {

    public ContactJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Contact contact) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ContactGroup group1 = contact.getGroup1();
            if (group1 != null) {
                group1 = em.getReference(group1.getClass(), group1.getId());
                contact.setGroup1(group1);
            }
            em.persist(contact);
            if (group1 != null) {
                group1.getContactCollection().add(contact);
                group1 = em.merge(group1);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Contact contact) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contact persistentContact = em.find(Contact.class, contact.getId());
            ContactGroup group1Old = persistentContact.getGroup1();
            ContactGroup group1New = contact.getGroup1();
            if (group1New != null) {
                group1New = em.getReference(group1New.getClass(), group1New.getId());
                contact.setGroup1(group1New);
            }
            contact = em.merge(contact);
            if (group1Old != null && !group1Old.equals(group1New)) {
                group1Old.getContactCollection().remove(contact);
                group1Old = em.merge(group1Old);
            }
            if (group1New != null && !group1New.equals(group1Old)) {
                group1New.getContactCollection().add(contact);
                group1New = em.merge(group1New);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = contact.getId();
                if (findContact(id) == null) {
                    throw new NonexistentEntityException("The contact with id " + id + " no longer exists.");
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
            Contact contact;
            try {
                contact = em.getReference(Contact.class, id);
                contact.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The contact with id " + id + " no longer exists.", enfe);
            }
            ContactGroup group1 = contact.getGroup1();
            if (group1 != null) {
                group1.getContactCollection().remove(contact);
                group1 = em.merge(group1);
            }
            em.remove(contact);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Contact> findContactEntities() {
        return findContactEntities(true, -1, -1);
    }

    public List<Contact> findContactEntities(int maxResults, int firstResult) {
        return findContactEntities(false, maxResults, firstResult);
    }

    private List<Contact> findContactEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Contact.class));
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

    public Contact findContact(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Contact.class, id);
        } finally {
            em.close();
        }
    }

    public int getContactCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Contact> rt = cq.from(Contact.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
