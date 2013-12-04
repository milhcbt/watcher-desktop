/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.desktop;

import com.codencare.watcher.controller.UserJpaController;
import com.codencare.watcher.controller.exceptions.IllegalOrphanException;
import com.codencare.watcher.controller.exceptions.NonexistentEntityException;
import com.codencare.watcher.entity.User;
import com.codencare.watcher.util.ComboPair;
import com.mytdev.javafx.scene.control.AutoCompleteTextField;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.log4j.Logger;

/**
 * This is a controller class to manage User Administration.
 * @see   void onAdminUser(ActionEvent event) in TraditionalMainController.java
 * @author Iman L Hakim <imanlhakim at gmail.com>
 */
public class UserAdminController {

    private static final Logger LOGGER = Logger.getLogger(MainFXMLController.class.getName());
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("watcherDB");
    private static final UserJpaController ujc = new UserJpaController(emf);

    private User currUser;
    private final ObservableList<ComboPair<Long, String>> userList = FXCollections.observableArrayList();

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="addUser"
    private Button addUser; // Value injected by FXMLLoader

    @FXML // fx:id="addUserEmail"
    private TextField addUserEmail; // Value injected by FXMLLoader

    @FXML // fx:id="addUserName"
    private TextField addUserName; // Value injected by FXMLLoader

    @FXML // fx:id="addUserPassword"
    private PasswordField addUserPassword; // Value injected by FXMLLoader

    @FXML // fx:id="addUserType"
    private ComboBox<String> addUserType; // Value injected by FXMLLoader

    @FXML // fx:id="deleteUser"
    private Button deleteUser; // Value injected by FXMLLoader

    @FXML // fx:id="saveUser"
    private Button saveUser; // Value injected by FXMLLoader

    @FXML // fx:id="userTable"
    private TableView<User> userTable; // Value injected by FXMLLoader

    @FXML // fx:id="userVBox"
    private VBox userVBox; // Value injected by FXMLLoader

    // Handler for Button[fx:id="addUser"] onAction
    @FXML
    void addUser(ActionEvent event) {
        currUser = null;
        clearUserInput();
        disableUserInput(false);
        deleteUser.setDisable(true);
    }

    // Handler for Button[fx:id="deleteUser"] onAction
    @FXML
    void deleteUser(ActionEvent event) {
        try {
            if (currUser != null) {
                ujc.destroy(currUser.getId());
                disableUserInput(true);
                if (userTable.getItems() != null) {
                    userTable.getItems().remove(currUser);
                }
            }

        } catch (NonexistentEntityException | IllegalOrphanException ex) {
            LOGGER.error(ex.toString());
        }
    }

    // Handler for Button[fx:id="saveUser"] onAction
    @FXML
    void saveUser(ActionEvent event) {
        try {

            User oldUser = null;
            if (currUser != null && currUser.getId() != null) {
                oldUser = ujc.findUser(currUser.getId());
            } else {
                currUser = new User();
            }
            currUser.setName(addUserName.getText());
            currUser.setEmail(addUserEmail.getText());
            currUser.setPassword(addUserPassword.getText());
            if (addUserType.getValue().equalsIgnoreCase("Administrator")) {
                currUser.setType(2);
            } else {
                currUser.setType(1);
            }
            boolean validInput = validateUser(currUser);
            if (validInput && oldUser != null) {
                ujc.edit(currUser);
                userTable.getItems().set(userTable.getItems().indexOf(oldUser), currUser);
                disableUserInput(true);
            } else if (validInput) {
                ujc.create(currUser);
                updateUser();
                userTable.setItems(FXCollections.observableList(ujc.findByNameLike(currUser.getName())));
                disableUserInput(true);
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert addUser != null : "fx:id=\"addUser\" was not injected: check your FXML file 'UserAdministration.fxml'.";
        assert addUserEmail != null : "fx:id=\"addUserEmail\" was not injected: check your FXML file 'UserAdministration.fxml'.";
        assert addUserName != null : "fx:id=\"addUserName\" was not injected: check your FXML file 'UserAdministration.fxml'.";
        assert addUserPassword != null : "fx:id=\"addUserPassword\" was not injected: check your FXML file 'UserAdministration.fxml'.";
        assert addUserType != null : "fx:id=\"addUserType\" was not injected: check your FXML file 'UserAdministration.fxml'.";
        assert deleteUser != null : "fx:id=\"deleteUser\" was not injected: check your FXML file 'UserAdministration.fxml'.";
        assert saveUser != null : "fx:id=\"saveUser\" was not injected: check your FXML file 'UserAdministration.fxml'.";
        assert userTable != null : "fx:id=\"userTable\" was not injected: check your FXML file 'UserAdministration.fxml'.";
        assert userVBox != null : "fx:id=\"userVBox\" was not injected: check your FXML file 'UserAdministration.fxml'.";

        // Initialize your logic here: all @FXML variables will have been injected
        updateUser();
        final AutoCompleteTextField<ComboPair<Long, String>> nameBox = new AutoCompleteTextField();
        nameBox.setItems(userList);//FIXME: may become problem for large user data.
        nameBox.getPopup().addEventHandler(WindowEvent.WINDOW_HIDING,
                new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent t) {
                        String name = nameBox.getSelectedData().getValue();
                        ObservableList<User> ul = FXCollections.observableList(ujc.findByNameLike(name));
                        userTable.setItems(ul);
                    }
                });

        userVBox.getChildren().add(0, nameBox);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                nameBox.requestFocus();
            }
        });

        userTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            // this method will be called whenever user selected row

            @Override
            public void changed(ObservableValue<? extends User> ov, User oldValue, User newValue) {

                // TablePosition<User, ?> o = tvu.getEditingCell();
                LOGGER.debug("old:" + oldValue + ", new:" + newValue);
                if (newValue != null) {
                    currUser = newValue;
                    addUserName.setText(currUser.getName());
                    addUserEmail.setText(currUser.getEmail());
                    addUserPassword.setText(currUser.getPassword());
                    if (currUser.getType() == 2) {
                        addUserType.setValue("Administrator");
                    } else {
                        addUserType.setValue("User");
                    }
                    disableUserInput(false);
                }
            }
        });

    }

    private void updateUser() {
        List<Object[]> ul = ujc.listIdName();
        for (Object[] user : ul) {
            ComboPair p = new ComboPair(user[1], user[0]);///1=id, 0= name.
            this.userList.add(p);
        }
    }

    private void disableUserInput(boolean disable) {
        addUserName.setDisable(disable);
        addUserEmail.setDisable(disable);
        addUserPassword.setDisable(disable);
        addUserType.setDisable(disable);
        saveUser.setDisable(disable);
        deleteUser.setDisable(disable);
    }

    private void clearUserInput() {
        addUserName.setText("");
        addUserEmail.setText("");
        addUserPassword.setText("");
        addUserType.setValue("User");
    }

    private boolean validateUser(User u) {
        if (!u.getName().matches("\\w{2,40}")) {
            return false;
        }
        if (!u.getPassword().matches(".{6,20}")) {
            return false;
        }
        return u.getEmail().toUpperCase().matches("\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b");
    }
}
