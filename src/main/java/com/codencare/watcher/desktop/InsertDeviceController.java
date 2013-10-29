
package com.codencare.watcher.desktop;

import com.codencare.watcher.entity.DeviceRegistration;
import com.codencare.watcher.entity.DeviceSqlRegistration;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InsertDeviceController implements Initializable {

    @FXML
    Button btnCancel,btnInsert;        
    @FXML
    private TextField deviceid,deviceip,remarks;        
    DeviceSqlRegistration ssql = new DeviceSqlRegistration();
    @FXML
    private void insert(ActionEvent event){
        DeviceRegistration pojo = new DeviceRegistration(); 
        System.out.println(ssql.userMaxID());
        pojo.setId(ssql.userMaxID());
        pojo.setDeviceId(deviceid.getText());
        pojo.setDeviceIp(deviceip.getText()); 
        pojo.setRemarks(remarks.getText());
        ssql.insertDevice(pojo);
        Stage stage = (Stage)btnInsert.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void cancel(ActionEvent event){
     Stage stage = (Stage)btnCancel.getScene().getWindow();
        stage.close();
    }
      @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }    
}
