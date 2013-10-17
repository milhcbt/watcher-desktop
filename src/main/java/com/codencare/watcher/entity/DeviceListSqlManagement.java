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

public class DeviceListSqlManagement {
    
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
    
    public void insertDevice(DeviceListManagement pojo){
        try {
            connected();
            String sql = "Insert into device_list values("+pojo.getId()+",'"+pojo.getIp()+"',"+pojo.getRemarks()+")";
            statement.executeUpdate(sql);
            System.out.println(sql);
            closed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public ObservableList<DeviceListManagement> listDevice(){
        try {
            connected();
            ObservableList<DeviceListManagement>list = FXCollections.observableArrayList();
            ResultSet rs = statement.executeQuery("Select * from device_list");
            System.out.printf("refresh dipencet");
            while(rs.next()){
                DeviceListManagement pojo = new DeviceListManagement();
                pojo.setNo(rs.getInt(1));
                pojo.setId(rs.getString(2));
                pojo.setIp(rs.getString(3));
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
    
    
    public void updateDevice(DeviceListManagement pojo){
        try {
            connected();
            String sql = "Update device_list set device_ip='"+pojo.getIp()+"',remarks = "+pojo.getRemarks()+" Where no = "+pojo.getNo();
            statement.executeUpdate(sql);
            System.out.println(sql);
        } catch (Exception e) {
        }
        finally{
        closed();
        }
    }
    
    public void deleteDevice(String id){
        try {
            connected();
            statement.executeUpdate("Delete from device_list where device_id = "+id);
        } catch (Exception e) {
        }finally{
           closed();
        }
}
    
     public int deviceMaxID() {
        try {
            connected();
            int max = 1;
            ResultSet rs = statement.executeQuery("Select max(device_id) From device_list");
            rs.next();
            max = rs.getInt(1);
            rs.close();
            closed();
            return max + 1;
        } catch (Exception e) {
            return 0;
        }
    }
     
     public DeviceListManagement findByID(int id){
         try {
             connected();
             ResultSet rs = statement.executeQuery("Select * from device_id where no="+id);
             DeviceListManagement pojo = new DeviceListManagement();
             while(rs.next()){
                 pojo.setNo(rs.getInt(1));
                 pojo.setId(rs.getString(2));
                 pojo.setIp(rs.getString(3));
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
