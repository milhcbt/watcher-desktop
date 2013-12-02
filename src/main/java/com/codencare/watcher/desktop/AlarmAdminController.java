package com.codencare.watcher.desktop;

/**
 * Sample Skeleton for "AlarmConfiguration.fxml" Controller Class
 * You can copy and paste this code into your favorite IDE
 **/

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import org.apache.log4j.Logger;


public class AlarmAdminController {
    private static final Logger LOGGER = Logger.getLogger(MainFXMLController.class.getName());

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="digit1Owner"
    private CheckBox digit1Owner; // Value injected by FXMLLoader

    @FXML // fx:id="digit1Popup"
    private CheckBox digit1Popup; // Value injected by FXMLLoader

    @FXML // fx:id="digit1Sound"
    private CheckBox digit1Sound; // Value injected by FXMLLoader

    @FXML // fx:id="digit1Team"
    private ComboBox<?> digit1Team; // Value injected by FXMLLoader

    @FXML // fx:id="digit2Owner"
    private CheckBox digit2Owner; // Value injected by FXMLLoader

    @FXML // fx:id="digit2Popup"
    private CheckBox digit2Popup; // Value injected by FXMLLoader

    @FXML // fx:id="digit2Sound"
    private CheckBox digit2Sound; // Value injected by FXMLLoader

    @FXML // fx:id="digit2Team"
    private ComboBox<?> digit2Team; // Value injected by FXMLLoader

    @FXML // fx:id="digit3Owner"
    private CheckBox digit3Owner; // Value injected by FXMLLoader

    @FXML // fx:id="digit3Popup"
    private CheckBox digit3Popup; // Value injected by FXMLLoader

    @FXML // fx:id="digit3Sound"
    private CheckBox digit3Sound; // Value injected by FXMLLoader

    @FXML // fx:id="digit3Team"
    private ComboBox<?> digit3Team; // Value injected by FXMLLoader

    @FXML // fx:id="digit4Owner"
    private CheckBox digit4Owner; // Value injected by FXMLLoader

    @FXML // fx:id="digit4Popup"
    private CheckBox digit4Popup; // Value injected by FXMLLoader

    @FXML // fx:id="digit4Sound"
    private CheckBox digit4Sound; // Value injected by FXMLLoader

    @FXML // fx:id="digit4Team"
    private ComboBox<?> digit4Team; // Value injected by FXMLLoader

    @FXML // fx:id="saveAlarmConf"
    private Button saveAlarmConf; // Value injected by FXMLLoader

    @FXML // fx:id="teamA"
    private TextArea teamA; // Value injected by FXMLLoader

    @FXML // fx:id="teamB"
    private TextArea teamB; // Value injected by FXMLLoader


