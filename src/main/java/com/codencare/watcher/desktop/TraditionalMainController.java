/**
 * Sample Skeleton for "TraditionalMain.fxml" Controller Class You can copy and
 * paste this code into your favorite IDE
 *
 */
package com.codencare.watcher.desktop;

import com.codencare.esb.message.IMessage;
import com.codencare.esb.message.Metajasa01;
import com.codencare.esb.message.Prasimax;
import com.codencare.watcher.component.MapView;
import com.codencare.watcher.controller.CityJpaController;
import com.codencare.watcher.controller.CustomerJpaController;
import com.codencare.watcher.controller.DeviceJpaController;
import com.codencare.watcher.controller.UserJpaController;
import com.codencare.watcher.controller.exceptions.IllegalOrphanException;
import com.codencare.watcher.controller.exceptions.NonexistentEntityException;
import com.codencare.watcher.entity.City;
import com.codencare.watcher.entity.Customer;
import com.codencare.watcher.entity.Device;
import com.codencare.watcher.entity.User;
import com.codencare.watcher.esb.processor.Metajasa01Processor;
import com.codencare.watcher.esb.processor.PrasimakProcessor;
import com.codencare.watcher.util.ComboPair;
import com.mytdev.javafx.scene.control.AutoCompleteTextField;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

public class TraditionalMainController {

    private static final Logger LOGGER = Logger.getLogger(MainFXMLController.class.getName());
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("watcherDB");
    private static  CamelContext context;

    static ServerSocket server;
    static final int port = 7000;
    static final Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
    static final URL MEDIA_URL = TraditionalMainController.class.getResource("/styles/mine/audio/alarm.mp3");

    private Point2D loc;
    private List<Device> alarmedDevices;

    private MapView mapView;

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
        assert mainPane != null : "fx:id=\"mainPane\" was not injected: check your FXML file 'TraditionalMain.fxml'.";
        assert menuBar != null : "fx:id=\"menuBar\" was not injected: check your FXML file 'TraditionalMain.fxml'.";
        assert myAccount != null : "fx:id=\"myAccount\" was not injected: check your FXML file 'TraditionalMain.fxml'.";
        assert statusPane != null : "fx:id=\"statusPane\" was not injected: check your FXML file 'TraditionalMain.fxml'.";

        // Initialize your logic here: all @FXML variables will have been injected
        startServer();
        updateDevice();
        mapView = new MapView(alarmedDevices);
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
        try  {
            fxmlLoader.load();
            dlg.setContent(root);
            dlg.show();
        } catch (IOException ex) {
            LOGGER.error(ex.toString());
        }
    }

    // Handler for MenuItem[fx:id="close"] onAction
    @FXML
    void onClose(ActionEvent event) {
        try {
            context.stop();
        } catch (Exception ex) {
           LOGGER.error(ex);
        }
        ((Stage) mainPane.getScene().getWindow()).close();
    }

    // Handler for MenuItem[fx:id="myAccount"] onAction
    @FXML
    void onMyAccount(ActionEvent event
    ) {
        // handle the event here
    }

    void updateDevice() {
//          alarmedDevices = null;
        //  emf.getCache().evict(Device.class);
        alarmedDevices = (new DeviceJpaController(emf)).findAlarmedDevice();
    }

   
    private void startServer() {
        context = new DefaultCamelContext();
        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("netty:tcp://localhost:5000?sync=false&backlog=128&allowDefaultCodec=false&textline=false&delimiter=NULL")
                            .process(new Processor() {

                                @Override
                                public void process(Exchange exchng) throws Exception {
                                    final String body = exchng.getIn().getBody(String.class);
                                    IMessage msg = null;
                                    if (body.matches("[ijklIJKL]|M\\d{1,4}|N\\d{1,4}|O\\d{1,4}|P\\d{1,4}")) {
                                        msg = new Prasimax(exchng.getIn());
                                    }
                                    if (body.matches("IO[^IORST]*\\*|RST[^IORST]*\\*")) {
                                        msg = new Metajasa01(exchng.getIn());
                                    }
                                    LOGGER.debug(body);
                                    exchng.getIn().setBody(msg, IMessage.class);
                                }

                            })
                            .process(new Processor() {
                                @Override
                                public void process(final Exchange exchng) throws Exception {
                                    Platform.runLater(new Runnable() {

                                        @Override
                                        public void run() {
                                            try {
                                                IMessage msg = exchng.getIn().getBody(IMessage.class);
                                                Media media = new Media(MEDIA_URL.toString());
                                                MediaPlayer mediaPlayer = new MediaPlayer(media);
                                                mediaPlayer.setAutoPlay(true);
                                                Action response = Dialogs.create()
                                                .title("alarm")
                                                .owner(mainPane.getScene().getWindow())
                                                .message(msg.getLocalAddress().toString())
                                                //.lightweight()
                                                .showWarning();

                                                if (response == Dialog.Actions.OK) {
                                                    // ... submit user input
                                                } else {
                                                    // ... user cancelled, reset form to default
                                                }
                                            } catch (UnknownHostException ex) {
                                                LOGGER.error(ex);
                                            }
                                        }
                                    });
                                }

                            })
                            .to("log:com.codencare.watcher");
                }
            });
            context.start();
        } catch (Exception ex) {
            LOGGER.error(ex);
        }
//        try {
//            if (server == null || server.isClosed()) {
//                server = new ServerSocket(port);
//            }
//            Thread t = new Thread(task);
//            t.setDaemon(true);
//            t.start();
//        } catch (IOException ex) {
//            LOGGER.error(ex);
//        }
    }

    Task<Void> task = new Task<Void>() {
        @Override
        protected Void call() {
            // while (true) {

            //}
            return null;
        }
    };
}
