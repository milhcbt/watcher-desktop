/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codencare.esb.message;

/**
 *
 * @author abah
 */
public enum DigitalInput {

    HIGH(1), LOW(0), UNKNOW(-1);

    private final int value;

    private DigitalInput(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}
