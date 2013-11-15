/**
 * Sample Skeleton for "TraditionalMain.fxml" Controller Class You can copy and
 * paste this code into your favorite IDE
 *
 */
package com.codencare.watcher.desktop;

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
import com.codencare.watcher.util.ComboPair;
import com.mytdev.javafx.scene.control.AutoCompleteTextField;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.log4j.Logger;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

//FIXME: THIS CLASS EVERYTHING AT ONE, SHOULD SEPARATED FOR EACH FORM, AND MAKE FORM COMPONENT GENERIC
public class TraditionalMainController {

    private static final Logger LOGGER = Logger.getLogger(MainFXMLController.class.getName());
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("watcherDB");
    
    

    static ServerSocket server;
    static final int port = 7000;
    static final Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();

    private Point2D loc;
    private List<Device> alarmedDevices;

    private MapView mapView;

//    @FXML // ResourceBundle that was given to the FXMLLoader
//    private ResourceBundle resources;
//
//    @FXML // URL userForm of the FXML file that was given to the FXMLLoader
//    private URL location;
    @FXML // fx:id="mainPane"
    private ScrollPane mainPane; // Value injected by FXMLLoader

    @FXML // fx:id="statusPane"
    private FlowPane statusPane; // Value injected by FXMLLoader

    //FIXME: THIS CLASS EVERYTHING AT ONE, SHOULD SEPARATED FOR EACH FORM, AND MAKE FORM COMPONENT GENERIC
    //user form properties - start
    private User currUser;

    @FXML
    private VBox userVBox;
    @FXML
    private TextField addUserEmail;

    @FXML
    private Button addUser;

    @FXML
    private Button saveUser;

    @FXML
    private Button deleteUser;

    @FXML
    private TextField addUserName;

    @FXML
    private PasswordField addUserPassword;

    @FXML
    private ComboBox<String> addUserType;

    @FXML
    private TableView<User> userTable;
    private final ObservableList<ComboPair<Long, String>> userList = FXCollections.observableArrayList();

    //user form properties - end
    //FIXME: THIS CLASS EVERYTHING AT ONE :), SHOULD SEPARATED FOR EACH FORM, AND MAKE FORM COMPONENT GENERIC
    // customer form properties - start
    Customer currCust;

    @FXML
    private TableView<Customer> customerTable;
    private final ObservableList<ComboPair<Long, String>> custList = FXCollections.observableArrayList();

    @FXML
    private VBox custVBox;

    @FXML
    private Button addCust;

    @FXML
    private Button deleteCust;

    @FXML
    private Button saveCust;

    @FXML
    private TextArea addCustAddress;

    @FXML
    private ComboBox<ComboPair<Integer, String>> addCustArea;

    @FXML
    private ComboBox<String> addCustCity;

    @FXML
    private TextField addCustEmail;

    @FXML
    private TextField addCustName;

    @FXML
    private TextField addCustPrimaryPhone;

    @FXML
    private TextField addCustSecPhone;

    @FXML
    private ComboBox<ComboPair<Integer, String>> addCustZipCode;

    // customer form properties - end
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
        final AnchorPane root = new AnchorPane();
        Dialog dlg = new Dialog(null, MainApp.defaultProps.getProperty("customer-management"), true, true);
        final CustomerJpaController cjc = new CustomerJpaController(emf);

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/CustomerAdministration.fxml"));

