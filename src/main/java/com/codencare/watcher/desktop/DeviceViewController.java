package com.codencare.watcher.desktop;

import com.codencare.watcher.entity.DeviceRegistration;
import com.codencare.watcher.entity.DeviceSqlRegistration;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class DeviceViewController implements Initializable {
    
    @FXML
    private TableView<DeviceRegistration> table;
    @FXML
    private TableColumn<DeviceRegistration,String>deviceid,deviceip,remarks;
    @FXML
    private TableColumn<DeviceRegistration,Integer>id;
    @FXML
    private ObservableList<DeviceRegistration> list = FXCollections.observableArrayList();
    DeviceSqlRegistration ssql = new DeviceSqlRegistration();
    @FXML
    private Button btnInsert,btnDelete,btnRefresh;
    
    @FXML
    private void insert(ActionEvent event) throws IOException{
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/DeviceRegistration.fxml"));        
        Scene scene = new Scene(root);        
        primaryStage.setScene(scene);
        primaryStage.show();
        list = ssql.listDevice();
        table.setItems(list);
    }
    @FXML
    private void update(ActionEvent event) throws IOException{
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/DeviceUpdate.fxml"));        
        Scene scene = new Scene(root);        
        primaryStage.setScene(scene);
        primaryStage.show();
    }                                       
    @FXML
    private void refresh(ActionEvent event){
        list = ssql.listDevice();
        table.setItems(list);
    }
    @FXML
    private void delete(){
        DeviceRegistration pojo = table.getSelectionModel().getSelectedItem();
        ssql.deleteDevice(pojo.getId());
        System.out.println(pojo.getId());
        list = ssql.listDevice();
        table.setItems(list);
    }
    @FXML
    private void selectRow(MouseEvent event) throws IOException{
        if(event.getClickCount()==2){           
           DeviceRegistration pojo = table.getSelectionModel().getSelectedItem();
            DeviceUpdateController.id = pojo.getId();
            System.out.println(DeviceUpdateController.id);
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/DeviceUpdate.fxml"));        
            Scene scene = new Scene(root);        
            primaryStage.setScene(scene);
            primaryStage.show();
        }                                               
    }                                           
                                                
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        
        id.setCellValueFactory(new PropertyValueFactory<DeviceRegistration,Integer>("id"));
        deviceid.setCellValueFactory(new PropertyValueFactory<DeviceRegistration,String>("deviceid"));
        deviceip.setCellValueFactory(new PropertyValueFactory<DeviceRegistration,String>("deviceip"));
        remarks.setCellValueFactory(new PropertyValueFactory<DeviceRegistration,String>("remarks"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        list = ssql.listDevice();
        table.setItems(list);
        
    }
}
