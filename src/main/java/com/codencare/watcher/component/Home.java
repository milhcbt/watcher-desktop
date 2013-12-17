/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.component;

import com.codencare.watcher.controller.DeviceJpaController;
import com.codencare.watcher.desktop.MainApp;
import com.codencare.watcher.entity.Device;
import java.util.Objects;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.log4j.Logger;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

/**
 * This class is implementation of home icon
 *
 * @author ImanLHakim@gmail.com
 */
public class Home extends Button {

    private static final Logger LOGGER = Logger.getLogger(Home.class.getName());
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("watcherDB");

    /**
     * Margin between image and location edge
     */
    private static final int MARGIN = Integer.parseInt(MainApp.defaultProps.getProperty("margin"));
//   TODO: removed using images to heavy in rendering.....
//    /**
//     * Image link when home ac
//     */
//    private static final Image HOME_IMG_ACTIVE_ON_AC = new Image(MapView.class.getResourceAsStream(MainApp.defaultProps.getProperty("home-image-ac")));
//    /**
//     * Image link when home inactive
//     */
//    private static final Image HOME_IMG_INACTIVE = new Image(MapView.class.getResourceAsStream(MainApp.defaultProps.getProperty("home-image-inactive")));
//    /**
//     * Image link when home on battery
//     */
//    private static final Image HOME_IMG_ACTIVE_ON_BATT = new Image(MapView.class.getResourceAsStream(MainApp.defaultProps.getProperty("home-image-battery")));
//    /**
//     * Image link when home alarmed
//     */
    private static final Image HOME_IMG_ALARMED = new Image(MapView.class.getResourceAsStream(MainApp.defaultProps.getProperty("home-image-alarmed")));

    /**
     * width of home icon
     */
    private static final double HOME_WIDTH = Double.parseDouble(MainApp.defaultProps.getProperty("home-width","30"));
    /**
     * height of home icon
     */
    private static final double HOME_HEIGHT = Double.parseDouble(MainApp.defaultProps.getProperty("home-height","30"));
    
    private static final int SMILE = 9786;
    private static final int SAD = 9785;
    private static final int SKULL = 9760;
    private static final int SNOW = 10052;
    
    /**
     * horizontal ratio for zooming
     */
    private static double xRatio = 1;
    /**
     * vertical ration for zooming
     */
    private static double yRatio = 1;

    /**
     * x position
     */
    private double x = 0;
    /**
     * y position
     */
    private double y = 0;
    /**
     * X position of mouse
     */
    private double mousex = 0;
    /**
     * Y position of mouse
     */
    private double mousey = 0;

//    /**
//     * image place-holder
//     */
//    private ImageView HOME_VIEW = new ImageView(HOME_IMG_INACTIVE);
    /**
     * device
     */
    private final Device device;

