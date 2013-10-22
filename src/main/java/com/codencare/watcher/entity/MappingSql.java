
package com.codencare.watcher.entity;

import com.codencare.watcher.entity.CustomerManagement;
import com.mysql.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MappingSql {
    
    private Connection connection = null;
    private Statement statement = null;
    
    private void connected(){
        try {
            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/watcher?zeroDateTimeBehavior=convertToNull","root","12345");
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void closed(){
        try {
            connection.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();            
        }
    }
    
    
    public ObservableList<MappingManagement> listDevice(){
        try {
            connected();
            ObservableList<MappingManagement>list = FXCollections.observableArrayList();
            ResultSet rs = statement.executeQuery("SELECT device.customer_id,customer.nama,device.locX,device.locY FROM device join customer on device.customer_id=customer.id");
            System.out.printf("refresh dipencet");
            while(rs.next()){
                MappingManagement pojo = new MappingManagement();
                pojo.setId_cus(rs.getInt(1));
                pojo.setName(rs.getString(2));
                pojo.setLocX(rs.getInt(3));
                pojo.setLocY(rs.getInt(4));
                list.add(pojo);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally{
        closed();
        }        
    }
    
    
     public int userMaxID() {
        try {
            connected();
            int max = 1;
            ResultSet rs = statement.executeQuery("Select max(id) From customer");
            rs.next();
            max = rs.getInt(1);
            rs.close();
            closed();
            return max + 1;
        } catch (Exception e) {
            return 0;
        }
    }
     
     public MappingManagement findByID(int id){
         try {
             connected();
             ResultSet rs = statement.executeQuery("Select customer_id,locX,locY from device where id="+id);
             MappingManagement pojo = new MappingManagement();
             while(rs.next()){
                 pojo.setName(rs.getString(1));
                 pojo.setLocX(rs.getInt(2));
                 pojo.setLocY(rs.getInt(3));
             }
             return pojo;
         } catch (Exception e) {
             e.printStackTrace();
             return null;
         }finally{
         closed();
         }
     }

}