    // Handler for Button[fx:id="saveAlarmConf"] onAction
    @FXML
    void onSaveAlarmConf(ActionEvent event) {
        MainApp.defaultProps.setProperty("digit1-owner",String.valueOf(digit1Owner.isSelected()));
        MainApp.defaultProps.setProperty("digit1-popup",String.valueOf(digit1Popup.isSelected()));
        MainApp.defaultProps.setProperty("digit1-sound",String.valueOf(digit1Sound.isSelected()));
        MainApp.defaultProps.setProperty("digit1-team",String.valueOf(digit1Team.getValue()));
        MainApp.defaultProps.setProperty("digit2-owner",String.valueOf(digit2Owner.isSelected()));
        MainApp.defaultProps.setProperty("digit2-popup",String.valueOf(digit2Popup.isSelected()));
        MainApp.defaultProps.setProperty("digit2-sound",String.valueOf(digit2Sound.isSelected()));
        MainApp.defaultProps.setProperty("digit2-team",String.valueOf(digit2Team.getValue()));
        MainApp.defaultProps.setProperty("digit3-owner",String.valueOf(digit3Owner.isSelected()));
        MainApp.defaultProps.setProperty("digit3-popup",String.valueOf(digit3Popup.isSelected()));
        MainApp.defaultProps.setProperty("digit3-sound",String.valueOf(digit3Sound.isSelected()));
        MainApp.defaultProps.setProperty("digit3-team",String.valueOf(digit3Team.getValue()));
        MainApp.defaultProps.setProperty("digit4-owner",String.valueOf(digit4Owner.isSelected()));
        MainApp.defaultProps.setProperty("digit4-popup",String.valueOf(digit4Popup.isSelected()));
        MainApp.defaultProps.setProperty("digit4-sound",String.valueOf(digit4Sound.isSelected()));
        MainApp.defaultProps.setProperty("digit4-team",String.valueOf(digit4Team.getValue()));
        try {
            LOGGER.debug("saving alarm confinguration");
            OutputStream os = new FileOutputStream("./watcher.properties");
            MainApp.defaultProps.store(os, (new Date()).toString());
            os.close();
        } catch (FileNotFoundException ex) {
            LOGGER.error(ex);
        } catch (IOException ex) {
            LOGGER.error(ex);
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert digit1Owner != null : "fx:id=\"digit1Owner\" was not injected: check your FXML file 'AlarmConfiguration.fxml'.";
        assert digit1Popup != null : "fx:id=\"digit1Popup\" was not injected: check your FXML file 'AlarmConfiguration.fxml'.";
        assert digit1Sound != null : "fx:id=\"digit1Sound\" was not injected: check your FXML file 'AlarmConfiguration.fxml'.";
        assert digit1Team != null : "fx:id=\"digit1Team\" was not injected: check your FXML file 'AlarmConfiguration.fxml'.";
        assert digit2Owner != null : "fx:id=\"digit2Owner\" was not injected: check your FXML file 'AlarmConfiguration.fxml'.";
        assert digit2Popup != null : "fx:id=\"digit2Popup\" was not injected: check your FXML file 'AlarmConfiguration.fxml'.";
        assert digit2Sound != null : "fx:id=\"digit2Sound\" was not injected: check your FXML file 'AlarmConfiguration.fxml'.";
        assert digit2Team != null : "fx:id=\"digit2Team\" was not injected: check your FXML file 'AlarmConfiguration.fxml'.";
        assert digit3Owner != null : "fx:id=\"digit3Owner\" was not injected: check your FXML file 'AlarmConfiguration.fxml'.";
        assert digit3Popup != null : "fx:id=\"digit3Popup\" was not injected: check your FXML file 'AlarmConfiguration.fxml'.";
        assert digit3Sound != null : "fx:id=\"digit3Sound\" was not injected: check your FXML file 'AlarmConfiguration.fxml'.";
        assert digit3Team != null : "fx:id=\"digit3Team\" was not injected: check your FXML file 'AlarmConfiguration.fxml'.";
        assert digit4Owner != null : "fx:id=\"digit4Owner\" was not injected: check your FXML file 'AlarmConfiguration.fxml'.";
        assert digit4Popup != null : "fx:id=\"digit4Popup\" was not injected: check your FXML file 'AlarmConfiguration.fxml'.";
        assert digit4Sound != null : "fx:id=\"digit4Sound\" was not injected: check your FXML file 'AlarmConfiguration.fxml'.";
        assert digit4Team != null : "fx:id=\"digit4Team\" was not injected: check your FXML file 'AlarmConfiguration.fxml'.";
        assert saveAlarmConf != null : "fx:id=\"saveAlarmConf\" was not injected: check your FXML file 'AlarmConfiguration.fxml'.";
        assert teamA != null : "fx:id=\"teamA\" was not injected: check your FXML file 'AlarmConfiguration.fxml'.";
        assert teamB != null : "fx:id=\"teamB\" was not injected: check your FXML file 'AlarmConfiguration.fxml'.";

        // Initialize your logic here: all @FXML variables will have been injected

    }

}
