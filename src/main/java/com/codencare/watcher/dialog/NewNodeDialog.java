/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codencare.watcher.dialog;

import com.codencare.watcher.controller.CustomerJpaController;
import com.codencare.watcher.controller.DeviceJpaController;
import com.codencare.watcher.entity.Customer;
import com.codencare.watcher.entity.Device;
import com.codencare.watcher.util.ComboPair;
import com.codencare.watcher.util.DataConverter;
import com.mytdev.javafx.scene.control.AutoCompleteTextField;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.log4j.Logger;

/**
 *
 * @author abah
 */
public class NewNodeDialog extends Stage {

    private static final Logger LOGGER = Logger.getLogger(NewNodeDialog.class.getName());
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("watcherDB");
    private static final CustomerJpaController cjc = new CustomerJpaController(emf);

    private Customer currentCustomer;
    private Point2D currentLoc;
    private TextField namaTxt;
    private TextField alamatTxt;
    private TextField telpTxt;
    private TextField emailTxt;
    private ObservableList<ComboPair<Long, String>> data; 

    public NewNodeDialog(Stage owner, boolean modality, String title, Point2D location) {
        super();
        currentLoc = location;
        //SAMPLE DATA
        
        updateData();

        
        initOwner(owner);
        Modality m = modality ? Modality.APPLICATION_MODAL : Modality.NONE;
        initModality(m);
        setOpacity(.90);
        setTitle(title);
        final Group root = new Group();
        Scene scene = new Scene(root, 300, 400);
        setScene(scene);
        GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(5));
        gridpane.setHgap(5);
        gridpane.setVgap(5);

        Label mainLabel = new Label("New Device Info");
        gridpane.add(mainLabel, 0, 0, Integer.MAX_VALUE, 1);

        Label custLabel = new Label("Customer");
        gridpane.add(custLabel, 0, 1);

        Label namaCustLbl = new Label("Nama: ");
        gridpane.add(namaCustLbl, 0, 2);

        namaTxt = new TextField();
        namaTxt.setVisible(false);
        gridpane.add(namaTxt, 1, 2);

        final AutoCompleteTextField<ComboPair<Long, String>> nameBox = new AutoCompleteTextField();
        nameBox.setItems(data);
        nameBox.getPopup().addEventHandler(WindowEvent.WINDOW_HIDING,
                new EventHandler<WindowEvent>() {
                @Override
                    public void handle(WindowEvent t) {
                        namaTxt.setText(nameBox.getText());
                        currentCustomer = cjc.findCustomer(nameBox.getSelectedData().getKey());
                        alamatTxt.setText(currentCustomer.getAlamat());
                        telpTxt.setText(currentCustomer.getPhone());
                        emailTxt.setText(currentCustomer.getEmail());
                    }
                });
        gridpane.add(nameBox, 1, 2);

        Button newCusButton = new Button("+");
        newCusButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Button btn;
                if (event.getSource() instanceof Button) {
                    btn = (Button) event.getSource();
                    if (btn.getText().equalsIgnoreCase("+")) {
                        btn.setText("save");
                        nameBox.setVisible(false);
                        nameBox.setText("");
                        namaTxt.setVisible(true);
                        namaTxt.setText("");
                        alamatTxt.setDisable(false);
                        alamatTxt.setText("");
                        telpTxt.setDisable(false);
                        telpTxt.setText("");
                        emailTxt.setDisable(false);
                        emailTxt.setText("");

                    } else {
                        long newId = cjc.maxId() + 1;
                        Customer newCust = new Customer(newId);
                        newCust.setNama(namaTxt.getText());
                        newCust.setAlamat(alamatTxt.getText());
                        newCust.setPhone(telpTxt.getText());
                        newCust.setEmail(emailTxt.getText());
                        cjc.create(newCust);
                        updateData();
                        currentCustomer = newCust;
                        btn.setText("+");
                        nameBox.setVisible(true);
                        nameBox.setText("");
                        namaTxt.setVisible(false);
                        namaTxt.setText("");
                        alamatTxt.setDisable(true);
                        alamatTxt.setText("");
                        telpTxt.setDisable(true);
                        telpTxt.setText("");
                        emailTxt.setDisable(true);
                        emailTxt.setText("");
                    }
                }
            }
        });
        gridpane.add(newCusButton, 2, 2);

        Label alamatLbl = new Label("Alamat: ");
        gridpane.add(alamatLbl, 0, 3);

        alamatTxt = new TextField("alamat");
        alamatTxt.setDisable(true);
        gridpane.add(alamatTxt, 1, 3, 2, 1);

        Label telpLbl = new Label("Telp");
        gridpane.add(telpLbl, 0, 4);

        telpTxt = new TextField("telepon");
        telpTxt.setDisable(true);
        gridpane.add(telpTxt, 1, 4, 2, 1);

        Label emailLbl = new Label("email");
        gridpane.add(emailLbl, 0, 5);

        emailTxt = new TextField("e-mail");
        emailTxt.setDisable(true);
        gridpane.add(emailTxt, 1, 5, 2, 1);

        Label deviceLbl = new Label("Device");
        gridpane.add(deviceLbl, 0, 6, Integer.MAX_VALUE, 1);

        Label posLabel = new Label("X: " + location.getX() + "   Y: " + location.getY());
        gridpane.add(posLabel, 0, 7, Integer.MAX_VALUE, 1);

        Label ipLbl = new Label("IP");
        gridpane.add(ipLbl, 0, 8);

        final TextField ipTxt = new TextField("Ip Device");
        gridpane.add(ipTxt, 1, 8);

        Button newDevice = new Button("save Device");
        gridpane.add(newDevice, 1, 9,Integer.MAX_VALUE,1);
        newDevice.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                long newIp = 0;
                try {
                    InetAddress ip = InetAddress.getByName(ipTxt.getText());
                    newIp = DataConverter.bytesToLong(ip.getAddress());
                } catch (UnknownHostException ex) {
                    java.util.logging.Logger.getLogger(NewNodeDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (newIp != 0) {
                    Device newDev = new Device(BigInteger.valueOf(newIp));
                    newDev.setLocX((int) currentLoc.getX());
                    newDev.setLocY((int) currentLoc.getY());
                    newDev.setCustomerId(currentCustomer);
                    DeviceJpaController djc = new DeviceJpaController(emf);
                    try {
                        djc.create(newDev);
                    } catch (Exception ex) {
                        java.util.logging.Logger.getLogger(NewNodeDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                 ((Stage) root.getScene().getWindow()).close();
            }
        });
        root.getChildren().add(gridpane);
    }
    
    private void updateData(){
        data = FXCollections.observableArrayList();
        List<Customer> custList = cjc.findCustomerEntities();
        for (Customer c : custList) {
            ComboPair p = new ComboPair(c.getId(), c.getNama());
            data.add(p);
        }
    }
}
