package com.codencare.watcher.desktop;

import com.codencare.watcher.entity.UserManagement;
import com.codencare.watcher.controller.UserSqlManagement;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserUpdateViewController implements Initializable {

    @FXML
    Button btnCancel, btnInsert;
    @FXML
    private TextField nama, alamat, phone, email;
    UserSqlManagement ssql = new UserSqlManagement();
    public static Integer id;
    private UserManagement pojo = new UserManagement();
    @FXML
    private void insert(ActionEvent event) {        
        pojo.setid(id);
        pojo.setnama(nama.getText());
        pojo.setalamat(alamat.getText());
        pojo.setphone(Integer.parseInt(phone.getText()));
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
         UserManagement pojo = ssql.findByID(id);
         System.out.println(id);
         nama.setText(pojo.getnama());
         alamat.setText(pojo.getalamat());
         phone.setText(String.valueOf(pojo.getphone()));
         email.setText(pojo.getEmail());
       
    }
}
