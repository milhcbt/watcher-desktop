package com.codencare.watcher.desktop;

import com.codencare.watcher.entity.CustomerManagement;
import com.codencare.watcher.entity.DeviceRegistration;
import com.codencare.watcher.entity.DeviceSqlRegistration;
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

public class DeviceRegistrationController implements Initializable {
    
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
        new DeviceInputMain().start(new Stage());
        list = ssql.listDevice();
        table.setItems(list);
    }
    @FXML
    private void update(ActionEvent event){
        new DeviceUpdateMain().start(new Stage());
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
    private void selectRow(MouseEvent event){
        if(event.getClickCount()==2){           
           DeviceRegistration pojo = table.getSelectionModel().getSelectedItem();
            DeviceUpdateController.id = pojo.getId();
//            System.out.println(DeviceUpdateControllerkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkiiiiiii jn .id);
            new DeviceUpdateMain().start(new Stage());
        }                                               
    }                                           
                                                
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        id.setCellValueFactory(new PropertyValueFactory<DeviceRegistration,Integer>("id"));
        deviceid.setCellValueFactory(new PropertyValueFactory<DeviceRegistration,String>("device_id"));
        deviceip.setCellValueFactory(new PropertyValueFactory<DeviceRegistration,String>("device_ip"));
        remarks.setCellValueFactory(new PropertyValueFactory<DeviceRegistration,String>("remarks"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        list = ssql.listDevice();
        table.setItems(list);
        
    }
}
