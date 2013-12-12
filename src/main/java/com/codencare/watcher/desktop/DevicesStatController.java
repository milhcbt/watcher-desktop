/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.desktop;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 *
 * @author Iman L Hakim <imanlhakim@gmail.com>
 */
public class DevicesStatController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="onDisconnected"
    private Label onDisconnected; // Value injected by FXMLLoader

    @FXML // fx:id="onAC"
    private Label onAC; // Value injected by FXMLLoader

    @FXML // fx:id="onBattery"
    private Label onBattery; // Value injected by FXMLLoader

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert onDisconnected != null : "fx:id=\"disconnected\" was not injected: check your FXML file 'DevicesStat.fxml'.";
        assert onAC != null : "fx:id=\"onAC\" was not injected: check your FXML file 'DevicesStat.fxml'.";
        assert onBattery != null : "fx:id=\"onBattery\" was not injected: check your FXML file 'DevicesStat.fxml'.";

        // Initialize your logic here: all @FXML variables will have been injected
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
//                                Calendar time = Calendar.getInstance();
                                onBattery.setText((new Date()).toString());
                            }
                        }
                ),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }

}
