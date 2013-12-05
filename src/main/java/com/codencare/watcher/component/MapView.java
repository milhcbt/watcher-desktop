/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.component;

import com.codencare.watcher.controller.DeviceJpaController;
import com.codencare.watcher.desktop.MainApp;
import com.codencare.watcher.entity.Device;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * This component represent map of area
 *
 * @author ImanLHakim@gmail.com
 */
public class MapView extends Control {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("watcherDB");

    /**
     * margin between map and location edge in window.
     */
    private static final int MARGIN = Integer.parseInt(MainApp.defaultProps.getProperty("margin"));

    private static Image map;

    private final Pane pane = new Pane();
    /**
     * map image place holder.
     */
    private final Canvas mapCanvas = new Canvas();

    /**
     * list of device to homes viewed in map
     */
    private final List<Home> homes = new ArrayList<>();
    /**
     * Horizontal ratio of map image.
     */
    private double xRatio = 1;
    /**
     * Vertical ratio of map image.
     */
    private double yRatio = 1;

    /**
     * Map zoom type, only limited value allowed, homeecause it's only homeitmap
     * image TODO: vector image will give homeetter result
     */
    private MapZoom mapZoom;

    /**
     * A constructor.
     *
     */
    public MapView() {
        getStyleClass().add("map-view");
        loadDevices();
        loadMapImage();
    }
    
    /**
     * Load device from database
     */
    private void loadDevices(){
        DeviceJpaController djc = new DeviceJpaController(emf);
        for (Device d : djc.findDeviceEntities()) {
            Home home = new Home(d);
            homes.add(home);
        }
    }
    
    /**
     * Load and draw image for first time.
     */
    private void loadMapImage() {
        map = new Image(MapView.class.getResourceAsStream(MainApp.defaultProps.getProperty("map-image")));
        drawMap(MapZoom.Original, true);
        pane.getChildren().add(mapCanvas);
        putAllHome();
        this.getChildren().add(pane);
    }

    /**
     * Default css for this component.
     *
     * @return
     */
    @Override
    protected String getUserAgentStylesheet() {
        return MapView.class.getResource("MapView.css").toExternalForm();
    }

    /**
     * Put all icons into map.
     */
    private void putAllHome() {
        if (homes != null) {
            for (Home h : homes) {
                putHomeIcon(h);
            }
        }
    }

    /**
     * Clean up map.
     */
    private void removeAllHome() {
        if (homes != null) {
            homes.clear();
        }
    }

    /**
     * Set zooming mode
     *
     * @param mapZoom
     * @param keepRatio
     */
    public void drawMap(MapZoom mapZoom, boolean keepRatio) {
        /* TODO: implement keepRatio, currently assumed false*/
//        removeAllHome();
        this.mapZoom = mapZoom;
        if (this.mapZoom == MapZoom.FitAll) {
            mapCanvas.setWidth(getScene().getWidth() - MARGIN);
            mapCanvas.setHeight(getScene().getHeight() - MARGIN);
            mapCanvas.getGraphicsContext2D().drawImage(map, 0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
            this.yRatio = mapCanvas.getHeight() / map.getHeight();
            this.xRatio = mapCanvas.getWidth() / map.getWidth();

        } else if (this.mapZoom == MapZoom.FitHeight) {
            mapCanvas.setWidth(map.getWidth());
            mapCanvas.setHeight(getScene().getHeight() - MARGIN);
            mapCanvas.getGraphicsContext2D().drawImage(map, 0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
            this.yRatio = mapCanvas.getHeight() / map.getHeight();

        } else if (this.mapZoom == MapZoom.FitWidth) {
            mapCanvas.setWidth(getScene().getWidth() - MARGIN);
            mapCanvas.setHeight(map.getHeight());
            mapCanvas.getGraphicsContext2D().drawImage(map, 0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
            this.xRatio = mapCanvas.getWidth() / map.getWidth();
        } else {
            mapCanvas.setWidth(map.getWidth());
            mapCanvas.setHeight(map.getHeight());
            mapCanvas.getGraphicsContext2D().drawImage(map, 0, 0);
            this.yRatio = mapCanvas.getHeight() / map.getHeight();
            this.xRatio = mapCanvas.getWidth() / map.getWidth();

        }
//        putAllHome();
    }

    /**
     * getZoom mode
     *
     * @return
     */
    public MapZoom getMapZoom() {
        return mapZoom;
    }

    /**
     * put device on map
     *
     * @param d device
     */
    private void putHomeIcon(Home h) {
        pane.getChildren().add(h);
    }
    
    public void setHomeMode(String id,DeviceMode dm){
        if (homes != null){
            for(Home h: homes){
                if(h.getId().equalsIgnoreCase(id)){
                    h.setMode(dm);
                }
            }
        }
    }
}
