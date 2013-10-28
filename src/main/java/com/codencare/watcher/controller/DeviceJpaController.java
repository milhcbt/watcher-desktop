/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codencare.watcher.controller;

import com.codencare.watcher.controller.exceptions.IllegalOrphanException;
import com.codencare.watcher.controller.exceptions.NonexistentEntityException;
import com.codencare.watcher.controller.exceptions.PreexistingEntityException;
import com.codencare.watcher.desktop.MainApp;
import com.codencare.watcher.entity.Customer;
import com.codencare.watcher.entity.Device;
import com.codencare.watcher.entity.Message;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.commons.beanutils.PropertyUtils;

/**
 *
 * @author abah
 */
public class DeviceJpaController implements Serializable {

    public DeviceJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Device device) throws PreexistingEntityException, Exception {
        if (device.getMessageCollection() == null) {
            device.setMessageCollection(new ArrayList<Message>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Customer customerId = device.getCustomerId();
            if (customerId != null) {
                customerId = em.getReference(customerId.getClass(), customerId.getId());
                device.setCustomerId(customerId);
            }
            Collection<Message> attachedMessageCollection = new ArrayList<Message>();
            for (Message messageCollectionMessageToAttach : device.getMessageCollection()) {
                messageCollectionMessageToAttach = em.getReference(messageCollectionMessageToAttach.getClass(), messageCollectionMessageToAttach.getId());
                attachedMessageCollection.add(messageCollectionMessageToAttach);
            }
            device.setMessageCollection(attachedMessageCollection);
            em.persist(device);
            if (customerId != null) {
                customerId.getDeviceCollection().add(device);
                customerId = em.merge(customerId);
            }
            for (Message messageCollectionMessage : device.getMessageCollection()) {
                Device oldDeviceIdOfMessageCollectionMessage = messageCollectionMessage.getDeviceId();
                messageCollectionMessage.setDeviceId(device);
                messageCollectionMessage = em.merge(messageCollectionMessage);
                if (oldDeviceIdOfMessageCollectionMessage != null) {
                    oldDeviceIdOfMessageCollectionMessage.getMessageCollection().remove(messageCollectionMessage);
                    oldDeviceIdOfMessageCollectionMessage = em.merge(oldDeviceIdOfMessageCollectionMessage);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDevice(device.getId()) != null) {
                throw new PreexistingEntityException("Device " + device + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Device device) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Device persistentDevice = em.find(Device.class, device.getId());
            Customer customerIdOld = persistentDevice.getCustomerId();
            Customer customerIdNew = device.getCustomerId();
            Collection<Message> messageCollectionOld = persistentDevice.getMessageCollection();
            Collection<Message> messageCollectionNew = device.getMessageCollection();
            List<String> illegalOrphanMessages = null;
            for (Message messageCollectionOldMessage : messageCollectionOld) {
                if (!messageCollectionNew.contains(messageCollectionOldMessage)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Message " + messageCollectionOldMessage + " since its deviceId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (customerIdNew != null) {
                customerIdNew = em.getReference(customerIdNew.getClass(), customerIdNew.getId());
                device.setCustomerId(customerIdNew);
            }
            Collection<Message> attachedMessageCollectionNew = new ArrayList<Message>();
            for (Message messageCollectionNewMessageToAttach : messageCollectionNew) {
                messageCollectionNewMessageToAttach = em.getReference(messageCollectionNewMessageToAttach.getClass(), messageCollectionNewMessageToAttach.getId());
                attachedMessageCollectionNew.add(messageCollectionNewMessageToAttach);
            }
            messageCollectionNew = attachedMessageCollectionNew;
            device.setMessageCollection(messageCollectionNew);
            device = em.merge(device);
            if (customerIdOld != null && !customerIdOld.equals(customerIdNew)) {
                customerIdOld.getDeviceCollection().remove(device);
                customerIdOld = em.merge(customerIdOld);
            }
            if (customerIdNew != null && !customerIdNew.equals(customerIdOld)) {
                customerIdNew.getDeviceCollection().add(device);
                customerIdNew = em.merge(customerIdNew);
            }
            for (Message messageCollectionNewMessage : messageCollectionNew) {
                if (!messageCollectionOld.contains(messageCollectionNewMessage)) {
                    Device oldDeviceIdOfMessageCollectionNewMessage = messageCollectionNewMessage.getDeviceId();
                    messageCollectionNewMessage.setDeviceId(device);
                    messageCollectionNewMessage = em.merge(messageCollectionNewMessage);
                    if (oldDeviceIdOfMessageCollectionNewMessage != null && !oldDeviceIdOfMessageCollectionNewMessage.equals(device)) {
                        oldDeviceIdOfMessageCollectionNewMessage.getMessageCollection().remove(messageCollectionNewMessage);
                        oldDeviceIdOfMessageCollectionNewMessage = em.merge(oldDeviceIdOfMessageCollectionNewMessage);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = device.getId();
                if (findDevice(id) == null) {
                    throw new NonexistentEntityException("The device with id " + id + " no longer exists.");
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
            Device device;
            try {
                device = em.getReference(Device.class, id);
                device.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The device with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Message> messageCollectionOrphanCheck = device.getMessageCollection();
            for (Message messageCollectionOrphanCheckMessage : messageCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Device (" + device + ") cannot be destroyed since the Message " + messageCollectionOrphanCheckMessage + " in its messageCollection field has a non-nullable deviceId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Customer customerId = device.getCustomerId();
            if (customerId != null) {
                customerId.getDeviceCollection().remove(device);
                customerId = em.merge(customerId);
            }
            em.remove(device);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Device> findDeviceEntities() {
        return findDeviceEntities(true, -1, -1);
    }

    public List<Device> findDeviceEntities(int maxResults, int firstResult) {
        return findDeviceEntities(false, maxResults, firstResult);
    }

    private List<Device> findDeviceEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Device.class));
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

    public Device findDevice(long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Device.class, id);
        } finally {
            em.close();
        }
    }

    public int getDeviceCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Device> rt = cq.from(Device.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((BigInteger) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Device> findAlarmedDevice() {
        EntityManager em = getEntityManager();
        String template = "SELECT d FROM Device d WHERE d.resolve=-1";
        String strQuery = String.format(template);
        try {
            em.clear();
            Query query = em.createQuery(strQuery);
            return query.getResultList();
        } finally {
           if (em != null) {
                em.close();
            }
        }
    }

    public void turnOfAlarm(Device d) {
        EntityManager em = getEntityManager();

        try {
            Device ud = em.find(Device.class, d.getId());
            String alarmField = MainApp.defaultProps.getProperty("alarm-input");
            Method m = PropertyUtils.getWriteMethod(PropertyUtils.getPropertyDescriptor(d, alarmField));
            em.getTransaction().begin();
            m.invoke(ud, Device.DIGIT_ONKNOW);
            ud.setResolve(Device.RESOLVE_RESOLVED);
            em.getTransaction().commit();
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DeviceJpaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(DeviceJpaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(DeviceJpaController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
             if (em != null) {
                em.close();
            }
        }

    }
}
