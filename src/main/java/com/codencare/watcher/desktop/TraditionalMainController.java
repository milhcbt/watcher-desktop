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
import com.codencare.watcher.util.ComboPair;
import com.mytdev.javafx.scene.control.AutoCompleteTextField;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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

//    private ObservableList<User> ul;

    private Point2D loc;
    private List<Device> alarmedDevices;

    private MapView mapView;
    private ObservableList<ComboPair<Long, String>> userList;

    private User currUser;

    @FXML
    private VBox userVBox;
    @FXML
    private TextField addEmail;

    @FXML
    private Button addUser;

    @FXML
    private Button saveUser;

    @FXML
    private Button deleteUser;

    @FXML
    private TextField addName;

    @FXML
    private PasswordField addPassword;

    @FXML
    private ComboBox<String> addUserType;

    @FXML
    private TableView userTable;

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
        final Dialog dlg = new Dialog(mainPane.getScene().getWindow(), MainApp.defaultProps.getProperty("user-management"), true, true);
        final UserJpaController ujc = new UserJpaController(emf);
        final AnchorPane root = new AnchorPane();
        updateUser();
//        ul = FXCollections.observableList(ujc.findUserEntities());

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/UserAdministration.fxml"));

        fxmlLoader.setRoot(root);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
//            userTable.setItems(ul);
            dlg.setContent(root);
            final AutoCompleteTextField<ComboPair<Long, String>> nameBox = new AutoCompleteTextField();
            nameBox.setItems(userList);
            nameBox.getPopup().addEventHandler(WindowEvent.WINDOW_HIDING,
                    new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent t) {
                            String name = nameBox.getSelectedData().getValue();
                            ObservableList<User> ul = FXCollections.observableList(ujc.findByName(name));
                            userTable.setItems(ul);
                        }
                    });

            userVBox.getChildren().add(0, nameBox);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    nameBox.requestFocus();
                }
            });

            userTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
                // this method will be called whenever user selected row

                @Override
                public void changed(ObservableValue<? extends User> ov, User oldValue, User newValue) {

                    // TablePosition<User, ?> o = tvu.getEditingCell();
                    LOGGER.info("old:" + oldValue + ", new:" + newValue);
                    if (newValue != null) {
                        currUser = newValue;
                        addName.setText(currUser.getName());
                        addEmail.setText(currUser.getEmail());
                        addPassword.setText(currUser.getPassword());
                        if (currUser.getType() == 2) {
                            addUserType.setValue("Administrator");
                        } else {
                            addUserType.setValue("User");
                        }
                        disableUserInput(false);
                    }
                }
            });
            dlg.show();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

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
            del.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    int i = getIndex();
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

        @Override
        public void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                setGraphic(del);
            }
        }
    }

    private void updateUser() {
        UserJpaController ujc = new UserJpaController(emf);
        userList = FXCollections.observableArrayList();
        List<User> userList = ujc.findUserEntities();
        for (User user : userList) {
            ComboPair p = new ComboPair(user.getId(), user.getName());
            this.userList.add(p);
        }
    }

    @FXML
    void addUser(ActionEvent event) {
        currUser = null;
        clearUserInput();
        disableUserInput(false);
    }

    @FXML
    void saveUser(ActionEvent event) {
        try {
            UserJpaController ujc = new UserJpaController(emf);
            User oldUser = null;
            if (currUser != null) {
                oldUser = ujc.findUser(currUser.getId());
            } else {
                currUser = new User();
            }
            currUser.setName(addName.getText());
            currUser.setEmail(addEmail.getText());
            currUser.setPassword(addPassword.getText());
            if (addUserType.getValue().equalsIgnoreCase("Administrator")) {
                currUser.setType(2);
            } else {
                currUser.setType(1);
            }

            if (oldUser != null && validateUser(currUser)) {
                ujc.edit(currUser);
                userTable.getItems().set(userTable.getItems().indexOf(oldUser), currUser);
                 disableUserInput(true);
            } else  if (validateUser(currUser)){
                ujc.create(currUser);
                updateUser();
                userTable.setItems(FXCollections.observableList(ujc.findByName(currUser.getName())));
                 disableUserInput(true);
            }           
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }

    @FXML
    void deleteUser(ActionEvent event) {
        try {
            UserJpaController ujc = new UserJpaController(emf);
            if(currUser !=null){
                ujc.destroy(currUser.getId());
                disableUserInput(true);
                if (userTable.getItems() != null){
                    userTable.getItems().remove(currUser);
                }
            }
            
        } catch (NonexistentEntityException ex) {
            LOGGER.error(ex.toString());
        }
    }

    private void disableUserInput(boolean disable) {
        addName.setDisable(disable);
        addEmail.setDisable(disable);
        addPassword.setDisable(disable);
        addUserType.setDisable(disable);
        saveUser.setDisable(disable);
        deleteUser.setDisable(disable);
    }

    private void clearUserInput() {
        addName.setText("");
        addEmail.setText("");
        addPassword.setText("");
        addUserType.setValue("User");
    }
    
    private boolean validateUser (User u){
        if(!u.getName().matches("\\w{2,40}")){
            return false;
        }
        if(!u.getPassword().matches("\\w{6,20}")){
            return false;
        }
        if(!u.getEmail().toUpperCase().matches("\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b")){
            return false;
        }
        return true;
    }
}
