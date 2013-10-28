package com.codencare.watcher.desktop;

import com.codencare.watcher.component.MapView;
import com.codencare.watcher.component.MapZoom;
import com.codencare.watcher.controller.CustomerJpaController;
import com.codencare.watcher.controller.DeviceJpaController;
import com.codencare.watcher.dialog.NewNodeDialog;
import com.codencare.watcher.entity.Customer;
import com.codencare.watcher.entity.Device;
import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.stage.Stage;
import javafx.util.converter.LongStringConverter;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

public class MainFXMLController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(MainFXMLController.class.getName());
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("watcherDB");

    static ServerSocket server;
    static final int port = 7000;

   
    //FXML
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TabPane detailTabs;

    @FXML
    private TitledPane details;

    @FXML
    private Button exitButton;

    @FXML
    private AnchorPane rightDetail;

    @FXML
    private AnchorPane root;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private SplitPane splitPane;

    @FXML
    private TableView<?> userTable;

    @FXML
    private ChoiceBox<?> zoomChoices;
    //LMXF

    private Point2D loc;
    private List<Device> alarmedDevices;

    private MapView mapView;

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        assert customerTable != null : "fx:id=\"customerTable\" was not injected: check your FXML file 'Main.fxml'.";
        assert detailTabs != null : "fx:id=\"detailTabs\" was not injected: check your FXML file 'Main.fxml'.";
        assert details != null : "fx:id=\"details\" was not injected: check your FXML file 'Main.fxml'.";
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'Main.fxml'.";
        assert rightDetail != null : "fx:id=\"rightDetail\" was not injected: check your FXML file 'Main.fxml'.";
        assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'Main.fxml'.";
        assert scrollPane != null : "fx:id=\"scrollPane\" was not injected: check your FXML file 'Main.fxml'.";
        assert splitPane != null : "fx:id=\"splitPane\" was not injected: check your FXML file 'Main.fxml'.";
        assert userTable != null : "fx:id=\"userTable\" was not injected: check your FXML file 'Main.fxml'.";
        assert zoomChoices != null : "fx:id=\"zoomChoices\" was not injected: check your FXML file 'Main.fxml'.";

//      map = new Image(MainFXMLController.class.getResourceAsStream("/styles/img/purimansion-org.jpg"));
        updateDevice();
        mapView = new MapView(alarmedDevices);
        mapView.setId("mapView");
        mapView.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                onContextMenu(event);
            }
        });
        scrollPane.setContent(mapView);
//        detailTabs.prefWidthProperty().bind(root.widthProperty().divide(2));
        customerTable.prefWidthProperty().bind(detailTabs.widthProperty());
        userTable.prefWidthProperty().bind(detailTabs.widthProperty());
       
        details.setBackground(Background.EMPTY);
        detailTabs.setBackground(Background.EMPTY);
        rightDetail.setBackground(Background.EMPTY);

        zoomChoices.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                
                updateDevice();
                if (newValue.intValue() ==  0) {
                    mapView.setMapZoom(MapZoom.Original, alarmedDevices);
                } else if (newValue.intValue() == 1) {
                    mapView.setMapZoom(MapZoom.FitAll, alarmedDevices);
                }// else if (newValue == 2) {
//                    mapView.setMapZoom(MapZoom.FitWidth, alarmedDevices);
//                } else if (newValue == 3) {
//                    mapView.setMapZoom(MapZoom.FitHeight, alarmedDevices);
//                }
            }

        });

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

