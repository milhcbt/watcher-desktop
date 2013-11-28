package com.codencare.watcher.desktop;

import com.codencare.watcher.controller.CityJpaController;
import com.codencare.watcher.controller.CustomerJpaController;
import com.codencare.watcher.entity.City;
import com.codencare.watcher.entity.Customer;
import com.codencare.watcher.util.ComboPair;
import com.mytdev.javafx.scene.control.AutoCompleteTextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.log4j.Logger;

/**
 *
 * @author abah
 */
public class CustAdminController {

    private static final Logger LOGGER = Logger.getLogger(MainFXMLController.class.getName());
    private static final EntityManagerFactory EM_FACTORY = Persistence.createEntityManagerFactory("watcherDB");
    private static final CustomerJpaController CUST_JPA_CONT = new CustomerJpaController(EM_FACTORY);
    private static final CityJpaController CITY_JPA_CONT = new CityJpaController(EM_FACTORY);

    private final ObservableList<ComboPair<Long, String>> custList = FXCollections.observableArrayList();
    Customer currCust;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="addCust"
    private Button addCust; // Value injected by FXMLLoader

    @FXML // fx:id="addCustAddress"
    private TextArea addCustAddress; // Value injected by FXMLLoader

    @FXML // fx:id="addCustArea"
    private ComboBox<ComboPair<Integer, String>> addCustArea; // Value injected by FXMLLoader

    @FXML // fx:id="addCustCity"
    private ComboBox<String> addCustCity; // Value injected by FXMLLoader

    @FXML // fx:id="addCustEmail"
    private TextField addCustEmail; // Value injected by FXMLLoader

    @FXML // fx:id="addCustName"
    private TextField addCustName; // Value injected by FXMLLoader

    @FXML // fx:id="addCustPrimaryPhone"
    private TextField addCustPrimaryPhone; // Value injected by FXMLLoader

    @FXML // fx:id="addCustSecPhone"
    private TextField addCustSecPhone; // Value injected by FXMLLoader

    @FXML // fx:id="addCustZipCode"
    private ComboBox<ComboPair<Integer, String>> addCustZipCode; // Value injected by FXMLLoader

    @FXML // fx:id="custVBox"
    private VBox custVBox; // Value injected by FXMLLoader

    @FXML // fx:id="customerTable"
    private TableView<Customer> customerTable; // Value injected by FXMLLoader

    @FXML // fx:id="deleteCust"
    private Button deleteCust; // Value injected by FXMLLoader

    @FXML // fx:id="primaryPhone"
    private Label primaryPhone; // Value injected by FXMLLoader

    @FXML // fx:id="saveCust"
    private Button saveCust; // Value injected by FXMLLoader

    // Handler for Button[fx:id="addCust"] onAction
    @FXML
    void addCust(ActionEvent event) {
        currCust = null;
        clearCustInput();
        disableCustInput(false);
        deleteCust.setDisable(true);
    }

    // Handler for Button[fx:id="deleteCust"] onAction
    @FXML
    void deleteCust(ActionEvent event) {
        // handle the event here
    }

    // Handler for ComboBox[fx:id="addCustArea"] onAction
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

    // Handler for ComboBox[fx:id="addCustCity"] onAction
    @FXML
    void onCitySelected(ActionEvent event) {
        LOGGER.debug("city " + addCustCity.getValue() + " selected");
        cityUpdated(addCustCity.getValue());
    }

    // Handler for ComboBox[fx:id="addCustZipCode"] onAction
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

