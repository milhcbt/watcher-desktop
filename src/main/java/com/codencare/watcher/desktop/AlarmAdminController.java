/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.desktop;

import com.codencare.watcher.util.DataConverter;
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
    public static final String TEAM_B = "teamB";
    public static final String TEAM_A = "teamA";
    public static final String TEAM_A_LABEL ="Tim A";
    public static final String TEAM_B_LABEL ="Tim B";
    public static final String DIGIT4_TEAM = "digit4-team";
    public static final String DIGIT4_SOUND = "digit4-sound";
    public static final String DIGIT4_POPUP = "digit4-popup";
    public static final String DIGIT4_OWNER = "digit4-owner";
    public static final String DIGIT3_TEAM = "digit3-team";
    public static final String DIGIT3_SOUND = "digit3-sound";
    public static final String DIGIT3_POPUP = "digit3-popup";
    public static final String DIGIT3_OWNER = "digit3-owner";
    public static final String DIGIT2_TEAM = "digit2-team";
    public static final String DIGIT2_SOUND = "digit2-sound";
    public static final String DIGIT2_POPUP = "digit2-popup";
    public static final String DIGIT2_OWNER = "digit2-owner";
    public static final String DIGIT1_TEAM = "digit1-team";
    public static final String DIGIT1_SOUND = "digit1-sound";
    public static final String DIGIT1_POPUP = "digit1-popup";
    public static final String DIGIT1_OWNER = "digit1-owner";
 

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
        String[] listTeamA = DataConverter.wssvToArray(teamA.getText());
        String[] listTeamB = DataConverter.wssvToArray(teamB.getText());
        boolean valid = false;
        for (String s : listTeamA) {
            DataConverter.normalizePhone(s);
            valid = DataConverter.validatePhone(s);
        }
        for (String s : listTeamB) {
            DataConverter.normalizePhone(s);
            valid = DataConverter.validatePhone(s);
        }

        if (valid) {

            MainApp.defaultProps.setProperty(DIGIT1_OWNER, String.valueOf(digit1Owner.isSelected()));
            MainApp.defaultProps.setProperty(DIGIT1_POPUP, String.valueOf(digit1Popup.isSelected()));
            MainApp.defaultProps.setProperty(DIGIT1_SOUND, String.valueOf(digit1Sound.isSelected()));
            MainApp.defaultProps.setProperty(DIGIT1_TEAM, String.valueOf(digit1Team.getValue()));
            MainApp.defaultProps.setProperty(DIGIT2_OWNER, String.valueOf(digit2Owner.isSelected()));
            MainApp.defaultProps.setProperty(DIGIT2_POPUP, String.valueOf(digit2Popup.isSelected()));
            MainApp.defaultProps.setProperty(DIGIT2_SOUND, String.valueOf(digit2Sound.isSelected()));
            MainApp.defaultProps.setProperty(DIGIT2_TEAM, String.valueOf(digit2Team.getValue()));
            MainApp.defaultProps.setProperty(DIGIT3_OWNER, String.valueOf(digit3Owner.isSelected()));
            MainApp.defaultProps.setProperty(DIGIT3_POPUP, String.valueOf(digit3Popup.isSelected()));
            MainApp.defaultProps.setProperty(DIGIT3_SOUND, String.valueOf(digit3Sound.isSelected()));
            MainApp.defaultProps.setProperty(DIGIT3_TEAM, String.valueOf(digit3Team.getValue()));
            MainApp.defaultProps.setProperty(DIGIT4_OWNER, String.valueOf(digit4Owner.isSelected()));
            MainApp.defaultProps.setProperty(DIGIT4_POPUP, String.valueOf(digit4Popup.isSelected()));
            MainApp.defaultProps.setProperty(DIGIT4_SOUND, String.valueOf(digit4Sound.isSelected()));
            MainApp.defaultProps.setProperty(DIGIT4_TEAM, String.valueOf(digit4Team.getValue()));
            MainApp.defaultProps.setProperty(TEAM_A, DataConverter.listToCSV(listTeamA));
            MainApp.defaultProps.setProperty(TEAM_B, DataConverter.listToCSV(listTeamB));

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
     * sync UI with properties file
     */
    private void loadConf() {      
        digit1Owner.setSelected(Boolean.parseBoolean(MainApp.defaultProps.getProperty(DIGIT1_OWNER)));
        digit1Popup.setSelected(Boolean.parseBoolean(MainApp.defaultProps.getProperty(DIGIT1_POPUP)));
        digit1Sound.setSelected(Boolean.parseBoolean(MainApp.defaultProps.getProperty(DIGIT1_SOUND)));
        digit1Team.setValue(MainApp.defaultProps.getProperty(DIGIT1_TEAM));
        digit2Owner.setSelected(Boolean.parseBoolean(MainApp.defaultProps.getProperty(DIGIT2_OWNER)));
        digit2Popup.setSelected(Boolean.parseBoolean(MainApp.defaultProps.getProperty(DIGIT2_POPUP)));
        digit2Sound.setSelected(Boolean.parseBoolean(MainApp.defaultProps.getProperty(DIGIT2_SOUND)));
        digit2Team.setValue(MainApp.defaultProps.getProperty(DIGIT2_TEAM));
        digit3Owner.setSelected(Boolean.parseBoolean(MainApp.defaultProps.getProperty(DIGIT3_OWNER)));
        digit3Popup.setSelected(Boolean.parseBoolean(MainApp.defaultProps.getProperty(DIGIT3_POPUP)));
        digit3Sound.setSelected(Boolean.parseBoolean(MainApp.defaultProps.getProperty(DIGIT3_SOUND)));
        digit3Team.setValue(MainApp.defaultProps.getProperty(DIGIT3_TEAM));
        digit4Owner.setSelected(Boolean.parseBoolean(MainApp.defaultProps.getProperty(DIGIT4_OWNER)));
        digit4Popup.setSelected(Boolean.parseBoolean(MainApp.defaultProps.getProperty(DIGIT4_POPUP)));
        digit4Sound.setSelected(Boolean.parseBoolean(MainApp.defaultProps.getProperty(DIGIT4_SOUND)));
        digit4Team.setValue(MainApp.defaultProps.getProperty(DIGIT4_TEAM));
        teamA.setText(DataConverter.csvToNlvs(MainApp.defaultProps.getProperty(TEAM_A)));
        teamB.setText(DataConverter.csvToNlvs(MainApp.defaultProps.getProperty(TEAM_B)));
    }
}
