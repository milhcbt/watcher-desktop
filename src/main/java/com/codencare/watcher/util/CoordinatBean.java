/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.util;

import javafx.beans.property.SimpleDoubleProperty;

/**
 * a bean for Cartesian (XY) Coordinate.
 * @author Iman L Hakim <imanlhakim at gmail.com>
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
