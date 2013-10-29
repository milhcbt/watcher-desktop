
package com.codencare.watcher.entity;

import com.mysql.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DeviceSqlRegistration {
    
    private Connection connection = null;
    private Statement statement = null;
    
    private void connected(){
        try {
            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/watcher?zeroDateTimeBehavior=convertToNull","root","");
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
    
    public void insertDevice(DeviceRegistration pojo){
        try {
            connected();
            String sql = "Insert into device_list values("+pojo.getId()+",'"+pojo.getDeviceId()+"','"+pojo.getDeviceIp()+"','"+pojo.getRemarks()+"')";
            statement.executeUpdate(sql);
            System.out.println(sql);
            closed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public ObservableList<DeviceRegistration> listDevice(){
        try {
            connected();
            ObservableList<DeviceRegistration>list = FXCollections.observableArrayList();
            ResultSet rs = statement.executeQuery("Select * from device_list");
            while(rs.next()){
                DeviceRegistration pojo = new DeviceRegistration();
                pojo.setId(rs.getInt(1));
                pojo.setDeviceId(rs.getString(2));
                pojo.setDeviceIp(rs.getString(3));
                pojo.setRemarks(rs.getString(4));
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
    
    
    public void updateDevice(DeviceRegistration pojo){
        try {
            connected();
            String sql = "Update device_list set deviceid='"+pojo.getDeviceId()+"',deviceip = '"+pojo.getDeviceIp()+"',remarks = '"+pojo.getRemarks()+"' Where id ="+pojo.getId();
            statement.executeUpdate(sql);
            System.out.println(sql);
        } catch (Exception e) {
        }
        finally{
        closed();
        }
    }
    
    public void deleteDevice(int id){
        try {
            connected();
            statement.executeUpdate("Delete from device_list where id = "+id);
        } catch (Exception e) {
        }finally{
           closed();
        }
}
    
     public int userMaxID() {
        try {
            connected();
            int max = 1;
            ResultSet rs = statement.executeQuery("Select max(id) From device_list");
            rs.next();
            max = rs.getInt(1);
            rs.close();
            closed();
            return max + 1;
        } catch (Exception e) {
            return 0;
        }
    }
     
     public DeviceRegistration findByID(int id){
         try {
             connected();
             ResultSet rs = statement.executeQuery("Select * from device_list where id="+id);
             DeviceRegistration pojo = new DeviceRegistration();
             while(rs.next()){
                 pojo.setId(rs.getInt(1));
                 pojo.setDeviceId(rs.getString(2));
                 pojo.setDeviceIp(rs.getString(3));
                 pojo.setRemarks(rs.getString(4));
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
