/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codencare.watcher.component;

import com.codencare.watcher.desktop.MainApp;
import com.codencare.watcher.desktop.MainFXMLController;
import com.codencare.watcher.entity.Device;
import com.sun.prism.paint.Color;
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
 *
 * @author abah
 */
public class Home extends Button{
   
    private static final int MARGIN = Integer.parseInt(MainApp.defaultProps.getProperty("margin"));
    private static final Image HOME_IMG = new Image(MapView.class.getResourceAsStream(MainApp.defaultProps.getProperty("home-image")));
    private static final double HOME_WIDTH =Double.parseDouble(MainApp.defaultProps.getProperty("home-width"));
    private static final double HOME_HEIGHT =Double.parseDouble(MainApp.defaultProps.getProperty("home-height"));
    private static double xRatio = 1;
    private static double yRatio = 1;
    private ImageView HOME_VIEW = new ImageView(HOME_IMG);
    
    public Home(final Device d){
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

                            if (response == Dialog.Actions.OK) {
                                // ... submit user input
                               MapView mapView = (MapView) getScene().lookup("#mapView");
                               mapView.removeDevice(d);
                               MainFXMLController.removeAlarm(d);
                            } else {
                                // ... user cancelled, reset form to default
                            }
                }
         });
    } 

    public static void setXRatio(double ratio) {
        Home.xRatio = ratio;
    }

    public static double getXRatio() {
        return xRatio;
    }   

    public static double getyRatio() {
        return yRatio;
    }

    public static void setyRatio(double yRatio) {
        Home.yRatio = yRatio;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.getId());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Home other = (Home) obj;
        if (!Objects.equals(this.getId(), other.getId())) {
            return false;
        }
        return true;
    }
    
}
