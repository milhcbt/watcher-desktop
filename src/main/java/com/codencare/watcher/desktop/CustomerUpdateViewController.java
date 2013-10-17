package com.codencare.watcher.desktop;

import com.codencare.watcher.entity.CustomerManagement;
import com.codencare.watcher.entity.CustomerSqlManagement;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CustomerUpdateViewController implements Initializable {

    @FXML
    Button btnCancel, btnInsert;
    @FXML
    private TextField nama, alamat, phone, email;
    CustomerSqlManagement ssql = new CustomerSqlManagement();
    public static Integer id;
    private CustomerManagement pojo = new CustomerManagement();
    @FXML
    private void insert(ActionEvent event) {        
        pojo.setId(id);
        pojo.setNama(nama.getText());
        pojo.setAlamat(alamat.getText());
        pojo.setPhone(phone.getText());
        pojo.setEmail(email.getText());
        ssql.updateUser(pojo);
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
         CustomerManagement pojo = ssql.findByID(id);
         System.out.println(id);
         nama.setText(pojo.getNama());
         alamat.setText(pojo.getAlamat());
         phone.setText(String.valueOf(pojo.getPhone()));
         email.setText(pojo.getEmail());
       
    }
}
