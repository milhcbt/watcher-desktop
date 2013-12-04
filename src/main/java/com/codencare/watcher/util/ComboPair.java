/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.util;

import javafx.util.Pair;

/**
 * Key-Value-Pair class to be used as value for ComboBox 
 * @author Iman L Hakim <imanlhakim at gmail.com>
 * @param <K> key
 * @param <V> value
 */
public class ComboPair<K,V> extends Pair <K,V>{
    private static final String SEPARATOR = "|";
    
    /**
     * A Constructor
     * @param k key
     * @param v value
     */
    public ComboPair(K k, V v) {
        super(k, v);
    }
    
    /**
     * String representation of value in ComboBox or list.
     * @return 
     */
    @Override
    public String toString(){
        return getValue().toString();
    }
    
}
