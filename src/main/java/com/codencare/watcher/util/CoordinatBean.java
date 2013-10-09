/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codencare.watcher.util;

import javafx.beans.property.SimpleDoubleProperty;

/**
 *
 * @author abah
 */
public final class CoordinatBean {

    public CoordinatBean(double x, double y) {
        xProperty().set(x);
        yProperty().set(y);
    }
    
    private SimpleDoubleProperty x;
    public void setX(double x){xProperty().set(x);}
    public double getX(){return x.get();}
    public SimpleDoubleProperty xProperty(){
        if(x == null) x = new SimpleDoubleProperty();
        return x;
    }
    
    private SimpleDoubleProperty y;
    public void setY(double y){yProperty().set(y);}
    public double getY(){return y.get();}
    public SimpleDoubleProperty yProperty(){
        if(y==null) y = new SimpleDoubleProperty();
        return y;
    }
}
