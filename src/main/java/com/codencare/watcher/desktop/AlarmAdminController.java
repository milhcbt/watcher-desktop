/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.desktop;

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

/**
 * Controller for Alarm Configuration
 * @author Iman L Hakim <imanlhakim at gmail.com>
 */
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
    private ComboBox<String> digit1Team; // Value injected by FXMLLoader

    @FXML // fx:id="digit2Owner"
    private CheckBox digit2Owner; // Value injected by FXMLLoader

    @FXML // fx:id="digit2Popup"
    private CheckBox digit2Popup; // Value injected by FXMLLoader

    @FXML // fx:id="digit2Sound"
    private CheckBox digit2Sound; // Value injected by FXMLLoader

    @FXML // fx:id="digit2Team"
    private ComboBox<String> digit2Team; // Value injected by FXMLLoader

    @FXML // fx:id="digit3Owner"
    private CheckBox digit3Owner; // Value injected by FXMLLoader

    @FXML // fx:id="digit3Popup"
    private CheckBox digit3Popup; // Value injected by FXMLLoader

    @FXML // fx:id="digit3Sound"
    private CheckBox digit3Sound; // Value injected by FXMLLoader

    @FXML // fx:id="digit3Team"
    private ComboBox<String> digit3Team; // Value injected by FXMLLoader

    @FXML // fx:id="digit4Owner"
    private CheckBox digit4Owner; // Value injected by FXMLLoader

    @FXML // fx:id="digit4Popup"
    private CheckBox digit4Popup; // Value injected by FXMLLoader

    @FXML // fx:id="digit4Sound"
    private CheckBox digit4Sound; // Value injected by FXMLLoader

    @FXML // fx:id="digit4Team"
    private ComboBox<String> digit4Team; // Value injected by FXMLLoader

    @FXML // fx:id="saveAlarmConf"
    private Button saveAlarmConf; // Value injected by FXMLLoader

    @FXML // fx:id="teamA"
    private TextArea teamA; // Value injected by FXMLLoader

    @FXML // fx:id="teamB"
    private TextArea teamB; // Value injected by FXMLLoader

    // Handler for Button[fx:id="saveAlarmConf"] onAction
    @FXML
    void onSaveAlarmConf(ActionEvent event) {
        String[] listTeamA = phoneList(teamA.getText());
        String[] listTeamB = phoneList(teamB.getText());
        boolean valid = false;
        for (String s : listTeamA) {
            valid = validatePhone(s);
        }
        for (String s : listTeamB) {
            valid = validatePhone(s);
        }

        if (valid) {

            MainApp.defaultProps.setProperty("digit1-owner", String.valueOf(digit1Owner.isSelected()));
            MainApp.defaultProps.setProperty("digit1-popup", String.valueOf(digit1Popup.isSelected()));
            MainApp.defaultProps.setProperty("digit1-sound", String.valueOf(digit1Sound.isSelected()));
            MainApp.defaultProps.setProperty("digit1-team", String.valueOf(digit1Team.getValue()));
            MainApp.defaultProps.setProperty("digit2-owner", String.valueOf(digit2Owner.isSelected()));
            MainApp.defaultProps.setProperty("digit2-popup", String.valueOf(digit2Popup.isSelected()));
            MainApp.defaultProps.setProperty("digit2-sound", String.valueOf(digit2Sound.isSelected()));
            MainApp.defaultProps.setProperty("digit2-team", String.valueOf(digit2Team.getValue()));
            MainApp.defaultProps.setProperty("digit3-owner", String.valueOf(digit3Owner.isSelected()));
            MainApp.defaultProps.setProperty("digit3-popup", String.valueOf(digit3Popup.isSelected()));
            MainApp.defaultProps.setProperty("digit3-sound", String.valueOf(digit3Sound.isSelected()));
            MainApp.defaultProps.setProperty("digit3-team", String.valueOf(digit3Team.getValue()));
            MainApp.defaultProps.setProperty("digit4-owner", String.valueOf(digit4Owner.isSelected()));
            MainApp.defaultProps.setProperty("digit4-popup", String.valueOf(digit4Popup.isSelected()));
            MainApp.defaultProps.setProperty("digit4-sound", String.valueOf(digit4Sound.isSelected()));
            MainApp.defaultProps.setProperty("digit4-team", String.valueOf(digit4Team.getValue()));
            MainApp.defaultProps.setProperty("teamA", listToCSV(listTeamA));
            MainApp.defaultProps.setProperty("teamB", listToCSV(listTeamB));

            LOGGER.debug("saving alarm confinguration");
            try (OutputStream os = new FileOutputStream("./watcher.properties")) {
                MainApp.defaultProps.store(os, (new Date()).toString());
                MainApp.loadConfig();//reload configuration
            } catch (FileNotFoundException ex) {
                LOGGER.error(ex);
            } catch (IOException ex) {
                LOGGER.error(ex);
            }
        } else {
            LOGGER.debug("somephone number not valid");
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
        
        loadConf();//syncronize UI with properties file
    }

    /**
     * validate phone number
     * valid number must have contry code, starts with "+"
     * @param phone
     * @return 
     */
    private boolean validatePhone(String phone) {
        return phone.matches("\\+[0-9]{6,15}");
    }

    /**
     * Make sure a phone number has country code.
     * some modem only send text to number with country code.
     * assume all number without country code is Indonesia number (+62)
     * @param phone phone number without country code.
     * @return phone number with country code.
     */
    private String normalizePhone(String phone) {
        return phone.replaceFirst("^0", "+62");
    }

    /**
     * Convert phone list in "any white space" separated value
     * usually from Text-box or TextArea 
     * into Array of Strings of phone numbers
     * @param allPhone phone number from TextAre
     * @return Array of phone number
     */
    private String[] phoneList(String allPhone) {
        String[] phoneList = allPhone.split("[\\s]");
        for (int i = 0; i < phoneList.length; i++) {
            phoneList[i] = normalizePhone(phoneList[i]);
        }

        return phoneList;
    }

    /**
     * Convert List of phone number into Come separated value.
     * @param phoneList list of phone numbers in String
     * @return phone number list in CSV format.
     */
    private String listToCSV(String[] phoneList) {
        StringBuilder sb = new StringBuilder();
        for (String s : phoneList) {
            sb.append(s);
            sb.append(',');
        }
        sb.delete(sb.lastIndexOf(","), sb.lastIndexOf(""));//remove last come
        return sb.toString();
    }
    /**
     * convert come separated value into string with "new line" separated value.
     * @param csv string with coma separated value.
     * @return string with "new line" separated value.
     */
    private String csvToList(String csv){
        return csv.replace(",", "\n");
    }

    /**
     * sync UI with properties file
     */
    private void loadConf() {      
        digit1Owner.setSelected(Boolean.parseBoolean(MainApp.defaultProps.getProperty("digit1-owner")));
        digit1Popup.setSelected(Boolean.parseBoolean(MainApp.defaultProps.getProperty("digit1-popup")));
        digit1Sound.setSelected(Boolean.parseBoolean(MainApp.defaultProps.getProperty("digit1-sound")));
        digit1Team.setValue(MainApp.defaultProps.getProperty("digit1-team"));
        digit2Owner.setSelected(Boolean.parseBoolean(MainApp.defaultProps.getProperty("digit2-owner")));
        digit2Popup.setSelected(Boolean.parseBoolean(MainApp.defaultProps.getProperty("digit2-popup")));
        digit2Sound.setSelected(Boolean.parseBoolean(MainApp.defaultProps.getProperty("digit2-sound")));
        digit2Team.setValue(MainApp.defaultProps.getProperty("digit2-team"));
        digit3Owner.setSelected(Boolean.parseBoolean(MainApp.defaultProps.getProperty("digit3-owner")));
        digit3Popup.setSelected(Boolean.parseBoolean(MainApp.defaultProps.getProperty("digit3-popup")));
        digit3Sound.setSelected(Boolean.parseBoolean(MainApp.defaultProps.getProperty("digit3-sound")));
        digit3Team.setValue(MainApp.defaultProps.getProperty("digit3-team"));
        digit4Owner.setSelected(Boolean.parseBoolean(MainApp.defaultProps.getProperty("digit4-owner")));
        digit4Popup.setSelected(Boolean.parseBoolean(MainApp.defaultProps.getProperty("digit4-popup")));
        digit4Sound.setSelected(Boolean.parseBoolean(MainApp.defaultProps.getProperty("digit4-sound")));
        digit4Team.setValue(MainApp.defaultProps.getProperty("digit4-team"));
        teamA.setText(csvToList(MainApp.defaultProps.getProperty("teamA")));
        teamB.setText(csvToList(MainApp.defaultProps.getProperty("teamB")));
    }
}
