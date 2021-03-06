/**
 * Sample Skeleton for "TraditionalMain.fxml" Controller Class You can copy and
 * paste this code into your favorite IDE
 *
 */
package com.codencare.watcher.desktop;

import com.codencare.watcher.component.MapView;
import com.codencare.watcher.controller.CustomerJpaController;
import com.codencare.watcher.dialog.AlertDialog;
import java.io.IOException;
import java.net.URL;
import java.util.Stack;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.log4j.Logger;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

public class TraditionalMainController {

    private static final Logger LOGGER = Logger.getLogger(MainFXMLController.class.getName());
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("watcherDB");

//    static ServerSocket server;
//    static final int port = 7000;
    static final Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
  
    private Point2D loc;

    private static MapView mapView;

    private static Stage statView;

    @FXML // fx:id="mainPane"
    private ScrollPane mainPane; // Value injected by FXMLLoader

    @FXML // fx:id="statusPane"
    private FlowPane statusPane; // Value injected by FXMLLoader

    // menu bar - start
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

    @FXML // fx:id="myAccount"
    private MenuItem myAccount; // Value injected by FXMLLoader 

    @FXML
    private CheckMenuItem deviceStat;

    @FXML // fx:id="menuBar"
    private MenuBar menuBar; // Value injected by FXMLLoader

    // menu bar - end
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert about != null : "fx:id=\"about\" was not injected: check your FXML file 'TraditionalMain.fxml'.";
        assert adminComm != null : "fx:id=\"adminComm\" was not injected: check your FXML file 'TraditionalMain.fxml'.";
        assert adminCustomer != null : "fx:id=\"adminCustomer\" was not injected: check your FXML file 'TraditionalMain.fxml'.";
        assert adminUser != null : "fx:id=\"adminUser\" was not injected: check your FXML file 'TraditionalMain.fxml'.";
        assert alarm != null : "fx:id=\"alarm\" was not injected: check your FXML file 'TraditionalMain.fxml'.";
        assert close != null : "fx:id=\"close\" was not injected: check your FXML file 'TraditionalMain.fxml'.";
        assert deviceStat != null : "fx:id=\"deviceStat\" was not injected: check your FXML file 'TraditionalMain.fxml'.";
        assert mainPane != null : "fx:id=\"mainPane\" was not injected: check your FXML file 'TraditionalMain.fxml'.";
        assert menuBar != null : "fx:id=\"menuBar\" was not injected: check your FXML file 'TraditionalMain.fxml'.";
        assert myAccount != null : "fx:id=\"myAccount\" was not injected: check your FXML file 'TraditionalMain.fxml'.";
        assert statusPane != null : "fx:id=\"statusPane\" was not injected: check your FXML file 'TraditionalMain.fxml'.";

        // Initialize your logic here: all @FXML variables will have been injected
        mapView = new MapView();
        mapView.setId("mapView");
        mainPane.setContent(mapView);
    }
// Handler for MenuItem[fx:id="about"] onAction

    @FXML
    void onAbout(ActionEvent event) {
        Dialogs.create()
                .title("About")
                .message("just a simple application\nimanlhakim@gmail.com for details")
                .showInformation();
    }

    // Handler for MenuItem[fx:id="alarm"] onAction
    @FXML
    void onAdminAlarm(ActionEvent event) {
        final AnchorPane root = new AnchorPane();
        Dialog dlg = new Dialog(null, MainApp.defaultProps.getProperty("alarm-management"), true, true);

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/AlarmConfiguration.fxml"));

        fxmlLoader.setRoot(root);
        fxmlLoader.setController(new AlarmAdminController());
        try {
            fxmlLoader.load();
            dlg.setContent(root);
            dlg.show();
        } catch (IOException ex) {
            LOGGER.error(ex.toString());
        }
    }

    // Handler for MenuItem[fx:id="adminComm"] onAction
    @FXML
    void onAdminComm(ActionEvent event) {
        // handle the event here
    }

    // Handler for MenuItem[fx:id="adminCustomer"] onAction
    @FXML
    void onAdminCustomer(ActionEvent event) {
        final AnchorPane root = new AnchorPane();
        Dialog dlg = new Dialog(null, MainApp.defaultProps.getProperty("customer-management"), true, true);
        final CustomerJpaController cjc = new CustomerJpaController(emf);

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/CustomerAdministration.fxml"));

        fxmlLoader.setRoot(root);
        fxmlLoader.setController(new CustAdminController());
        try {
            fxmlLoader.load();
            dlg.setContent(root);
            dlg.show();
        } catch (IOException ex) {
            LOGGER.error(ex.toString());
        }
    }

    // Handler for MenuItem[fx:id="adminUser"] onAction
    @FXML
    void onAdminUser(ActionEvent event) {
        final Dialog dlg;
        dlg = new Dialog(mainPane.getScene().getWindow(), MainApp.defaultProps.getProperty("user-management"), true, true);
        final AnchorPane root = new AnchorPane();

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/UserAdministration.fxml"));

        fxmlLoader.setRoot(root);
        fxmlLoader.setController(new UserAdminController());
        try {
            fxmlLoader.load();
            dlg.setContent(root);
            dlg.show();
        } catch (IOException ex) {
            LOGGER.error(ex.toString());
        }
    }

    @FXML
    void onViewStat(ActionEvent event) {
        LOGGER.debug(deviceStat.isSelected());
        if (statView == null) {
            statView = new Stage();
            statView.initOwner(mainPane.getScene().getWindow());
            statView.setResizable(false);
            statView.initModality(Modality.NONE);
            statView.initStyle(StageStyle.TRANSPARENT);
            final AnchorPane root = new AnchorPane();

            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource("/fxml/DevicesStat.fxml"));

            root.setStyle("-fx-background-color: rgba(0, 100, 100, 0.5); -fx-background-radius: 10;");
            fxmlLoader.setRoot(root);
            fxmlLoader.setController(new DevicesStatController());
            try {
                fxmlLoader.load();
            } catch (IOException ex) {
                LOGGER.error(ex.toString());
            }

            Scene scn = new Scene(root);
            scn.setFill(Color.TRANSPARENT);

            statView.setScene(scn);
        }
        if (deviceStat.isSelected() == true) {
            statView.show();
        } else {
            statView.hide();
        }

    }

    // Handler for MenuItem[fx:id="close"] onAction
    @FXML
    void onClose(ActionEvent event
    ) {
        MainApp.stopServer();
        ((Stage) mainPane.getScene().getWindow()).close();
    }

    // Handler for MenuItem[fx:id="myAccount"] onAction
    @FXML
    void onMyAccount(ActionEvent event
    ) {
        // handle the event here
    }
}
