/**
 * Sample Skeleton for "TraditionalMain.fxml" Controller Class You can copy and
 * paste this code into your favorite IDE
 *
 */
package com.codencare.watcher.desktop;

import com.codencare.watcher.component.MapView;
import com.codencare.watcher.controller.DeviceJpaController;
import com.codencare.watcher.entity.Device;
import java.net.ServerSocket;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.log4j.Logger;

public class TraditionalMainController {

    private static final Logger LOGGER = Logger.getLogger(MainFXMLController.class.getName());
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("watcherDB");

    static ServerSocket server;
    static final int port = 7000;
    static final Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();

    private Point2D loc;
    private List<Device> alarmedDevices;

    private MapView mapView;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="about"
    private MenuItem about; // Value injected by FXMLLoader

    @FXML // fx:id="adminComm"
    private MenuItem adminComm; // Value injected by FXMLLoader

    @FXML // fx:id="adminCustomer"
    private MenuItem adminCustomer; // Value injected by FXMLLoader

    @FXML // fx:id="adminUser"
    private MenuItem adminUser; // Value injected by FXMLLoader

    @FXML // fx:id="alarm"
    private MenuItem alarm; // Value injected by FXMLLoader

    @FXML // fx:id="close"
    private MenuItem close; // Value injected by FXMLLoader

    @FXML // fx:id="mainPane"
    private ScrollPane mainPane; // Value injected by FXMLLoader

    @FXML // fx:id="menuBar"
    private MenuBar menuBar; // Value injected by FXMLLoader

    @FXML // fx:id="myAccount"
    private MenuItem myAccount; // Value injected by FXMLLoader

    @FXML // fx:id="statusPane"
    private FlowPane statusPane; // Value injected by FXMLLoader

    // Handler for MenuItem[fx:id="about"] onAction
    @FXML
    void onAbout(ActionEvent event) {
        // handle the event here
    }

    // Handler for MenuItem[fx:id="alarm"] onAction
    @FXML
    void onAdminAlarm(ActionEvent event) {
        // handle the event here
    }

    // Handler for MenuItem[fx:id="adminComm"] onAction
    @FXML
    void onAdminComm(ActionEvent event) {
        // handle the event here
    }

    // Handler for MenuItem[fx:id="adminCustomer"] onAction
    @FXML
    void onAdminCustomer(ActionEvent event) {
        // handle the event here
    }

    // Handler for MenuItem[fx:id="adminUser"] onAction
    @FXML
    void onAdminUser(ActionEvent event) {
        // handle the event here
    }

    // Handler for MenuItem[fx:id="close"] onAction
    @FXML
    void onClose(ActionEvent event) {
         ((Stage) mainPane.getScene().getWindow()).close();
    }

    // Handler for MenuItem[fx:id="myAccount"] onAction
    @FXML
    void onMyAccount(ActionEvent event) {
        // handle the event here
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert about != null : "fx:id=\"about\" was not injected: check your FXML file 'TraditionalMain.fxml'.";
        assert adminComm != null : "fx:id=\"adminComm\" was not injected: check your FXML file 'TraditionalMain.fxml'.";
        assert adminCustomer != null : "fx:id=\"adminCustomer\" was not injected: check your FXML file 'TraditionalMain.fxml'.";
        assert adminUser != null : "fx:id=\"adminUser\" was not injected: check your FXML file 'TraditionalMain.fxml'.";
        assert alarm != null : "fx:id=\"alarm\" was not injected: check your FXML file 'TraditionalMain.fxml'.";
        assert close != null : "fx:id=\"close\" was not injected: check your FXML file 'TraditionalMain.fxml'.";
        assert mainPane != null : "fx:id=\"mainPane\" was not injected: check your FXML file 'TraditionalMain.fxml'.";
        assert menuBar != null : "fx:id=\"menuBar\" was not injected: check your FXML file 'TraditionalMain.fxml'.";
        assert myAccount != null : "fx:id=\"myAccount\" was not injected: check your FXML file 'TraditionalMain.fxml'.";
        assert statusPane != null : "fx:id=\"statusPane\" was not injected: check your FXML file 'TraditionalMain.fxml'.";

        // Initialize your logic here: all @FXML variables will have been injected
        updateDevice();
        mapView = new MapView(alarmedDevices);
        mapView.setId("mapView");
        mainPane.setContent(mapView);
    }

    void updateDevice() {
//          alarmedDevices = null;
        //  emf.getCache().evict(Device.class);
        alarmedDevices = (new DeviceJpaController(emf)).findAlarmedDevice();
    }

}
