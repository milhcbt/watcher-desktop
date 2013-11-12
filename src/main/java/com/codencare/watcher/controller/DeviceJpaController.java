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
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.codencare.watcher.entity.Customer;
import com.codencare.watcher.entity.City;
import com.codencare.watcher.entity.Message;
import java.util.ArrayList;
import java.util.Collection;
import com.codencare.watcher.entity.AlarmLog;
import com.codencare.watcher.entity.Device;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author abah
 */
public class DeviceJpaController implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(DeviceJpaController.class.getName());

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
        if (device.getAlarmLogCollection() == null) {
            device.setAlarmLogCollection(new ArrayList<AlarmLog>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Customer customerId = device.getCustomer();
            if (customerId != null) {
                customerId = em.getReference(customerId.getClass(), customerId.getId());
                device.setCustomer(customerId);
            }
            City city = device.getCity();
            if (city != null) {
                city = em.getReference(city.getClass(), city.getId());
                device.setCity(city);
            }
            Collection<Message> attachedMessageCollection = new ArrayList<Message>();
            for (Message messageCollectionMessageToAttach : device.getMessageCollection()) {
                messageCollectionMessageToAttach = em.getReference(messageCollectionMessageToAttach.getClass(), messageCollectionMessageToAttach.getId());
                attachedMessageCollection.add(messageCollectionMessageToAttach);
            }
            device.setMessageCollection(attachedMessageCollection);
            Collection<AlarmLog> attachedAlarmLogCollection = new ArrayList<AlarmLog>();
            for (AlarmLog alarmLogCollectionAlarmLogToAttach : device.getAlarmLogCollection()) {
                alarmLogCollectionAlarmLogToAttach = em.getReference(alarmLogCollectionAlarmLogToAttach.getClass(), alarmLogCollectionAlarmLogToAttach.getId());
                attachedAlarmLogCollection.add(alarmLogCollectionAlarmLogToAttach);
            }
            device.setAlarmLogCollection(attachedAlarmLogCollection);
            em.persist(device);
            if (customerId != null) {
                customerId.getDeviceCollection().add(device);
                customerId = em.merge(customerId);
            }
            if (city != null) {
                city.getDeviceCollection().add(device);
                city = em.merge(city);
            }
            for (Message messageCollectionMessage : device.getMessageCollection()) {
                Device oldDeviceIdOfMessageCollectionMessage = messageCollectionMessage.getDevice();
                messageCollectionMessage.setDevice(device);
                messageCollectionMessage = em.merge(messageCollectionMessage);
                if (oldDeviceIdOfMessageCollectionMessage != null) {
                    oldDeviceIdOfMessageCollectionMessage.getMessageCollection().remove(messageCollectionMessage);
                    oldDeviceIdOfMessageCollectionMessage = em.merge(oldDeviceIdOfMessageCollectionMessage);
                }
            }
            for (AlarmLog alarmLogCollectionAlarmLog : device.getAlarmLogCollection()) {
                Device oldDeviceOfAlarmLogCollectionAlarmLog = alarmLogCollectionAlarmLog.getDevice();
                alarmLogCollectionAlarmLog.setDevice(device);
                alarmLogCollectionAlarmLog = em.merge(alarmLogCollectionAlarmLog);
                if (oldDeviceOfAlarmLogCollectionAlarmLog != null) {
                    oldDeviceOfAlarmLogCollectionAlarmLog.getAlarmLogCollection().remove(alarmLogCollectionAlarmLog);
                    oldDeviceOfAlarmLogCollectionAlarmLog = em.merge(oldDeviceOfAlarmLogCollectionAlarmLog);
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
            Customer customerIdOld = persistentDevice.getCustomer();
            Customer customerIdNew = device.getCustomer();
            City cityOld = persistentDevice.getCity();
            City cityNew = device.getCity();
            Collection<Message> messageCollectionOld = persistentDevice.getMessageCollection();
            Collection<Message> messageCollectionNew = device.getMessageCollection();
            Collection<AlarmLog> alarmLogCollectionOld = persistentDevice.getAlarmLogCollection();
            Collection<AlarmLog> alarmLogCollectionNew = device.getAlarmLogCollection();
            List<String> illegalOrphanMessages = null;
            for (Message messageCollectionOldMessage : messageCollectionOld) {
                if (!messageCollectionNew.contains(messageCollectionOldMessage)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Message " + messageCollectionOldMessage + " since its deviceId field is not nullable.");
                }
            }
            for (AlarmLog alarmLogCollectionOldAlarmLog : alarmLogCollectionOld) {
                if (!alarmLogCollectionNew.contains(alarmLogCollectionOldAlarmLog)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AlarmLog " + alarmLogCollectionOldAlarmLog + " since its device field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (customerIdNew != null) {
                customerIdNew = em.getReference(customerIdNew.getClass(), customerIdNew.getId());
                device.setCustomer(customerIdNew);
            }
            if (cityNew != null) {
                cityNew = em.getReference(cityNew.getClass(), cityNew.getId());
                device.setCity(cityNew);
            }
            Collection<Message> attachedMessageCollectionNew = new ArrayList<Message>();
            for (Message messageCollectionNewMessageToAttach : messageCollectionNew) {
                messageCollectionNewMessageToAttach = em.getReference(messageCollectionNewMessageToAttach.getClass(), messageCollectionNewMessageToAttach.getId());
                attachedMessageCollectionNew.add(messageCollectionNewMessageToAttach);
            }
            messageCollectionNew = attachedMessageCollectionNew;
            device.setMessageCollection(messageCollectionNew);
            Collection<AlarmLog> attachedAlarmLogCollectionNew = new ArrayList<AlarmLog>();
            for (AlarmLog alarmLogCollectionNewAlarmLogToAttach : alarmLogCollectionNew) {
                alarmLogCollectionNewAlarmLogToAttach = em.getReference(alarmLogCollectionNewAlarmLogToAttach.getClass(), alarmLogCollectionNewAlarmLogToAttach.getId());
                attachedAlarmLogCollectionNew.add(alarmLogCollectionNewAlarmLogToAttach);
            }
            alarmLogCollectionNew = attachedAlarmLogCollectionNew;
            device.setAlarmLogCollection(alarmLogCollectionNew);
            device = em.merge(device);
            if (customerIdOld != null && !customerIdOld.equals(customerIdNew)) {
                customerIdOld.getDeviceCollection().remove(device);
                customerIdOld = em.merge(customerIdOld);
            }
            if (customerIdNew != null && !customerIdNew.equals(customerIdOld)) {
                customerIdNew.getDeviceCollection().add(device);
                customerIdNew = em.merge(customerIdNew);
            }
            if (cityOld != null && !cityOld.equals(cityNew)) {
                cityOld.getDeviceCollection().remove(device);
                cityOld = em.merge(cityOld);
            }
            if (cityNew != null && !cityNew.equals(cityOld)) {
                cityNew.getDeviceCollection().add(device);
                cityNew = em.merge(cityNew);
            }
            for (Message messageCollectionNewMessage : messageCollectionNew) {
                if (!messageCollectionOld.contains(messageCollectionNewMessage)) {
                    Device oldDeviceIdOfMessageCollectionNewMessage = messageCollectionNewMessage.getDevice();
                    messageCollectionNewMessage.setDevice(device);
                    messageCollectionNewMessage = em.merge(messageCollectionNewMessage);
                    if (oldDeviceIdOfMessageCollectionNewMessage != null && !oldDeviceIdOfMessageCollectionNewMessage.equals(device)) {
                        oldDeviceIdOfMessageCollectionNewMessage.getMessageCollection().remove(messageCollectionNewMessage);
                        oldDeviceIdOfMessageCollectionNewMessage = em.merge(oldDeviceIdOfMessageCollectionNewMessage);
                    }
                }
            }
            for (AlarmLog alarmLogCollectionNewAlarmLog : alarmLogCollectionNew) {
                if (!alarmLogCollectionOld.contains(alarmLogCollectionNewAlarmLog)) {
                    Device oldDeviceOfAlarmLogCollectionNewAlarmLog = alarmLogCollectionNewAlarmLog.getDevice();
                    alarmLogCollectionNewAlarmLog.setDevice(device);
                    alarmLogCollectionNewAlarmLog = em.merge(alarmLogCollectionNewAlarmLog);
                    if (oldDeviceOfAlarmLogCollectionNewAlarmLog != null && !oldDeviceOfAlarmLogCollectionNewAlarmLog.equals(device)) {
                        oldDeviceOfAlarmLogCollectionNewAlarmLog.getAlarmLogCollection().remove(alarmLogCollectionNewAlarmLog);
                        oldDeviceOfAlarmLogCollectionNewAlarmLog = em.merge(oldDeviceOfAlarmLogCollectionNewAlarmLog);
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

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
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
            Collection<AlarmLog> alarmLogCollectionOrphanCheck = device.getAlarmLogCollection();
            for (AlarmLog alarmLogCollectionOrphanCheckAlarmLog : alarmLogCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Device (" + device + ") cannot be destroyed since the AlarmLog " + alarmLogCollectionOrphanCheckAlarmLog + " in its alarmLogCollection field has a non-nullable device field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Customer customerId = device.getCustomer();
            if (customerId != null) {
                customerId.getDeviceCollection().remove(device);
                customerId = em.merge(customerId);
            }
            City city = device.getCity();
            if (city != null) {
                city.getDeviceCollection().remove(device);
                city = em.merge(city);
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

    public Device findDevice(Long id) {
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
            return ((Long) q.getSingleResult()).intValue();
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
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
           LOGGER.error(ex.toString());
        } finally {
             if (em != null) {
                em.close();
            }
        }

    }
}
