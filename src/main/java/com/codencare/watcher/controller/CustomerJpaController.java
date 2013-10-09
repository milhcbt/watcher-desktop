/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codencare.watcher.controller;

import com.codencare.watcher.controller.exceptions.IllegalOrphanException;
import com.codencare.watcher.controller.exceptions.NonexistentEntityException;
import com.codencare.watcher.entity.Customer;
import com.codencare.watcher.entity.Device;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
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
public class CustomerJpaController implements Serializable {

    public CustomerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Customer customer) {
        if (customer.getDeviceCollection() == null) {
            customer.setDeviceCollection(new ArrayList<Device>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Device> attachedDeviceCollection = new ArrayList<Device>();
            for (Device deviceCollectionDeviceToAttach : customer.getDeviceCollection()) {
                deviceCollectionDeviceToAttach = em.getReference(deviceCollectionDeviceToAttach.getClass(), deviceCollectionDeviceToAttach.getId());
                attachedDeviceCollection.add(deviceCollectionDeviceToAttach);
            }
            customer.setDeviceCollection(attachedDeviceCollection);
            em.persist(customer);
            for (Device deviceCollectionDevice : customer.getDeviceCollection()) {
                Customer oldCustomerIdOfDeviceCollectionDevice = deviceCollectionDevice.getCustomerId();
                deviceCollectionDevice.setCustomerId(customer);
                deviceCollectionDevice = em.merge(deviceCollectionDevice);
                if (oldCustomerIdOfDeviceCollectionDevice != null) {
                    oldCustomerIdOfDeviceCollectionDevice.getDeviceCollection().remove(deviceCollectionDevice);
                    oldCustomerIdOfDeviceCollectionDevice = em.merge(oldCustomerIdOfDeviceCollectionDevice);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Customer customer) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Customer persistentCustomer = em.find(Customer.class, customer.getId());
            Collection<Device> deviceCollectionOld = persistentCustomer.getDeviceCollection();
            Collection<Device> deviceCollectionNew = customer.getDeviceCollection();
            List<String> illegalOrphanMessages = null;
            for (Device deviceCollectionOldDevice : deviceCollectionOld) {
                if (!deviceCollectionNew.contains(deviceCollectionOldDevice)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Device " + deviceCollectionOldDevice + " since its customerId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Device> attachedDeviceCollectionNew = new ArrayList<Device>();
            for (Device deviceCollectionNewDeviceToAttach : deviceCollectionNew) {
                deviceCollectionNewDeviceToAttach = em.getReference(deviceCollectionNewDeviceToAttach.getClass(), deviceCollectionNewDeviceToAttach.getId());
                attachedDeviceCollectionNew.add(deviceCollectionNewDeviceToAttach);
            }
            deviceCollectionNew = attachedDeviceCollectionNew;
            customer.setDeviceCollection(deviceCollectionNew);
            customer = em.merge(customer);
            for (Device deviceCollectionNewDevice : deviceCollectionNew) {
                if (!deviceCollectionOld.contains(deviceCollectionNewDevice)) {
                    Customer oldCustomerIdOfDeviceCollectionNewDevice = deviceCollectionNewDevice.getCustomerId();
                    deviceCollectionNewDevice.setCustomerId(customer);
                    deviceCollectionNewDevice = em.merge(deviceCollectionNewDevice);
                    if (oldCustomerIdOfDeviceCollectionNewDevice != null && !oldCustomerIdOfDeviceCollectionNewDevice.equals(customer)) {
                        oldCustomerIdOfDeviceCollectionNewDevice.getDeviceCollection().remove(deviceCollectionNewDevice);
                        oldCustomerIdOfDeviceCollectionNewDevice = em.merge(oldCustomerIdOfDeviceCollectionNewDevice);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = customer.getId();
                if (findCustomer(id) == null) {
                    throw new NonexistentEntityException("The customer with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Customer customer;
            try {
                customer = em.getReference(Customer.class, id);
                customer.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The customer with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Device> deviceCollectionOrphanCheck = customer.getDeviceCollection();
            for (Device deviceCollectionOrphanCheckDevice : deviceCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Customer (" + customer + ") cannot be destroyed since the Device " + deviceCollectionOrphanCheckDevice + " in its deviceCollection field has a non-nullable customerId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(customer);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Customer> findCustomerEntities() {
        return findCustomerEntities(true, -1, -1);
    }

    public List<Customer> findCustomerEntities(int maxResults, int firstResult) {
        return findCustomerEntities(false, maxResults, firstResult);
    }

    private List<Customer> findCustomerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Customer.class));
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

    public Customer findCustomer(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Customer.class, id);
        } finally {
            em.close();
        }
    }

    public int getCustomerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Customer> rt = cq.from(Customer.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((BigInteger) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public long maxId(){
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNativeQuery("SELECT max(id) FROM `customer`");
            return ((BigInteger)q.getSingleResult()).longValue();
        } finally {
            em.close();
        }
    }
}
