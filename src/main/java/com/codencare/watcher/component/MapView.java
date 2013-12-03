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
    
    
    public void addDevice(Device d){
        devices.add(d);
        putHomeIcon(d);
    }
    public void removeDevice(Device d){
        removeHomeIcon(d);
        devices.remove(d);
    }
    
    @Override
    protected String getUserAgentStylesheet() {
        return MapView.class.getResource("MapView.css").toExternalForm();
    }
    
    private void putAllHome(){
         for (Device d : this.devices) {
            putHomeIcon(d);
        }
    }
    
    private void removeAllHome(){
        for (Device d : this.devices) {
            removeHomeIcon(d);
        }
    }
    
    public void setMapZoom(MapZoom mapZoom,List<Device> devices) {
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
    
    public MapZoom getMapZoom() {
        return mapZoom;
    }

//    public List<Device> getDevices() {
//        return devices;
//    }
//
//    public void setDevices(List<Device> devices) {
//        this.devices = devices;
//    }
    
    
    private void putHomeIcon(Device d) {
        Home.setXRatio(xRatio);
        Home.setyRatio(yRatio);
        final Home b = new Home(d,false);
        pane.getChildren().add(b);
    }
    private void removeHomeIcon(Device d){
        final Home b = new Home(d,false);
        pane.getChildren().remove(b);
    }
    
    
    
}
