/**
 * Sample Skeleton for "TraditionalMain.fxml" Controller Class You can copy and
 * paste this code into your favorite IDE
 *
 */
package com.codencare.watcher.desktop;

import com.codencare.watcher.component.MapView;
import com.codencare.watcher.controller.CustomerJpaController;
import com.codencare.watcher.controller.DeviceJpaController;
import com.codencare.watcher.controller.UserJpaController;
import com.codencare.watcher.controller.exceptions.NonexistentEntityException;
import com.codencare.watcher.entity.Customer;
import com.codencare.watcher.entity.Device;
import com.codencare.watcher.entity.User;
import java.net.ServerSocket;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.log4j.Logger;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import org.datafx.control.TableViewFactory;

public class TraditionalMainController {

    private static final Logger LOGGER = Logger.getLogger(MainFXMLController.class.getName());
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("watcherDB");

    static ServerSocket server;
    static final int port = 7000;
    static final Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();

    private ObservableList<User> ul;

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
        Dialogs.create()
                .title("About")
                .message("just a simple application\nimanlhakim@gmail.com for details")
                .showInformation();
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
        Dialog dlg = new Dialog(null, MainApp.defaultProps.getProperty("user-management"));
        CustomerJpaController cjc = new CustomerJpaController(emf);
        List<Customer> ul = cjc.findCustomerEntities();
//        TableViewFactory<User> tvfu= TableViewFactory.create(ujc.findUserEntities());
        TableView<Customer> tvu = TableViewFactory.create(Customer.class, ul)
                //                .selectColumns("Name", "Email")
                //                .renameColumn("Name", "Nama")
                //                .renameColumn("Email", "Surel")
                .buildTableView();

//        ObservableList<TableColumn<User,?>> ol = tvu.getColumns();
//        for(TableColumn<User,?> tc : ol){
//            Class dataType = tc.
//            tc.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<User,  >>() {
//
//             @Override
//                public void handle(TableColumn.CellEditEvent<User, ?> event) {
//                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                }});
//        }
        dlg.setContent(tvu);
        dlg.show();
    }

    // Handler for MenuItem[fx:id="adminUser"] onAction
    @FXML
    void onAdminUser(ActionEvent event) {
        final Dialog dlg = new Dialog(null, MainApp.defaultProps.getProperty("user-management"));
        final UserJpaController ujc = new UserJpaController(emf);
        ul =  FXCollections.observableList(ujc.findUserEntities());
        
        final TableView<User> tvu = TableViewFactory.create(User.class, ul)
                .selectColumns("Name", "Email","Type")
                .renameColumn("Name", MainApp.defaultProps.getProperty("name", "name"))
                .renameColumn("Email", MainApp.defaultProps.getProperty("email", "email"))
                .buildTableView();

        final TextField addName = new TextField();
        addName.setPromptText(MainApp.defaultProps.getProperty("add-name", "add-name"));
        final TextField addEmail = new TextField();
        addEmail.setPromptText(MainApp.defaultProps.getProperty("add-email","add-email"));
        final TextField addPass = new TextField ();
        addPass.setPromptText(MainApp.defaultProps.getProperty("password", "password"));
        final ComboBox addUserType = new ComboBox();
        addUserType.getItems().addAll(MainApp.defaultProps.getProperty("user", "user"),
                MainApp.defaultProps.getProperty("admin", "admin"));
        addUserType.setValue(MainApp.defaultProps.getProperty("user", "user"));
        final Button addButton = new Button("+");
        addButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                User newUser = new User();
                newUser.setName(addName.getText());
                newUser.setEmail(addEmail.getText());
                newUser.setPassword(addPass.getText());//TODO:important implements hashing.
                if (addUserType.getValue().equals(MainApp.defaultProps.getProperty("admin", "admin"))){
                    newUser.setType(User.TYPE_ADMIN);
                }else{
                    newUser.setType(User.TYPE_USER);
                }
               ujc.create(newUser);
               ul.add(newUser);
               LOGGER.info(newUser+" is saved");
//                ul= FXCollections.observableList( ujc.findUserEntities());
                addName.setText("");
                addEmail.setText("");
                addPass.setText("");
                addUserType.setValue(MainApp.defaultProps.getProperty("user", "user"));
            }
        });

        
        TableColumn delCol = new TableColumn<User,Boolean>();
        delCol.setCellFactory(new Callback<TableColumn<User,Boolean>,TableCell<User,Boolean>>() {
            @Override
            public TableCell<User, Boolean> call(TableColumn<User, Boolean> param) {
                return new DeleteTableCell<User,Boolean>(ul);
            }
        });
        tvu.getColumns().add(delCol);
        HBox hbox = new HBox();
        hbox.getChildren().add(addName);
        hbox.getChildren().add(addEmail);
        hbox.getChildren().add(addPass);
        hbox.getChildren().add(addUserType);
        hbox.getChildren().add(addButton);
        VBox vbox = new VBox();
        vbox.getChildren().add(tvu);
        vbox.getChildren().add(hbox);
//        tvu.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
//            // this method will be called whenever user selected row
//
//            @Override
//            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
//
//                TablePosition<User, ?> o = tvu.getEditingCell();
//                LOGGER.info("old:" + oldValue + ", new:" + newValue);
//
//            }
//        });
        ObservableList<TableColumn<User, ?>> ol = tvu.getColumns();
//        for (TableColumn tc : ol) {
//            tc.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<User, ?>>() {
//
//                @Override
//                public void handle(TableColumn.CellEditEvent<User, ?> event) {
//
//                    Dialogs.create().title(event.getNewValue().toString()).showInformation();
//                }
//            });
//        }
        dlg.setContent(vbox);
        dlg.show();
        ul = null;
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

    
    public static class DeleteTableCell<S, T> extends TableCell<S, T> {
        private final Button del;
        private final UserJpaController ujc = new UserJpaController(emf);
        private ObservableValue<T> ov;

        public DeleteTableCell(final ObservableList<?> data) {
            this.del = new Button("X");
            del.setStyle("-fx-base: red;");
            this.del.setAlignment(Pos.CENTER);
            setAlignment(Pos.CENTER);
            setGraphic(del);
            del.setOnAction(new EventHandler<ActionEvent> () {
                @Override
                public void handle(ActionEvent t) {
                    int i= getIndex();
                    User u = (User) data.get(i);
                    try {
                        ujc.destroy(u.getId());
                        data.remove(i);
                        setVisible(false);
                    } catch (NonexistentEntityException ex) {
                        LOGGER.error(ex.toString());
                    }
                }                
            });
        } 

        @Override public void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                setGraphic(del);
            }
        }
    }
}
