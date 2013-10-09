package com.codencare.watcher.desktop;

import com.codencare.watcher.desktop.DialogInsertUser;
import com.codencare.watcher.entity.UserManagement;
import com.codencare.watcher.controller.UserSqlManagement;
import com.codencare.watcher.desktop.UserUpdateViewController;
import com.codencare.watcher.desktop.UserUppdateModel;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class UserViewController implements Initializable {
    
    @FXML
    private TableView<UserManagement> table;
    @FXML
    private TableColumn<UserManagement,String>nama,alamat,email;
    @FXML
    private TableColumn<UserManagement,Integer>id,phone;
    @FXML
    private ObservableList<UserManagement> list = FXCollections.observableArrayList();
    UserSqlManagement ssql = new UserSqlManagement();
    @FXML
    private Button btnInsert,btnDelete,btnRefresh;
    
    @FXML
    private void insert(ActionEvent event) throws IOException{
        new DialogInsertUser().start(new Stage());
        list = ssql.listUser();
        table.setItems(list);
    }
    @FXML
    private void update(ActionEvent event){
        //new StudentUppdateModel().start(new Stage());
    }                                       
    @FXML
    private void refresh(ActionEvent event){
        list = ssql.listUser();
        table.setItems(list);
    }
    @FXML
    private void delete(){
        UserManagement pojo = table.getSelectionModel().getSelectedItem();
        ssql.deleteUser(pojo.getid());
        System.out.println(pojo.getid());
        list = ssql.listUser();
        table.setItems(list);
    }
    @FXML
    private void selectRow(MouseEvent event){
        if(event.getClickCount()==2){           
           UserManagement pojo = table.getSelectionModel().getSelectedItem();
            //StudenUpdateViewController.id = pojo.getId();
           // System.out.println(StudenUpdateViewController.id);
            new UserUppdateModel().start(new Stage());
        }                                               
    }                                           
                                                
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        
        id.setCellValueFactory(new PropertyValueFactory<UserManagement,Integer>("id"));
        nama.setCellValueFactory(new PropertyValueFactory<UserManagement,String>("nama"));
        alamat.setCellValueFactory(new PropertyValueFactory<UserManagement,String>("alamat"));
        phone.setCellValueFactory(new PropertyValueFactory<UserManagement,Integer>("phone"));
        email.setCellValueFactory(new PropertyValueFactory<UserManagement,String>("email"));        
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        list = ssql.listUser();
        table.setItems(list);
        
    }
}
