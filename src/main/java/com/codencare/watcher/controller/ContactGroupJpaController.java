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
import com.codencare.watcher.entity.Contact;
import com.codencare.watcher.entity.ContactGroup;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * TODO: documentation same as AlarmLog Controller
 * @author ImanLHakim@gmail.com
 */
public class ContactGroupJpaController implements Serializable {

    public ContactGroupJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ContactGroup contactGroup) {
        if (contactGroup.getContactCollection() == null) {
            contactGroup.setContactCollection(new ArrayList<Contact>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Contact> attachedContactCollection = new ArrayList<Contact>();
            for (Contact contactCollectionContactToAttach : contactGroup.getContactCollection()) {
                contactCollectionContactToAttach = em.getReference(contactCollectionContactToAttach.getClass(), contactCollectionContactToAttach.getId());
                attachedContactCollection.add(contactCollectionContactToAttach);
            }
            contactGroup.setContactCollection(attachedContactCollection);
            em.persist(contactGroup);
            for (Contact contactCollectionContact : contactGroup.getContactCollection()) {
                ContactGroup oldGroup1OfContactCollectionContact = contactCollectionContact.getGroup1();
                contactCollectionContact.setGroup1(contactGroup);
                contactCollectionContact = em.merge(contactCollectionContact);
                if (oldGroup1OfContactCollectionContact != null) {
                    oldGroup1OfContactCollectionContact.getContactCollection().remove(contactCollectionContact);
                    oldGroup1OfContactCollectionContact = em.merge(oldGroup1OfContactCollectionContact);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ContactGroup contactGroup) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ContactGroup persistentContactGroup = em.find(ContactGroup.class, contactGroup.getId());
            Collection<Contact> contactCollectionOld = persistentContactGroup.getContactCollection();
            Collection<Contact> contactCollectionNew = contactGroup.getContactCollection();
            List<String> illegalOrphanMessages = null;
            for (Contact contactCollectionOldContact : contactCollectionOld) {
                if (!contactCollectionNew.contains(contactCollectionOldContact)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Contact " + contactCollectionOldContact + " since its group1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Contact> attachedContactCollectionNew = new ArrayList<Contact>();
            for (Contact contactCollectionNewContactToAttach : contactCollectionNew) {
                contactCollectionNewContactToAttach = em.getReference(contactCollectionNewContactToAttach.getClass(), contactCollectionNewContactToAttach.getId());
                attachedContactCollectionNew.add(contactCollectionNewContactToAttach);
            }
            contactCollectionNew = attachedContactCollectionNew;
            contactGroup.setContactCollection(contactCollectionNew);
            contactGroup = em.merge(contactGroup);
            for (Contact contactCollectionNewContact : contactCollectionNew) {
                if (!contactCollectionOld.contains(contactCollectionNewContact)) {
                    ContactGroup oldGroup1OfContactCollectionNewContact = contactCollectionNewContact.getGroup1();
                    contactCollectionNewContact.setGroup1(contactGroup);
                    contactCollectionNewContact = em.merge(contactCollectionNewContact);
                    if (oldGroup1OfContactCollectionNewContact != null && !oldGroup1OfContactCollectionNewContact.equals(contactGroup)) {
                        oldGroup1OfContactCollectionNewContact.getContactCollection().remove(contactCollectionNewContact);
                        oldGroup1OfContactCollectionNewContact = em.merge(oldGroup1OfContactCollectionNewContact);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = contactGroup.getId();
                if (findContactGroup(id) == null) {
                    throw new NonexistentEntityException("The contactGroup with id " + id + " no longer exists.");
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
            ContactGroup contactGroup;
            try {
                contactGroup = em.getReference(ContactGroup.class, id);
                contactGroup.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The contactGroup with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Contact> contactCollectionOrphanCheck = contactGroup.getContactCollection();
            for (Contact contactCollectionOrphanCheckContact : contactCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ContactGroup (" + contactGroup + ") cannot be destroyed since the Contact " + contactCollectionOrphanCheckContact + " in its contactCollection field has a non-nullable group1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(contactGroup);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ContactGroup> findContactGroupEntities() {
        return findContactGroupEntities(true, -1, -1);
    }

    public List<ContactGroup> findContactGroupEntities(int maxResults, int firstResult) {
        return findContactGroupEntities(false, maxResults, firstResult);
    }

    private List<ContactGroup> findContactGroupEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ContactGroup.class));
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

    public ContactGroup findContactGroup(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ContactGroup.class, id);
        } finally {
            em.close();
        }
    }

    public int getContactGroupCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ContactGroup> rt = cq.from(ContactGroup.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