//    @FXML
    void onContextMenu(ContextMenuEvent event) {
        CustomerJpaController cjc = new CustomerJpaController(emf);
        List<Customer> custList = cjc.findCustomerEntities();
        ObservableList<Customer> ol = FXCollections.observableArrayList(custList);

        if (customerTable.getColumns().isEmpty()) {
            for (final PropertyDescriptor f : PropertyUtils.getPropertyDescriptors(Customer.class)) {
                if (f.getPropertyType().isAssignableFrom(Collection.class) || f.getPropertyType().isAssignableFrom(Class.class)) {
                } else if (f.getPropertyType().isAssignableFrom(String.class)) {
                    TableColumn<Customer, String> col = new TableColumn(f.getName());
                    col.setCellValueFactory(new PropertyValueFactory<Customer, String>(f.getName()));
                    col.setEditable(true);
                    col.setMinWidth(100);

                    col.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
                    col.setOnEditCommit(
                            new EventHandler<CellEditEvent<Customer, String>>() {
                                @Override
                                public void handle(CellEditEvent<Customer, String> t) {
                                    try {
                                        int i = t.getTablePosition().getRow();
                                        Customer c = t.getTableView().getItems().get(i);
                                        Method m = PropertyUtils.getWriteMethod(PropertyUtils.getPropertyDescriptor(c, f.getName()));
                                        m.invoke(c, t.getNewValue());
//                                    ((Customer) t.getTableView().getItems().get(
//                                    t.getTablePosition().getRow())).setNama(t.getNewValue());
                                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                                        java.util.logging.Logger.getLogger(MainFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                            );
                    customerTable.getColumns().add(col);
                } else if (f.getPropertyType().isAssignableFrom(Long.class)) {
                    TableColumn col = new TableColumn(f.getName());
                    col.setCellValueFactory(new PropertyValueFactory<Customer, Long>(f.getName()));
                    col.setEditable(true);
                    col.setMinWidth(100);

                    col.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
                    col.setOnEditCommit(
                            new EventHandler<CellEditEvent<Customer, Long>>() {
                                @Override
                                public void handle(CellEditEvent<Customer, Long> t) {
                                    try {
                                        int i = t.getTablePosition().getRow();
                                        Customer c = t.getTableView().getItems().get(i);
                                        Method m = PropertyUtils.getWriteMethod(PropertyUtils.getPropertyDescriptor(c, f.getName()));
                                        m.invoke(c, t.getNewValue());
//                                    ((Customer) t.getTableView().getItems().get(
//                                    t.getTablePosition().getRow())).setNama(t.getNewValue());
                                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                                        java.util.logging.Logger.getLogger(MainFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                            );
                    customerTable.getColumns().add(col);
                }
            }
        }
        customerTable.setEditable(true);
        customerTable.setItems(ol);
        customerTable.autosize();
        LOGGER.info("x:{0} y:{1}" + new Object[]{event.getX(), event.getY()});
    }

    @FXML
    void zoom(ActionEvent event) {
        updateDevice();
        if (mapView.getMapZoom() == MapZoom.Original) {
            mapView.setMapZoom(MapZoom.FitAll, alarmedDevices);
        }if (mapView.getMapZoom() == MapZoom.FitHeight) {
            mapView.setMapZoom(MapZoom.FitHeight, alarmedDevices);
        }if (mapView.getMapZoom() == MapZoom.FitWidth) {
            mapView.setMapZoom(MapZoom.FitWidth, alarmedDevices);
        }
        else {
            mapView.setMapZoom(MapZoom.Original, alarmedDevices);
        }
    }

    @FXML
    void exit(ActionEvent event) {
        ((Stage) root.getScene().getWindow()).close();
    }

    @FXML
    void onNewNode(ActionEvent event) {
        Stage newNode = new NewNodeDialog((Stage) root.getScene().getWindow(), true, MainApp.defaultProps.getProperty("new-device"), loc);
        newNode.showAndWait();
    }

    void updateDevice() {
//          alarmedDevices = null;
        //  emf.getCache().evict(Device.class);
        alarmedDevices = (new DeviceJpaController(emf)).findAlarmedDevice();
    }

  
     /**
     * TODO: this method need analysis (inter-dependent)
     * @param d 
     */
    public static void removeAlarm(Device d) {
        DeviceJpaController djc = new DeviceJpaController(emf);
        djc.turnOfAlarm(d);
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
                                    .owner(root.getScene().getWindow())
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

    class EditingCell extends TableCell {

        private TextField textField;

        public EditingCell() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText((String) getItem());
            setGraphic(null);
        }

        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> arg0,
                        Boolean arg1, Boolean arg2) {
                    if (!arg2) {
                        commitEdit(textField.getText());
                    }
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }

    
}
