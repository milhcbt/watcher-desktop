/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codencare.watcher.test;

import com.codencare.watcher.controller.CityJpaController;
import com.codencare.watcher.controller.CustomerJpaController;
import com.codencare.watcher.entity.City;
import com.codencare.watcher.entity.Customer;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.Test;

/**
 *
 * @author abah
 */
public class CustomerControllerTest {
     private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("watcherDB");

//    @Test
    public void createCustomer() throws Exception {
        CustomerJpaController cjc = new CustomerJpaController(emf);
        CityJpaController cityCont = new CityJpaController(emf);
        City city = cityCont.findCity(0);
        Customer customer = new Customer();
        customer.setAddress("test");
        customer.setCity(city);
//        customer.setDeviceCollection(null);
        customer.setEmail("email@mail.com");
//        customer.setId(Integer.SIZE);
        customer.setName("test");
        customer.setPrimaryPhone("2020");
        customer.setSecondaryPhone("462666");
        
        cjc.create(customer);
        
    }
}
