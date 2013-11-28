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
public enum ResolveStatus {

    RESOLVED(1), NORMAL(0), UNRESOLVED(-1);

    private final int value;

    private ResolveStatus(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}
