/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author abah
 */
public class MapView extends Control {

    private static final int MARGIN = Integer.parseInt(MainApp.defaultProps.getProperty("margin"));
    private static final int HOME_WH = 20;
    private final Pane pane = new Pane();
    private final Canvas mapCanvas = new Canvas();
    private final Image map;
    private List<Device> devices;
    private double xRatio = 1;
    private double yRatio = 1;
    
    private MapZoom mapZoom;
    
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
        final Home b = new Home(d);
        pane.getChildren().add(b);
    }
    private void removeHomeIcon(Device d){
        final Home b = new Home(d);
        pane.getChildren().remove(b);
    }
    
    
    
}
