/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.controller;

import com.codencare.watcher.controller.exceptions.IllegalOrphanException;
import com.codencare.watcher.controller.exceptions.NonexistentEntityException;
import com.codencare.watcher.entity.City;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.codencare.watcher.entity.Device;
import java.util.ArrayList;
import java.util.Collection;
import com.codencare.watcher.entity.Customer;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * TODO: documentation same as AlarmLog Controller
 * @author ImanLHakim@gmail.com
 */
public class CityJpaController implements Serializable {

    public CityJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(City city) {
        if (city.getDeviceCollection() == null) {
            city.setDeviceCollection(new ArrayList<Device>());
        }
        if (city.getCustomerCollection() == null) {
            city.setCustomerCollection(new ArrayList<Customer>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Device> attachedDeviceCollection = new ArrayList<Device>();
            for (Device deviceCollectionDeviceToAttach : city.getDeviceCollection()) {
                deviceCollectionDeviceToAttach = em.getReference(deviceCollectionDeviceToAttach.getClass(), deviceCollectionDeviceToAttach.getId());
                attachedDeviceCollection.add(deviceCollectionDeviceToAttach);
            }
            city.setDeviceCollection(attachedDeviceCollection);
            Collection<Customer> attachedCustomerCollection = new ArrayList<Customer>();
            for (Customer customerCollectionCustomerToAttach : city.getCustomerCollection()) {
                customerCollectionCustomerToAttach = em.getReference(customerCollectionCustomerToAttach.getClass(), customerCollectionCustomerToAttach.getId());
                attachedCustomerCollection.add(customerCollectionCustomerToAttach);
            }
            city.setCustomerCollection(attachedCustomerCollection);
            em.persist(city);
            for (Device deviceCollectionDevice : city.getDeviceCollection()) {
                City oldCityOfDeviceCollectionDevice = deviceCollectionDevice.getCity();
                deviceCollectionDevice.setCity(city);
                deviceCollectionDevice = em.merge(deviceCollectionDevice);
                if (oldCityOfDeviceCollectionDevice != null) {
                    oldCityOfDeviceCollectionDevice.getDeviceCollection().remove(deviceCollectionDevice);
                    oldCityOfDeviceCollectionDevice = em.merge(oldCityOfDeviceCollectionDevice);
                }
            }
            for (Customer customerCollectionCustomer : city.getCustomerCollection()) {
                City oldCityOfCustomerCollectionCustomer = customerCollectionCustomer.getCity();
                customerCollectionCustomer.setCity(city);
                customerCollectionCustomer = em.merge(customerCollectionCustomer);
                if (oldCityOfCustomerCollectionCustomer != null) {
                    oldCityOfCustomerCollectionCustomer.getCustomerCollection().remove(customerCollectionCustomer);
                    oldCityOfCustomerCollectionCustomer = em.merge(oldCityOfCustomerCollectionCustomer);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(City city) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            City persistentCity = em.find(City.class, city.getId());
            Collection<Device> deviceCollectionOld = persistentCity.getDeviceCollection();
            Collection<Device> deviceCollectionNew = city.getDeviceCollection();
            Collection<Customer> customerCollectionOld = persistentCity.getCustomerCollection();
            Collection<Customer> customerCollectionNew = city.getCustomerCollection();
            List<String> illegalOrphanMessages = null;
            for (Device deviceCollectionOldDevice : deviceCollectionOld) {
                if (!deviceCollectionNew.contains(deviceCollectionOldDevice)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Device " + deviceCollectionOldDevice + " since its city field is not nullable.");
                }
            }
            for (Customer customerCollectionOldCustomer : customerCollectionOld) {
                if (!customerCollectionNew.contains(customerCollectionOldCustomer)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Customer " + customerCollectionOldCustomer + " since its city field is not nullable.");
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
            city.setDeviceCollection(deviceCollectionNew);
            Collection<Customer> attachedCustomerCollectionNew = new ArrayList<Customer>();
            for (Customer customerCollectionNewCustomerToAttach : customerCollectionNew) {
                customerCollectionNewCustomerToAttach = em.getReference(customerCollectionNewCustomerToAttach.getClass(), customerCollectionNewCustomerToAttach.getId());
                attachedCustomerCollectionNew.add(customerCollectionNewCustomerToAttach);
            }
            customerCollectionNew = attachedCustomerCollectionNew;
            city.setCustomerCollection(customerCollectionNew);
            city = em.merge(city);
            for (Device deviceCollectionNewDevice : deviceCollectionNew) {
                if (!deviceCollectionOld.contains(deviceCollectionNewDevice)) {
                    City oldCityOfDeviceCollectionNewDevice = deviceCollectionNewDevice.getCity();
                    deviceCollectionNewDevice.setCity(city);
                    deviceCollectionNewDevice = em.merge(deviceCollectionNewDevice);
                    if (oldCityOfDeviceCollectionNewDevice != null && !oldCityOfDeviceCollectionNewDevice.equals(city)) {
                        oldCityOfDeviceCollectionNewDevice.getDeviceCollection().remove(deviceCollectionNewDevice);
                        oldCityOfDeviceCollectionNewDevice = em.merge(oldCityOfDeviceCollectionNewDevice);
                    }
                }
            }
            for (Customer customerCollectionNewCustomer : customerCollectionNew) {
                if (!customerCollectionOld.contains(customerCollectionNewCustomer)) {
                    City oldCityOfCustomerCollectionNewCustomer = customerCollectionNewCustomer.getCity();
                    customerCollectionNewCustomer.setCity(city);
                    customerCollectionNewCustomer = em.merge(customerCollectionNewCustomer);
                    if (oldCityOfCustomerCollectionNewCustomer != null && !oldCityOfCustomerCollectionNewCustomer.equals(city)) {
                        oldCityOfCustomerCollectionNewCustomer.getCustomerCollection().remove(customerCollectionNewCustomer);
                        oldCityOfCustomerCollectionNewCustomer = em.merge(oldCityOfCustomerCollectionNewCustomer);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = city.getId();
                if (findCity(id) == null) {
                    throw new NonexistentEntityException("The city with id " + id + " no longer exists.");
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
            City city;
            try {
                city = em.getReference(City.class, id);
                city.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The city with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Device> deviceCollectionOrphanCheck = city.getDeviceCollection();
            for (Device deviceCollectionOrphanCheckDevice : deviceCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This City (" + city + ") cannot be destroyed since the Device " + deviceCollectionOrphanCheckDevice + " in its deviceCollection field has a non-nullable city field.");
            }
            Collection<Customer> customerCollectionOrphanCheck = city.getCustomerCollection();
            for (Customer customerCollectionOrphanCheckCustomer : customerCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This City (" + city + ") cannot be destroyed since the Customer " + customerCollectionOrphanCheckCustomer + " in its customerCollection field has a non-nullable city field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(city);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<City> findCityEntities() {
        return findCityEntities(true, -1, -1);
    }

    public List<City> findCityEntities(int maxResults, int firstResult) {
        return findCityEntities(false, maxResults, firstResult);
    }

    private List<City> findCityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(City.class));
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

    public City findCity(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(City.class, id);
        } finally {
            em.close();
        }
    }
    
    public List<String> listUniqueName() {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("City.listByUniqueName");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<City> findByDesc(String desc) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("City.findByDesc");
            query.setParameter("description", desc);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public int getCityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<City> rt = cq.from(City.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}