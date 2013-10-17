package com.codencare.watcher.desktop;

import com.codencare.watcher.desktop.DialogInsertCustomer;
import com.codencare.watcher.entity.CustomerManagement;
import com.codencare.watcher.entity.CustomerSqlManagement;
import com.codencare.watcher.desktop.CustomerUpdateViewController;
import com.codencare.watcher.desktop.CustomerUpdateModel;
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

public class CustomerViewController implements Initializable {
    
    @FXML
    private TableView<CustomerManagement> table;
    @FXML
    private TableColumn<CustomerManagement,String>nama,alamat,email,phone;
    @FXML
    private ObservableList<CustomerManagement> list = FXCollections.observableArrayList();
    CustomerSqlManagement ssql = new CustomerSqlManagement();
    @FXML
    private Button btnInsert,btnDelete,btnRefresh;
    
    @FXML
    private void insert(ActionEvent event) throws IOException{
        new DialogInsertCustomer().start(new Stage());
        list = ssql.listUser();
        table.setItems(list);
    }
    @FXML
    private void update(ActionEvent event){
        new CustomerUpdateModel().start(new Stage());
    }                                       
    @FXML
    private void refresh(ActionEvent event){
        list = ssql.listUser();
        table.setItems(list);
    }
    @FXML
    private void delete(){
        CustomerManagement pojo = table.getSelectionModel().getSelectedItem();
        ssql.deleteUser(pojo.getId());
        System.out.println(pojo.getId());
        list = ssql.listUser();
        table.setItems(list);
    }
    @FXML
    private void selectRow(MouseEvent event){
        if(event.getClickCount()==2){           
           CustomerManagement pojo = table.getSelectionModel().getSelectedItem();
            CustomerUpdateViewController.id = pojo.getId();
            System.out.println(CustomerUpdateViewController.id);
            new CustomerUpdateModel().start(new Stage());
        }                                               
    }                                           
                                                
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        
        nama.setCellValueFactory(new PropertyValueFactory<CustomerManagement,String>("nama"));
        alamat.setCellValueFactory(new PropertyValueFactory<CustomerManagement,String>("alamat"));
        phone.setCellValueFactory(new PropertyValueFactory<CustomerManagement,String>("phone"));
        email.setCellValueFactory(new PropertyValueFactory<CustomerManagement,String>("email"));        
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        list = ssql.listUser();
        table.setItems(list);
        
    }
}
