
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

public class CustomerSqlManagement {
    
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
    
    public void insertUser(CustomerManagement pojo){
        try {
            connected();
            String sql = "Insert into customer values("+pojo.getId()+",'"+pojo.getNama()+"','"+pojo.getAlamat()+"','"+pojo.getPhone()+"','"+pojo.getEmail()+"')";
            statement.executeUpdate(sql);
            System.out.println(sql);
            closed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public ObservableList<CustomerManagement> listUser(){
        try {
            connected();
            ObservableList<CustomerManagement>list = FXCollections.observableArrayList();
            ResultSet rs = statement.executeQuery("Select * from customer");
            System.out.printf("refresh dipencet");
            while(rs.next()){
                CustomerManagement pojo = new CustomerManagement();
                pojo.setId(rs.getInt(1));
                pojo.setNama(rs.getString(2));
                pojo.setAlamat(rs.getString(3));
                pojo.setPhone(rs.getString(4));
                pojo.setEmail(rs.getString(5));
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
    
    
    public void updateUser(CustomerManagement pojo){
        try {
            connected();
            String sql = "Update customer set nama='"+pojo.getNama()+"',alamat = '"+pojo.getAlamat()+"',phone = '"+pojo.getPhone()+"',email = '"+pojo.getEmail()+"' Where id ="+pojo.getId();
            statement.executeUpdate(sql);
            System.out.println(sql);
        } catch (Exception e) {
        }
        finally{
        closed();
        }
    }
    
    public void deleteUser(int id){
        try {
            connected();
            statement.executeUpdate("Delete from customer where id = "+id);
        } catch (Exception e) {
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
     
     public CustomerManagement findByID(int id){
         try {
             connected();
             ResultSet rs = statement.executeQuery("Select * from customer where id="+id);
             CustomerManagement pojo = new CustomerManagement();
             while(rs.next()){
                 pojo.setId(rs.getInt(1));
                 pojo.setNama(rs.getString(2));
                 pojo.setAlamat(rs.getString(3));
                 pojo.setPhone(rs.getString(4));
                 pojo.setEmail(rs.getString(5));                
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
