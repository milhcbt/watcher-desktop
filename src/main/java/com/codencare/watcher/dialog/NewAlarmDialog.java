/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author abah
 */
public class NewAlarmDialog extends Stage {

    public NewAlarmDialog(Stage owner, boolean modality, String title,String data) {
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
        Label mainLabel = new Label("NewAlarm");
        gridpane.add(mainLabel, 1, 0, 2, 1);
        final TextArea alarmTxt = new TextArea(data);
        gridpane.add(alarmTxt, 1,1,2,2);
        Button login = new Button("close");
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
