/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.component;

import com.codencare.watcher.desktop.MainApp;
import com.codencare.watcher.desktop.MainFXMLController;
import com.codencare.watcher.entity.Device;
import java.util.Objects;
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
 * @author ImanLHakim@gmail.com
 */
public class Home extends Button{
   
    /**
     * Margin between image and location edge
     */
    private static final int MARGIN = Integer.parseInt(MainApp.defaultProps.getProperty("margin"));
    /**
     * Image link when home alarm active
     */
    private static final Image HOME_IMG_ACTIVE = new Image(MapView.class.getResourceAsStream(MainApp.defaultProps.getProperty("home-image-active")));
    /**
     * Image link when home alarm inactive
     */
    private static final Image HOME_IMG_INACTIVE = new Image(MapView.class.getResourceAsStream(MainApp.defaultProps.getProperty("home-image-inactive")));
    /**
     * width of home icon
     */
    private static final double HOME_WIDTH =Double.parseDouble(MainApp.defaultProps.getProperty("home-width"));
    /**
     * height of home icon
     */
    private static final double HOME_HEIGHT =Double.parseDouble(MainApp.defaultProps.getProperty("home-height"));
    /** 
     * horizontal ratio for zooming
     */
    private static double xRatio = 1;
    /**
     * vertical ration for zooming
     */
    private static double yRatio = 1;
    /** 
     * image place-holder
     */
    private ImageView HOME_VIEW = new ImageView(HOME_IMG_INACTIVE);
    
    /**
     * is alarm active or not, these states will show different image.
     */
    private boolean active;
    /**
     * Create home alarm icon
     * 
     * TODO: Create more functionality to this icon, including 
     * <li> context menu
     * for alarm and device administration.</li>
     * <li>context menu for alarm details</li>
     * <li>context menu for alarm/message respond</li>
     * @param d
     * @param active 
     */
    public Home(final Device d, boolean active){
        this.active = active;
        if(this.active){
            HOME_VIEW = new ImageView(HOME_IMG_ACTIVE);
        }else{
            HOME_VIEW = new ImageView(HOME_IMG_INACTIVE);
        }
        HOME_VIEW.setFitWidth(HOME_WIDTH*xRatio);
        HOME_VIEW.setFitHeight(HOME_HEIGHT*yRatio);
        double locX = ((d.getLocX()-MARGIN)*xRatio) - (HOME_WIDTH/2);
        double locY = ((d.getLocY()-MARGIN)*yRatio)-(HOME_HEIGHT/2);
        locX = locX < MARGIN? MARGIN:locX;
        locY = locY < MARGIN? MARGIN:locY;
        this.relocate(locX,locY );
        this.setId(String.valueOf(d.getId()));
        this.setBackground((Background.EMPTY));
//        this.getStyleClass().add("closebutton");
//        this.setMouseTransparent(true);
        this.setGraphic(HOME_VIEW);
        this.setTooltip(new Tooltip("id:"+d.getId()+", x:"+locX+",y:"+locY));
        this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        this.setOnMouseClicked(new EventHandler<MouseEvent>(){
                public void handle(MouseEvent event){
                    Action response = Dialogs.create()
                                    .title("alarm")
                                    .owner(getScene().getWindow())
                                    .message(d.toString())
                                    .showWarning();

//                            TODO: what should system do after click ?
                            if (response == Dialog.Actions.OK) {
                                //TODO: ... submit user input
                               MapView mapView = (MapView) getScene().lookup("#mapView");
                               mapView.removeDevice(d);
                               MainFXMLController.removeAlarm(d);
                            } else {
                                //TODO: ... user cancelled, reset form to default
                            }
                }
         });
    } 

    /**
     * Set horizontal ration for icon
     * @param ratio horizontal ratio
     */    
    public static void setXRatio(double ratio) {
        Home.xRatio = ratio;
    }

    /**
     * Get horizontal ratio of icon
     * @return 
     */
    public static double getXRatio() {
        return xRatio;
    }   

    /**
     * Get vertical ratio of icon
     * @return 
     */
    public static double getyRatio() {
        return yRatio;
    }

    /**
     * Set vertical ratio of icon
     * @param yRatio 
     */
    public static void setyRatio(double yRatio) {
        Home.yRatio = yRatio;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.getId());
        return hash;
    }

    /**
     * icon unique identified by it's id (from device's id)
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
}