    /**
     * Create home alarm icon
     *
     * TODO: Create more functionality to this icon, including
     * <li> context menu for alarm and device administration.</li>
     * <li>context menu for alarm details</li>
     * <li>context menu for alarm/message respond</li>
     *
     * @param device device
     */
    public Home(final Device device) {
        super();

        makeDragable();
        this.device = device;
        getStyleClass().add("home");
        setHomeStyle(device);
        
        x = ((this.device.getLocX()) * xRatio);
        y = ((this.device.getLocY()) * yRatio);
        x = x < 0 ? 0 : x;
        y = y < 0 ? 0 : y;
        this.prefHeight(HOME_HEIGHT);
        this.prefWidth(HOME_WIDTH);

        this.relocate(x, y);
        this.setId(String.valueOf(this.device.getId()));
        this.setBackground(Background.EMPTY);
        this.setTooltip(new Tooltip("id:" + this.device.getId() + ", x:" + x + ",y:" + y));
        this.setContentDisplay(ContentDisplay.TEXT_ONLY);
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Action response = Dialogs.create()
                        .title("alarm")
                        .owner(getScene().getWindow())
                        .message(device.toString())
                        .showWarning();

//                            TODO: what should system do after click ?
                if (response == Dialog.Actions.OK) {
                    //TODO: ... submit user input
                    setMode(DeviceMode.ACTIVE_ON_AC);
                } else {
                    //TODO: ... user cancelled, reset form to default
                }
            }
        });
    }

    /**
     * Set horizontal ration for icon
     *
     * @param ratio horizontal ratio
     */
    public static void setXRatio(double ratio) {
        Home.xRatio = ratio;
    }

    /**
     * Get horizontal ratio of icon
     *
     * @return
     */
    public static double getXRatio() {
        return xRatio;
    }

    /**
     * Get vertical ratio of icon
     *
     * @return
     */
    public static double getyRatio() {
        return yRatio;
    }

    /**
     * Set vertical ratio of icon
     *
     * @param yRatio
     */
    public static void setyRatio(double yRatio) {
        Home.yRatio = yRatio;
    }

    public void setMode(DeviceMode mode) {
        setMode(mode.getValue());
    }

    private void setMode(short m) {
        device.setMode(m);
        DeviceJpaController djc = new DeviceJpaController(emf);
        try {
            djc.edit(device);
            setHomeStyle(device);
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.getId());
        return hash;
    }

    /**
     * icon unique identified by it's id (from device's id)
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Home other = (Home) obj;
        return Objects.equals(this.getId(), other.getId());
    }

    //To make this function accessible for other Class
    @Override
    public ObservableList getChildren() {
        return super.getChildren();
    }

//    public DraggableNode(){
//        super();
    private void makeDragable() {
        //EventListener for MousePressed
        onMousePressedProperty().set(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                //record the current mouse X and Y position on Node
                mousex = event.getSceneX();
                mousey = event.getSceneY();
                //get the x and y position measure from Left-Top
                x = getLayoutX();
                y = getLayoutY();
            }

        });

        //Event Listener for MouseDragged
        onMouseDraggedProperty().set(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                DeviceJpaController djc = new DeviceJpaController(emf);
                //Get the exact moved X and Y
                x += event.getSceneX() - mousex;
                y += event.getSceneY() - mousey;

                //set the positon of Node after calculation
                setLayoutX(x);
                setLayoutY(y);

                //again set current Mouse x AND y position
                mousex = event.getSceneX();
                mousey = event.getSceneY();
                device.setLocX((int) mousex);
                device.setLocY((int) mousey);
                try {
                    djc.edit(device);
                } catch (Exception ex) {
                    LOGGER.error(ex.toString());
                }
            }
        });
    }

    private void setHomeStyle(Device d) {
        if (d.getMode() == Device.MODE_ACTIVE_ON_AC) {
//            getStyleClass().clear();
//            getStyleClass().add("home-ac");
            setStyle("-fx-background-color:rgba(0,255,0,0.3);");
//            setText("  "+String.valueOf(((char) SMILE))+"  ");
//            setGraphic(null);
        } else if (d.getMode() == Device.MODE_ACTIVE_ON_BATTERY) {
//            getStyleClass().clear();
//            getStyleClass().add("home-battery");
            setStyle("-fx-background-color:rgba(0,0,255,0.3);");
//            setText(""+String.valueOf(((char) SNOW))+"");
//            setGraphic(null);
        } else if (d.getMode() == Device.MODE_ALARMED) {
//            getStyleClass().clear();
//            getStyleClass().add("home-alarmed");
            setStyle("-fx-background-color:rgba(255,0,0,0.7);");
//            setText(""+String.valueOf(((char) SKULL))+"");
//            setGraphic(new ImageView(HOME_IMG_ALARMED));
        } else {
//            getStyleClass().clear();
//            getStyleClass().add("home-disconnected");
            setStyle("-fx-background-color:rgba(100,100,100,0.3);");
//            setText("  "+String.valueOf(((char) SAD))+"  ");
//            setGraphic(null);
        }
    }
}
