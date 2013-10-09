
package com.codencare.watcher.controller;

import com.codencare.watcher.entity.UserManagement;
import com.mysql.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserSqlManagement {
    
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
    
    public void insertUser(UserManagement pojo){
        try {
            connected();
            String sql = "Insert into user values("+pojo.getid()+",'"+pojo.getnama()+"','"+pojo.getalamat()+"',"+pojo.getphone()+",'"+pojo.getEmail()+"')";
            statement.executeUpdate(sql);
            System.out.println(sql);
            closed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public ObservableList<UserManagement> listUser(){
        try {
            connected();
            ObservableList<UserManagement>list = FXCollections.observableArrayList();
            ResultSet rs = statement.executeQuery("Select * from user");
            while(rs.next()){
                UserManagement pojo = new UserManagement();
                pojo.setid(rs.getInt(1));
                pojo.setnama(rs.getString(2));
                pojo.setalamat(rs.getString(3));
                pojo.setphone(rs.getInt(4));
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
    
    
    public void updateUser(UserManagement pojo){
        try {
            connected();
            String sql = "Update user set nama='"+pojo.getnama()+"',alamat = '"+pojo.getalamat()+"',phone = "+pojo.getphone()+",email = '"+pojo.getEmail()+"' Where id = "+pojo.getid();
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
            statement.executeUpdate("Delete from user where id = "+id);
        } catch (Exception e) {
        }finally{
           closed();
        }
}
    
     public int userMaxID() {
        try {
            connected();
            int max = 1;
            ResultSet rs = statement.executeQuery("Select max(id) From user");
            rs.next();
            max = rs.getInt(1);
            rs.close();
            closed();
            return max + 1;
        } catch (Exception e) {
            return 0;
        }
    }
     
     public UserManagement findByID(int id){
         try {
             connected();
             ResultSet rs = statement.executeQuery("Select * from user where id="+id);
             UserManagement pojo = new UserManagement();
             while(rs.next()){
                 pojo.setid(rs.getInt(1));
                 pojo.setnama(rs.getString(2));
                 pojo.setalamat(rs.getString(3));
                 pojo.setphone(rs.getInt(4));
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
