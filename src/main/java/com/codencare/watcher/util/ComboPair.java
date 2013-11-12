/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codencare.watcher.util;

import javafx.util.Pair;

/**
 *
 * @author abah
 * @param <K> key
 * @param <V> Value
 */
public class ComboPair<K,V> extends Pair <K,V>{
    private static final String SEPARATOR = "|";
    public ComboPair(K k, V v) {
        super(k, v);
    }
    
    @Override
    public String toString(){
        return getValue().toString();
    }
    
}
