package com.codencare.watcher.desktop;

import com.codencare.watcher.entity.DeviceListManagement;
import com.codencare.watcher.entity.DeviceListSqlManagement;
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
    private TextField id,ip,remarks;
    DeviceListSqlManagement ssql = new DeviceListSqlManagement();
    public static Integer no;
    private DeviceListManagement pojo = new DeviceListManagement();
    @FXML
    private void insert(ActionEvent event) {        
        pojo.setId(id.getText());
        pojo.setIp(ip.getText());
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
         DeviceListManagement pojo = ssql.findByID(no);
         //System.out.println(id);
         id.setText(pojo.getId());
         ip.setText(pojo.getIp());
         remarks.setText(pojo.getRemarks());
       
    }
}
