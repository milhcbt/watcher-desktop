package com.codencare.watcher.desktop;

import com.codencare.watcher.entity.CustomerManagement;
import com.codencare.watcher.entity.MappingManagement;
import com.codencare.watcher.entity.MappingSql;
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
import org.controlsfx.dialog.Dialogs;

public class MappingController implements Initializable {
    
    @FXML
    private TableView<MappingManagement> table;
    @FXML
    private TableColumn<MappingManagement,Integer>customer_id,locX,locY;
    @FXML
    private TableColumn<MappingManagement,String>customer;
    @FXML
    private ObservableList<MappingManagement> list = FXCollections.observableArrayList();
    MappingSql ssql = new MappingSql();
    @FXML
    private Button deleteButton;
    
    
   
    @FXML
    private void selectRow(MouseEvent event){
        if(event.getClickCount()==2){           
           MappingManagement pojo = table.getSelectionModel().getSelectedItem();
            //CustomerUpdateViewController.id = pojo.getId();
            //System.out.println(CustomerUpdateViewController.id);
            //new CustomerUpdateModel().start(new Stage());
        }                                               
    }                                           
                                                
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        
        customer_id.setCellValueFactory(new PropertyValueFactory<MappingManagement,Integer>("customer_id"));
        customer.setCellValueFactory(new PropertyValueFactory<MappingManagement,String>("customer"));
        locX.setCellValueFactory(new PropertyValueFactory<MappingManagement,Integer>("locX"));
        locY.setCellValueFactory(new PropertyValueFactory<MappingManagement,Integer>("locY"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        list = ssql.listDevice();
        table.setItems(list);
        
    }
}
