package com.codencare.watcher.desktop;

import com.codencare.watcher.entity.CustomerManagement;
import com.codencare.watcher.entity.DeviceListManagement;
import com.codencare.watcher.entity.DeviceListSqlManagement;
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

public class DeviceListController implements Initializable {
    
    @FXML
    private TableView<DeviceListManagement> table;
    @FXML
    private TableColumn<DeviceListManagement,String>id,ip,remarks;
    @FXML
    private TableColumn<DeviceListManagement,Integer>no;
    @FXML
    private ObservableList<DeviceListManagement> list = FXCollections.observableArrayList();
    DeviceListSqlManagement ssql = new DeviceListSqlManagement();
    @FXML
    private Button btnInsert,btnDelete,btnRefresh;
    
    @FXML
    private void insert(ActionEvent event) throws IOException{
        new DialogInsertDeviceList().start(new Stage());
        list = ssql.listDevice();
        table.setItems(list);
    }
    @FXML
    private void update(ActionEvent event){
        new DeviceListUpdate().start(new Stage());
    }                                       
    @FXML
    private void refresh(ActionEvent event){
        list = ssql.listDevice();
        table.setItems(list);
    }
    @FXML
    private void delete(){
        DeviceListManagement pojo = table.getSelectionModel().getSelectedItem();
        ssql.deleteDevice(pojo.getId());
        System.out.println(pojo.getId());
        list = ssql.listDevice();
        table.setItems(list);
    }
    @FXML
    private void selectRow(MouseEvent event){
        if(event.getClickCount()==2){           
           DeviceListManagement pojo = table.getSelectionModel().getSelectedItem();
            DeviceUpdateController.no = pojo.getNo();
            //System.out.println(CustomerUpdateViewController.id);
            new DeviceListUpdate().start(new Stage());
        }                                               
    }                                           
                                                
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        no.setCellValueFactory(new PropertyValueFactory<DeviceListManagement,Integer>("no"));
        id.setCellValueFactory(new PropertyValueFactory<DeviceListManagement,String>("id"));
        ip.setCellValueFactory(new PropertyValueFactory<DeviceListManagement,String>("ip"));
        remarks.setCellValueFactory(new PropertyValueFactory<DeviceListManagement,String>("remarks"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        list = ssql.listDevice();
        table.setItems(list);
        
    }
}
