/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.controller;

import com.codencare.watcher.controller.exceptions.IllegalOrphanException;
import com.codencare.watcher.controller.exceptions.NonexistentEntityException;
import com.codencare.watcher.controller.exceptions.PreexistingEntityException;
import com.codencare.watcher.entity.City;
import com.codencare.watcher.entity.Customer;
import com.codencare.watcher.entity.Device;
import java.io.Serializable;
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
 * TODO: documentation same as AlarmLog Controller
 * @author ImanLHakim@gmail.com
 */
public class CustomerJpaController implements Serializable {

    public CustomerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Customer customer) throws PreexistingEntityException, Exception {
        if (customer.getDeviceCollection() == null) {
            customer.setDeviceCollection(new ArrayList<Device>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            City city = customer.getCity();
            if (city != null) {
                city = em.getReference(city.getClass(), city.getId());
                customer.setCity(city);
            }
            Collection<Device> attachedDeviceCollection = new ArrayList<Device>();
            for (Device deviceCollectionDeviceToAttach : customer.getDeviceCollection()) {
                deviceCollectionDeviceToAttach = em.getReference(deviceCollectionDeviceToAttach.getClass(), deviceCollectionDeviceToAttach.getId());
                attachedDeviceCollection.add(deviceCollectionDeviceToAttach);
            }
            customer.setDeviceCollection(attachedDeviceCollection);
            em.persist(customer);
            if (city != null) {
                city.getCustomerCollection().add(customer);
                city = em.merge(city);
            }
            for (Device deviceCollectionDevice : customer.getDeviceCollection()) {
                Customer oldCustomerIdOfDeviceCollectionDevice = deviceCollectionDevice.getCustomer();
                deviceCollectionDevice.setCustomer(customer);
                deviceCollectionDevice = em.merge(deviceCollectionDevice);
                if (oldCustomerIdOfDeviceCollectionDevice != null) {
                    oldCustomerIdOfDeviceCollectionDevice.getDeviceCollection().remove(deviceCollectionDevice);
                    oldCustomerIdOfDeviceCollectionDevice = em.merge(oldCustomerIdOfDeviceCollectionDevice);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCustomer(customer.getId()) != null) {
                throw new PreexistingEntityException("Customer " + customer + " already exists.", ex);
            }
            throw ex;
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
            City cityOld = persistentCustomer.getCity();
            City cityNew = customer.getCity();
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
            if (cityNew != null) {
                cityNew = em.getReference(cityNew.getClass(), cityNew.getId());
                customer.setCity(cityNew);
            }
            Collection<Device> attachedDeviceCollectionNew = new ArrayList<Device>();
            for (Device deviceCollectionNewDeviceToAttach : deviceCollectionNew) {
                deviceCollectionNewDeviceToAttach = em.getReference(deviceCollectionNewDeviceToAttach.getClass(), deviceCollectionNewDeviceToAttach.getId());
                attachedDeviceCollectionNew.add(deviceCollectionNewDeviceToAttach);
            }
            deviceCollectionNew = attachedDeviceCollectionNew;
            customer.setDeviceCollection(deviceCollectionNew);
            customer = em.merge(customer);
            if (cityOld != null && !cityOld.equals(cityNew)) {
                cityOld.getCustomerCollection().remove(customer);
                cityOld = em.merge(cityOld);
            }
            if (cityNew != null && !cityNew.equals(cityOld)) {
                cityNew.getCustomerCollection().add(customer);
                cityNew = em.merge(cityNew);
            }
            for (Device deviceCollectionNewDevice : deviceCollectionNew) {
                if (!deviceCollectionOld.contains(deviceCollectionNewDevice)) {
                    Customer oldCustomerIdOfDeviceCollectionNewDevice = deviceCollectionNewDevice.getCustomer();
                    deviceCollectionNewDevice.setCustomer(customer);
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
                Integer id = customer.getId();
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
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
            City city = customer.getCity();
            if (city != null) {
                city.getCustomerCollection().remove(customer);
                city = em.merge(city);
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

    public Customer findCustomer(Integer id) {
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
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
     public List<Customer> findByName(String name) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Customer.findByNameLike");
            q.setParameter("name", "%" + name + "%");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public int maxId() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNativeQuery("SELECT max(id) FROM `customer`");
            return q.getFirstResult();
            
           /// return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
