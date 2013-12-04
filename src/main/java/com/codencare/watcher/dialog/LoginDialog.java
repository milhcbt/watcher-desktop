/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.dialog;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * TODO: not used clean up or move login logic from main class here.
 * Login Dialog:
 * @author Iman L Hakim <imanlhakim at gmail.com>
 */
public class LoginDialog extends Stage {

    public LoginDialog(Stage owner, boolean modality, String title) {
        super();
        initOwner(owner);
        Modality m = modality ? Modality.APPLICATION_MODAL : Modality.NONE;
        initModality(m);
        setOpacity(.90);
        setTitle(title);
        Group root = new Group();
        Scene scene = new Scene(root, 250, 150);
        setScene(scene);
        GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(5));
        gridpane.setHgap(5);
        gridpane.setVgap(5);
        Label mainLabel = new Label("Enter User Name & Password");
        gridpane.add(mainLabel, 1, 0, 2, 1);
        Label userNameLbl = new Label("User Name: ");
        gridpane.add(userNameLbl, 0, 1);
        Label passwordLbl = new Label("Password: ");
        gridpane.add(passwordLbl, 0, 2);
// username text field
        final TextField userNameFld = new TextField("Admin");
        gridpane.add(userNameFld, 1, 1);
// password field
        final PasswordField passwordFld = new PasswordField();
        passwordFld.setText("drowssap");
        gridpane.add(passwordFld, 1, 2);
        Button login = new Button("Change");
        login.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                close();
            }
        });
        gridpane.add(login, 1, 3);
        GridPane.setHalignment(login, HPos.RIGHT);
        root.getChildren().add(gridpane);
    }

}
