/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codencare.watcher.test;

import com.codencare.watcher.controller.CustomerJpaController;
import com.codencare.watcher.controller.DeviceJpaController;
import com.codencare.watcher.dialog.NewNodeDialog;
import com.codencare.watcher.entity.Customer;
import com.codencare.watcher.entity.Device;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author abah
 */
public class DeviceControllerTest {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("watcherDB");

    @Test
    public void findAlarmedDevice() {

        DeviceJpaController djc = new DeviceJpaController(emf);
        List<Device> list = djc.findAlarmedDevice();
        for (Device d : list) {
            assertEquals(d.getMode(),Device.MODE_ACTIVE_ON_AC);
        }
    }

    @Test
    public void insertNewDevice() {
        CustomerJpaController cjc = new CustomerJpaController(emf);
        Customer currentCustomer = cjc.findCustomer(cjc.maxId()); 
        Random rand = new Random();
        Device newDev = new Device((long)Integer.MAX_VALUE + (long)rand.nextInt(10000));
        newDev.setLocX(rand.nextInt(1000));
        newDev.setLocY(rand.nextInt(1000));
         
        newDev.setCustomer(currentCustomer);
        DeviceJpaController djc = new DeviceJpaController(emf);
        try {
            djc.create(newDev);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(NewNodeDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