        fxmlLoader.setRoot(root);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            dlg.setContent(root);
            final AutoCompleteTextField<ComboPair<Long, String>> nameBox = new AutoCompleteTextField();
            updateCustomer();
            nameBox.setItems(custList);
            nameBox.getPopup().addEventHandler(WindowEvent.WINDOW_HIDING,
                    new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent t) {
                            String name = nameBox.getSelectedData().getValue();
                            ObservableList<Customer> cl = FXCollections.observableList(cjc.findByName(name));
                            customerTable.setItems(cl);
                        }
                    });
            custVBox.getChildren().add(0, nameBox);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    nameBox.requestFocus();
                }
            });

            customerTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Customer>() {
                // this method will be called whenever user selected row

                @Override
                public void changed(ObservableValue<? extends Customer> ov, Customer oldValue, Customer newValue) {

                    // TablePosition<User, ?> o = tvu.getEditingCell();
                    LOGGER.debug("old:" + oldValue + ", new:" + newValue);
                    if (newValue != null) {
                        CityJpaController cityCont = new CityJpaController(emf);
                        currCust = newValue;
                        addCustAddress.setText(currCust.getAddress());
                        addCustCity.getItems().addAll(cityCont.listUniqueName());
                        addCustCity.setValue(currCust.getStringCity());
                        addCustEmail.setText(currCust.getEmail());
                        addCustName.setText(currCust.getName());
                        addCustPrimaryPhone.setText(currCust.getPrimaryPhone());
                        addCustSecPhone.setText(currCust.getSecondaryPhone());
                        ComboPair<Integer, String> selectedArea
                                = new ComboPair<>(
                                currCust.getCity().getId(), currCust.getArea());
                        addCustArea.setValue(selectedArea);
                        ComboPair<Integer, String> selectedZip
                                = new ComboPair<>(
                                currCust.getCity().getId(), currCust.getZipCode());
                        addCustZipCode.setValue(selectedZip);

                        disableCustInput(false);
                    }
                }
            });

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
        final UserJpaController ujc = new UserJpaController(emf);
        final AnchorPane root = new AnchorPane();
        updateUser();

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/UserAdministration.fxml"));

        fxmlLoader.setRoot(root);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            dlg.setContent(root);
            final AutoCompleteTextField<ComboPair<Long, String>> nameBox = new AutoCompleteTextField();
            nameBox.setItems(userList);//FIXME: may become problem for large user data.
            nameBox.getPopup().addEventHandler(WindowEvent.WINDOW_HIDING,
                    new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent t) {
                            String name = nameBox.getSelectedData().getValue();
                            ObservableList<User> ul = FXCollections.observableList(ujc.findByNameLike(name));
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
                    LOGGER.debug("old:" + oldValue + ", new:" + newValue);
                    if (newValue != null) {
                        currUser = newValue;
                        addUserName.setText(currUser.getName());
                        addUserEmail.setText(currUser.getEmail());
                        addUserPassword.setText(currUser.getPassword());
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
        } catch (IOException ex) {
            LOGGER.error(ex.toString());
        }

    }

    // Handler for MenuItem[fx:id="close"] onAction
    @FXML
    void onClose(ActionEvent event
            ) {
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
                    } catch (IllegalOrphanException ex) {
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

        List<Object[]> ul = ujc.listIdName();
        for (Object[] user : ul) {
            ComboPair p = new ComboPair(user[1], user[0]);///1=id, 0= name.
            this.userList.add(p);
        }
    }

    private void updateCustomer() {
        CustomerJpaController cjc = new CustomerJpaController(emf);
        List<Customer> cl = cjc.findCustomerEntities();
        for (Customer c : cl) {
            ComboPair p = new ComboPair(c.getId(), c.getName());
            this.custList.add(p);
        }
    }

    @FXML
    void addUser(ActionEvent event) {
        currUser = null;
        clearUserInput();
        disableUserInput(false);
        deleteUser.setDisable(true);
    }

    @FXML
    void saveUser(ActionEvent event) {
        try {
            UserJpaController ujc = new UserJpaController(emf);
            User oldUser = null;
            if (currUser != null && currUser.getId() != null) {
                oldUser = ujc.findUser(currUser.getId());
            } else {
                currUser = new User();
            }
            currUser.setName(addUserName.getText());
            currUser.setEmail(addUserEmail.getText());
            currUser.setPassword(addUserPassword.getText());
            if (addUserType.getValue().equalsIgnoreCase("Administrator")) {
                currUser.setType(2);
            } else {
                currUser.setType(1);
            }
            boolean validInput = validateUser(currUser);
            if (validInput && oldUser != null) {
                ujc.edit(currUser);
                userTable.getItems().set(userTable.getItems().indexOf(oldUser), currUser);
                disableUserInput(true);
            } else if (validInput) {
                ujc.create(currUser);
                updateUser();
                userTable.setItems(FXCollections.observableList(ujc.findByNameLike(currUser.getName())));
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
            if (currUser != null) {
                ujc.destroy(currUser.getId());
                disableUserInput(true);
                if (userTable.getItems() != null) {
                    userTable.getItems().remove(currUser);
                }
            }

        } catch (NonexistentEntityException ex) {
            LOGGER.error(ex.toString());
        } catch (IllegalOrphanException ex) {
            LOGGER.error(ex.toString());
        }
    }

    private void disableUserInput(boolean disable) {
        addUserName.setDisable(disable);
        addUserEmail.setDisable(disable);
        addUserPassword.setDisable(disable);
        addUserType.setDisable(disable);
        saveUser.setDisable(disable);
        deleteUser.setDisable(disable);
    }

    private void clearUserInput() {
        addUserName.setText("");
        addUserEmail.setText("");
        addUserPassword.setText("");
        addUserType.setValue("User");
    }

    private boolean validateUser(User u) {
        if (!u.getName().matches("\\w{2,40}")) {
            return false;
        }
        if (!u.getPassword().matches(".{6,20}")) {
            return false;
        }
        return u.getEmail().toUpperCase().matches("\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b");
    }

    @FXML
    void addCust(ActionEvent event) {
        currCust = null;
        clearCustInput();
        disableCustInput(false);
        deleteCust.setDisable(true);
    }

    @FXML
    void deleteCust(ActionEvent event) {
    }

    @FXML
    void saveCust(ActionEvent event) {
        try {
            CustomerJpaController cjc = new CustomerJpaController(emf);
            CityJpaController cityCont = new CityJpaController(emf);
            Customer oldCust = null;
            if (currCust != null && currCust.getId() != null) {
                oldCust = cjc.findCustomer(currCust.getId());
            } else {
                currCust = new Customer();
            }
            currCust.setAddress(addCustAddress.getText());
            City newCity = cityCont.findCity(addCustArea.getValue().getKey());
            currCust.setCity(newCity);
//            currCust.setDeviceCollection(alarmedDevices);
            currCust.setEmail(addCustEmail.getText());
//            currCust.setId(port);
            currCust.setName(addCustName.getText());
            currCust.setPrimaryPhone(addCustPrimaryPhone.getText());
            currCust.setSecondaryPhone(addCustSecPhone.getText());
            boolean validInput = validateCust(currCust);
            if (validInput && oldCust != null) {
                cjc.edit(currCust);
                customerTable.getItems().set(customerTable.getItems().indexOf(oldCust), currCust);
                disableCustInput(true);
            } else if (validInput) {
                cjc.create(currCust);
                updateCustomer();
                customerTable.setItems(FXCollections.observableList(cjc.findByName(currCust.getName())));
                disableCustInput(true);
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }
    
    private boolean validateCust(Customer cust){
        if(!cust.getAddress().matches("[\\w\\s]{2,40}")){
            return false;
        }
        if(!cust.getName().matches("[\\w\\s]{2,40}")){
            return false;
        }
        if(!cust.getPrimaryPhone().matches("\\d{4,20}|\\u002B\\d{4,20}")){
            return false;
        }
        if(!cust.getSecondaryPhone().matches("\\d{4,20}|\\u002B\\d{4,20}")){
            return false;
        }
        return cust.getEmail().toUpperCase().matches("\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b");
    }

    @FXML
    void onCitySelected(ActionEvent event) {
        LOGGER.debug("city " + addCustCity.getValue() + " selected");
        cityUpdated(addCustCity.getValue());
    }

    @FXML
    void onAreaSelected(ActionEvent event) {
        if (addCustArea != null && addCustArea.getValue() != null) {
            LOGGER.debug("area " + addCustArea.getValue().getKey()
                    + ":" + addCustArea.getValue().getValue() + " selected");
            if (addCustZipCode != null && addCustZipCode.getItems() != null) {

                List<ComboPair<Integer, String>> zipChoices
                        = addCustZipCode.getItems();
                for (ComboPair<Integer, String> pair : zipChoices) {
                    if (pair.getKey() == addCustArea.getValue().getKey()) {
                        addCustZipCode.setValue(pair);
                    }
                }
            }
        }
    }

    @FXML
    void onZipSelected(ActionEvent event) {
        if (addCustZipCode != null && addCustZipCode.getValue() != null) {
            LOGGER.debug("zip " + addCustZipCode.getValue().getKey()
                    + ":" + addCustZipCode.getValue().getValue() + " selected");
            if (addCustArea != null && addCustArea.getItems() != null) {
                List<ComboPair<Integer, String>> areaChoices
                        = addCustArea.getItems();
                for (ComboPair<Integer, String> pair : areaChoices) {
                    if (pair.getKey() == addCustZipCode.getValue().getKey()) {
                        addCustArea.setValue(pair);
                    }
                }
            }
        }
    }

    private void clearCustInput() {
        CityJpaController cityCont = new CityJpaController(emf);
        addCustAddress.setText("");

        addCustCity.getItems().addAll(cityCont.listUniqueName());
        if(addCustCity.getItems().size()>0){
            addCustCity.setValue(addCustCity.getItems().get(0));//FIXME: Magic number
            //        addCustZipCode.setValue(null);
            //        addCustArea.setValue(null);
        }
        addCustEmail.setText("");
        addCustName.setText("");
        addCustPrimaryPhone.setText("");
        addCustSecPhone.setText("");

    }

    private void disableCustInput(boolean disable) {
        addCustAddress.setDisable(disable);
        addCustArea.setDisable(disable);
        addCustCity.setDisable(disable);
        addCustEmail.setDisable(disable);
        addCustName.setDisable(disable);
        addCustPrimaryPhone.setDisable(disable);
        addCustSecPhone.setDisable(disable);
        addCustZipCode.setDisable(disable);
        saveCust.setDisable(disable);
        deleteCust.setDisable(disable);
    }

    private void cityUpdated(String cityName) {
        CityJpaController cityCont = new CityJpaController(emf);
        List<City> selectedCity = cityCont.findByDesc(cityName);
        List<ComboPair<Integer, String>> areaChoices;
        List<ComboPair<Integer, String>> zipChoices;
        if (selectedCity != null) {
            areaChoices = new ArrayList<>();
            zipChoices = new ArrayList<>();
            for (City city : selectedCity) {
                areaChoices.add(new ComboPair<>(
                        city.getId(), city.getArea()));
                zipChoices.add(new ComboPair<>(
                        city.getId(), city.getZipCode()));
            }
            addCustArea.getItems().clear();
            addCustArea.getItems().addAll(areaChoices);
            if (areaChoices.size() > 0) {
                addCustArea.setValue(areaChoices.get(0));///FIXME: Magic number
            }
            addCustZipCode.getItems().clear();
            addCustZipCode.getItems().addAll(zipChoices);
            if (zipChoices.size() > 0) {
                addCustZipCode.setValue(zipChoices.get(0));///FIXME: Magic number
            }
        }
    }
    
    private void startServer(){
        try {
            if (server == null || server.isClosed()) {
                server = new ServerSocket(port);
            }
            Thread t = new Thread(task);
            t.setDaemon(true);
            t.start();
        } catch (IOException ex) {
            LOGGER.error(ex);
        }
    }
    
    Task<Void> task = new Task<Void>() {
        @Override
        protected Void call() {
            while (true) {
                Socket sock = null;
                try {
                    sock = server.accept();
                    BufferedInputStream bis = new BufferedInputStream(sock.getInputStream());
                    Reader reader = new InputStreamReader(bis);
                    BufferedReader br = new BufferedReader(reader);
                    final StringBuilder data = new StringBuilder();
//                    do {
//                        int i = bis.read();
//                        LOGGER.info(i + "->"+(char)i);
//                        if(Character.isLetterOrDigit(i))
//                        data.append((char) i);
//                    } while (bis.available() > 0);
//                    LOGGER.info(data.toString());
                    data.append(br.readLine());
                    LOGGER.info(data.toString());
                    Platform.runLater(new Runnable() {
                        public void run() {
                            Action response = Dialogs.create()
                                    .title("alarm")
                                    .owner(mainPane.getScene().getWindow())
                                    .message(data.toString().trim())
                                    .lightweight()
                                    .showWarning();

                            if (response == Dialog.Actions.OK) {
                                // ... submit user input
                            } else {
                                // ... user cancelled, reset form to default
                            }
                        }
                    });
//                System.out.println((new Date()).toString());
//                Thread.sleep(1000);
                } catch (IOException ex) {
                    LOGGER.error(ex);
                } finally {
                    if (sock != null) {
                        try {
                            sock.close();
                        } catch (IOException ex) {
                            LOGGER.error(ex);
                        }
                    }
                }
            }
        }
    };
}
