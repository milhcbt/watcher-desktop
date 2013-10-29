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

public class DeviceUpdateController implements Initializable {

    @FXML
    Button btnCancel, btnInsert;
    @FXML
    private TextField deviceid, deviceip, remarks;
    DeviceSqlRegistration ssql = new DeviceSqlRegistration();
    public static Integer id;
    private DeviceRegistration pojo = new DeviceRegistration();
    @FXML
    private void insert(ActionEvent event) {        
        pojo.setId(id);
        pojo.setDeviceId(deviceid.getText());
        pojo.setDeviceIp(deviceip.getText());
        pojo.setRemarks(remarks.getText());
        ssql.updateDevice(pojo);
        Stage stage = (Stage) btnInsert.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
         DeviceRegistration pojo = ssql.findByID(id);
         System.out.println(id);
         deviceid.setText(pojo.getDeviceId());
         deviceip.setText(pojo.getDeviceIp()); 
         remarks.setText(pojo.getRemarks());
       
    }
}
