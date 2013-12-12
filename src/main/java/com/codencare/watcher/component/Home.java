/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.component;

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
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

/**
 * This class is implementation of home icon
 *
 * @author ImanLHakim@gmail.com
 */
public class Home extends Button {

    
    /////
    //ATTRIBUTES
    //X AND Y postion of Node
    double x = 0;
    double y = 0;
    //X AND Y position of mouse
    double mousex = 0;
    double mousey = 0;
    /**
     * Margin between image and location edge
     */
    private static final int MARGIN = Integer.parseInt(MainApp.defaultProps.getProperty("margin"));
    /**
     * Image link when home ac
     */
    private static final Image HOME_IMG_ACTIVE_ON_AC = new Image(MapView.class.getResourceAsStream(MainApp.defaultProps.getProperty("home-image-ac")));
    /**
     * Image link when home inactive
     */
    private static final Image HOME_IMG_INACTIVE = new Image(MapView.class.getResourceAsStream(MainApp.defaultProps.getProperty("home-image-inactive")));
    /**
     * Image link when home on battery
     */
    private static final Image HOME_IMG_ACTIVE_ON_BATT = new Image(MapView.class.getResourceAsStream(MainApp.defaultProps.getProperty("home-image-battery")));
    /**
     * Image link when home alarmed
     */
    private static final Image HOME_IMG_ALARMED = new Image(MapView.class.getResourceAsStream(MainApp.defaultProps.getProperty("home-image-alarmed")));

    /**
     * width of home icon
     */
    private static final double HOME_WIDTH = Double.parseDouble(MainApp.defaultProps.getProperty("home-width"));
    /**
     * height of home icon
     */
    private static final double HOME_HEIGHT = Double.parseDouble(MainApp.defaultProps.getProperty("home-height"));
    /**
     * horizontal ratio for zooming
     */
    private static double xRatio = 1;
    /**
     * vertical ration for zooming
     */
    private static double yRatio = 1;

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
    /**
     * image place-holder
     */
    private ImageView HOME_VIEW = new ImageView(HOME_IMG_INACTIVE);

    private Device d;

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
        
        this.d = device;
        setMode(this.d.getMode());
        double x = ((this.d.getLocX() - MARGIN) * xRatio) - (HOME_WIDTH / 2);
        double y = ((this.d.getLocY() - MARGIN) * yRatio) - (HOME_HEIGHT / 2);
        x = x < MARGIN ? MARGIN : x;
        y = y < MARGIN ? MARGIN : y;
        this.relocate(x, y);
        this.setId(String.valueOf(d.getId()));
        this.setBackground(Background.EMPTY);
        this.setTooltip(new Tooltip("id:" + d.getId() + ", x:" + x + ",y:" + y));
        this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Action response = Dialogs.create()
                        .title("alarm")
                        .owner(getScene().getWindow())
                        .message(d.toString())
                        .showWarning();

//                            TODO: what should system do after click ?
                if (response == Dialog.Actions.OK) {
                                //TODO: ... submit user input
//                               MapView mapView = (MapView) getScene().lookup("#mapView");
//                               mapView.removeDevice(d);
//                               MainFXMLController.removeAlarm(d); 

                } else {
                    //TODO: ... user cancelled, reset form to default
                }
            }
        });
    }

    public void setMode(DeviceMode mode) {
        setMode(mode.getValue());
    }

    private void setMode(short m) {
        d.setMode(m);
        if (d.getMode() == Device.MODE_ACTIVE_ON_AC) {
            HOME_VIEW = new ImageView(HOME_IMG_ACTIVE_ON_AC);
        } else if (d.getMode() == Device.MODE_ACTIVE_ON_BATTERY) {
            HOME_VIEW = new ImageView(HOME_IMG_ACTIVE_ON_BATT);
        } else if (d.getMode() == Device.MODE_ALARMED) {
            HOME_VIEW = new ImageView(HOME_IMG_ALARMED);
        } else {
            HOME_VIEW = new ImageView(HOME_IMG_INACTIVE);
        }
        HOME_VIEW.setFitWidth(HOME_WIDTH * xRatio);
        HOME_VIEW.setFitHeight(HOME_HEIGHT * yRatio);
        this.setGraphic(HOME_VIEW);
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
                //Get the exact moved X and Y
                x += event.getSceneX() - mousex;
                y += event.getSceneY() - mousey;

                //set the positon of Node after calculation
                setLayoutX(x);
                setLayoutY(y);

                //again set current Mouse x AND y position
                mousex = event.getSceneX();
                mousey = event.getSceneY();

            }
        });
    }
}
