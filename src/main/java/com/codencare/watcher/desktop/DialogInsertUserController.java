
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

public class DialogInsertUserController implements Initializable {

    @FXML
    Button btnCancel,btnInsert;        
    @FXML
    private TextField nama,alamat,phone,email;        
    UserSqlManagement ssql = new UserSqlManagement();
    @FXML
    private void insert(ActionEvent event){
        UserManagement pojo = new UserManagement(); 
        System.out.println(ssql.userMaxID());
        pojo.setid(ssql.userMaxID());
        pojo.setnama(nama.getText());
        pojo.setalamat(alamat.getText());
        pojo.setphone(Integer.parseInt(phone.getText()));
        pojo.setEmail(email.getText());
        ssql.insertUser(pojo);
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
