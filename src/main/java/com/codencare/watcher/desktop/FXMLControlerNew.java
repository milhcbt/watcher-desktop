package com.codencare.watcher.desktop;

//import static com.codencare.watcher.desktop.FXMLControlerNew.server;
import com.codencare.watcher.dialog.NewNodeDialog;
import com.codencare.watcher.util.CoordinatBean;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumnBuilder;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.log4j.Logger;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

public class FXMLControlerNew implements Initializable {

    static ServerSocket server;
    static final int port = 7000;

    private static final Logger LOGGER = Logger.getLogger(FXMLControlerNew.class.getName());
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("watcherDB");

    private final ObservableList coords = FXCollections.observableArrayList();
    private Point2D loc;

    @FXML
    AnchorPane root;

    @FXML
    TableView infoTable;
    
    @FXML
    ScrollPane scrollPane;
    
    @FXML
    MenuItem userManagement;

    @FXML
    private ImageView mapView;

   
    Task<Void> task = new Task<Void>() {
        @Override
        protected Void call() {
            while (true) {
                Socket sock = null;
                try {
                    sock = server.accept();
                    BufferedInputStream bis = new BufferedInputStream(sock.getInputStream());
                    final StringBuilder data = new StringBuilder();
                    do {
                        int i = bis.read();
                        LOGGER.info(i);
                        data.append((char) i);
                    } while (bis.available() > 0);
                    LOGGER.info(data.toString());
                    Platform.runLater(new Runnable() {
                        public void run() {
                            Action response = Dialogs.create()
                                    .title("alarm")
                                    .owner(root.getScene().getWindow())
                                    .message(data.toString())
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        assert infoTable != null : "fx:id=\"infoTable\" was not injected: check your FXML file 'Scene.fxml'.";
        assert mapView != null : "fx:id=\"mapView\" was not injected: check your FXML file 'Scene.fxml'.";
        assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'Scene.fxml'.";
        assert scrollPane != null : "fx:id=\"scrollPane\" was not injected: check your FXML file 'Scene.fxml'.";
        assert userManagement != null : "fx:id=\"userManagement\" was not injected: check your FXML file 'Scene.fxml'.";
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

     @FXML
    void onClick(MouseEvent event) {
//        CustomerJpaController cjc = new CustomerJpaController(emf);
//        List<Customer> custList = cjc.findCustomerEntities();
//        LOGGER.info(custList.toString());
        loc = new Point2D(event.getX(), event.getY());
        CoordinatBean cb = new CoordinatBean(event.getX(), event.getY());

        if (infoTable.getColumns().isEmpty()) {
            infoTable.getColumns().add(TableColumnBuilder.create().text("Coordinat X")
                    .cellValueFactory(new PropertyValueFactory("x"))
                    .prefWidth(50)
                    .build());
            infoTable.getColumns().add(TableColumnBuilder.create()
                    .text("Coordinat Y")
                    .cellValueFactory(new PropertyValueFactory("y"))
                    .prefWidth(50)
                    .build());
        }
        infoTable.setItems(coords);
        coords.add(cb);
        infoTable.autosize();
        LOGGER.info("x:{0} y:{1}" + new Object[]{event.getX(), event.getY()});
    }

  
    //add new node
    @FXML
    void onNewNode(ActionEvent event) {
        Stage newNode = new NewNodeDialog((Stage) root.getScene().getWindow(), true, MainApp.defaultProps.getProperty("new-device"), loc);
        newNode.showAndWait();
    }
    
    //button home
    @FXML
    void buttonHome(ActionEvent event) throws Exception{
        new MainApp().start(new Stage());  
    }
    
    //buttonExit
    @FXML
    void buttonExit(ActionEvent event){
        Platform.exit();
    }
    @FXML
    void btnCpass(ActionEvent event){
        Platform.exit();
    }
   
    //button User
    @FXML
  public void buttonUser(ActionEvent event){
        //myController.setScreen(ScreensFramework.MAIN_SCREEN);
    }
    }
