/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.component;

import com.codencare.watcher.desktop.MainApp;
import com.codencare.watcher.entity.Device;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * This component represent map of area
 * @author ImanLHakim@gmail.com
 */
public class MapView extends Control {

    /**
     * margin between map and location edge in window.
     */
    private static final int MARGIN = Integer.parseInt(MainApp.defaultProps.getProperty("margin"));
    
    private final Pane pane = new Pane();
    /**
     * map image place holder.
     */
    private final Canvas mapCanvas = new Canvas();
    private final Image map;
    /**
     * list of device to be viewed in map
     */
    private List<Device> devices;
    /**
     * Horizontal ratio of map image.
     */
    private double xRatio = 1;
    /**
     * Vertical ratio of map image.
     */
    private double yRatio = 1;
    
    /**
     * Map zoom type, only limited value allowed, because it's only bitmap image
     * TODO: vector image will give better result
     */
    private MapZoom mapZoom;
    
    /**
     * A constructor.
     * @param devices list of device to be viewed in map 
     */
    public MapView(List<Device> devices) {
        this.devices = devices;
        getStyleClass().add("map-view");
        map = new Image(MapView.class.getResourceAsStream(MainApp.defaultProps.getProperty("map-image")));
        mapCanvas.setHeight(map.getHeight());
        mapCanvas.setWidth(map.getWidth());
        mapCanvas.getGraphicsContext2D().drawImage(map, 0, 0);
        mapZoom = MapZoom.Original;
        pane.getChildren().add(mapCanvas);
        this.getChildren().add(pane);
        putAllHome();
    }
    
    /**
     * Add a device icon (home) into  map
     * @param d device
     */    
    public void addDevice(Device d){
        devices.add(d);
        putHomeIcon(d);
    }
    /**
     * Remove a device icon from map
     * @param d device
     */
    public void removeDevice(Device d){
        removeIcon(d);
        devices.remove(d);
    }
    
    /**
     * Default css for this component.
     * @return 
     */
    @Override
    protected String getUserAgentStylesheet() {
        return MapView.class.getResource("MapView.css").toExternalForm();
    }
    
    /**
     * Put all icons into map.
     */
    private void putAllHome(){
         for (Device d : this.devices) {
            putHomeIcon(d);
        }
    }
    
    /** 
     * Clean up map.
     */
    private void removeAllHome(){
        for (Device d : this.devices) {
            removeIcon(d);
        }
    }
    
    /**
     * Set zooming mode
     *      
     * @param mapZoom
     * @param devices
     * @param keepRatio 
     */
    public void setMapZoom(MapZoom mapZoom,List<Device> devices, boolean keepRatio) {
       /* TODO: implement keepRatio, currently assumed false*/
        removeAllHome();
        this.devices = devices;
        this.mapZoom = mapZoom;
        if (this.mapZoom == MapZoom.FitAll) {
           
            this.mapZoom = mapZoom;
            mapCanvas.setWidth(getScene().getWidth() - MARGIN);
            mapCanvas.setHeight(getScene().getHeight() - MARGIN);
            mapCanvas.getGraphicsContext2D().drawImage(map, 0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
            this.yRatio = mapCanvas.getHeight()/map.getHeight();
            this.xRatio = mapCanvas.getWidth()/map.getWidth();
                      
        } else if (this.mapZoom == MapZoom.FitHeight) {
            
            this.mapZoom = mapZoom;
            mapCanvas.setWidth(map.getWidth());
            mapCanvas.setHeight(getScene().getHeight() - MARGIN);
            mapCanvas.getGraphicsContext2D().drawImage(map, 0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
            this.yRatio = mapCanvas.getHeight()/map.getHeight();
            
        } else if (this.mapZoom == MapZoom.FitWidth) {
          
            this.mapZoom = mapZoom;
            mapCanvas.setWidth(getScene().getWidth() - MARGIN);
            mapCanvas.setHeight(map.getHeight());
            mapCanvas.getGraphicsContext2D().drawImage(map, 0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
            this.xRatio = mapCanvas.getWidth()/map.getWidth();
           
        } else {
            
            mapCanvas.setWidth(map.getWidth());
            mapCanvas.setHeight(map.getHeight());
            mapCanvas.getGraphicsContext2D().drawImage(map, 0, 0);
            this.yRatio = mapCanvas.getHeight()/map.getHeight();
            this.xRatio = mapCanvas.getWidth()/map.getWidth();
             
        }
         putAllHome(); 
    }
    
    /**
     * getZoom mode
     * @return 
     */
    public MapZoom getMapZoom() {
        return mapZoom;
    }
    
    /**
     * put device on map
     * @param d 
     */
    private void putHomeIcon(Device d) {
        Home.setXRatio(xRatio);
        Home.setyRatio(yRatio);
        final Home b = new Home(d,false);
        pane.getChildren().add(b);
    }
    
    /** 
     * remove home/icon from map
     * @param d 
     */
    private void removeIcon(Device d){
        final Home b = new Home(d,false);
        pane.getChildren().remove(b);
    }
}