    // Handler for Button[fx:id="saveCust"] onAction
    @FXML
    void saveCust(ActionEvent event) {
        try {
            Customer oldCust = null;
            if (currCust != null && currCust.getId() != null) {
                oldCust = CUST_JPA_CONT.findCustomer(currCust.getId());
            } else {
                currCust = new Customer();
            }
            currCust.setAddress(addCustAddress.getText());
            City newCity = CITY_JPA_CONT.findCity(addCustArea.getValue().getKey());
            currCust.setCity(newCity);
//            currCust.setDeviceCollection(alarmedDevices);
            currCust.setEmail(addCustEmail.getText());
//            currCust.setId(port);
            currCust.setName(addCustName.getText());
            currCust.setPrimaryPhone(addCustPrimaryPhone.getText());
            currCust.setSecondaryPhone(addCustSecPhone.getText());
            boolean validInput = validateCust(currCust);
            if (validInput && oldCust != null) {
                CUST_JPA_CONT.edit(currCust);
                customerTable.getItems().set(customerTable.getItems().indexOf(oldCust), currCust);
                disableCustInput(true);
            } else if (validInput) {
                CUST_JPA_CONT.create(currCust);
                updateCustomer();
                customerTable.setItems(FXCollections.observableList(CUST_JPA_CONT.findByName(currCust.getName())));
                disableCustInput(true);
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert addCust != null : "fx:id=\"addCust\" was not injected: check your FXML file 'CustomerAdministration.fxml'.";
        assert addCustAddress != null : "fx:id=\"addCustAddress\" was not injected: check your FXML file 'CustomerAdministration.fxml'.";
        assert addCustArea != null : "fx:id=\"addCustArea\" was not injected: check your FXML file 'CustomerAdministration.fxml'.";
        assert addCustCity != null : "fx:id=\"addCustCity\" was not injected: check your FXML file 'CustomerAdministration.fxml'.";
        assert addCustEmail != null : "fx:id=\"addCustEmail\" was not injected: check your FXML file 'CustomerAdministration.fxml'.";
        assert addCustName != null : "fx:id=\"addCustName\" was not injected: check your FXML file 'CustomerAdministration.fxml'.";
        assert addCustPrimaryPhone != null : "fx:id=\"addCustPrimaryPhone\" was not injected: check your FXML file 'CustomerAdministration.fxml'.";
        assert addCustSecPhone != null : "fx:id=\"addCustSecPhone\" was not injected: check your FXML file 'CustomerAdministration.fxml'.";
        assert addCustZipCode != null : "fx:id=\"addCustZipCode\" was not injected: check your FXML file 'CustomerAdministration.fxml'.";
        assert custVBox != null : "fx:id=\"custVBox\" was not injected: check your FXML file 'CustomerAdministration.fxml'.";
        assert customerTable != null : "fx:id=\"customerTable\" was not injected: check your FXML file 'CustomerAdministration.fxml'.";
        assert deleteCust != null : "fx:id=\"deleteCust\" was not injected: check your FXML file 'CustomerAdministration.fxml'.";
        assert primaryPhone != null : "fx:id=\"primaryPhone\" was not injected: check your FXML file 'CustomerAdministration.fxml'.";
        assert saveCust != null : "fx:id=\"saveCust\" was not injected: check your FXML file 'CustomerAdministration.fxml'.";

        // Initialize your logic here: all @FXML variables will have been injected
        final AutoCompleteTextField<ComboPair<Long, String>> nameBox = new AutoCompleteTextField();
        updateCustomer();
        nameBox.setItems(custList);
        nameBox.getPopup().addEventHandler(WindowEvent.WINDOW_HIDING,
                new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent t) {
                        String name = nameBox.getSelectedData().getValue();
                        ObservableList<Customer> cl = FXCollections.observableList(CUST_JPA_CONT.findByName(name));
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
                    CityJpaController cityCont = new CityJpaController(EM_FACTORY);
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
    }

    private void updateCustomer() {

        List<Customer> cl = CUST_JPA_CONT.findCustomerEntities();
        for (Customer c : cl) {
            ComboPair p = new ComboPair(c.getId(), c.getName());
            this.custList.add(p);
        }
    }

    private boolean validateCust(Customer cust) {
        if (!cust.getAddress().matches("[\\w\\s]{2,40}")) {
            LOGGER.debug("Address invalid");
            return false;
        }
        if (!cust.getName().matches("[\\w\\s]{2,40}")) {
            LOGGER.debug("name invalid");
            return false;
        }
        if (!cust.getPrimaryPhone().matches("\\d{4,20}|\\u002B\\d{4,20}")) {
            LOGGER.debug("primary phone invalid");
            return false;
        }
        if (!cust.getSecondaryPhone().matches("\\d{4,20}|\\u002B\\d{4,20}")) {
            LOGGER.debug("secondary phone invalid");
            return false;
        }
        if (!cust.getEmail().toUpperCase().matches("\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b")) {
            LOGGER.debug("email invalid");
            return false;
        }
        return true;
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
        List<City> selectedCity = CITY_JPA_CONT.findByDesc(cityName);
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

    private void clearCustInput() {
        CityJpaController cityCont = new CityJpaController(EM_FACTORY);
        addCustAddress.setText("");

        addCustCity.getItems().addAll(cityCont.listUniqueName());
        if (addCustCity.getItems().size() > 0) {
            addCustCity.setValue(addCustCity.getItems().get(0));//FIXME: Magic number
            //        addCustZipCode.setValue(null);
            //        addCustArea.setValue(null);
        }
        addCustEmail.setText("");
        addCustName.setText("");
        addCustPrimaryPhone.setText("");
        addCustSecPhone.setText("");

    }
}
