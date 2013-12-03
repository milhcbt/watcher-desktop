/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */

package com.codencare.esb.message;

/**
 * This enumeration contains valid value for Digital input from device
 * value is in integer and used in database
 * @author imanlhakim@gmail.com
 */
public enum DigitalInput {

    HIGH(1), LOW(0), UNKNOW(-1);

    private final int value;

    private DigitalInput(final int newValue) {
        value = newValue;
    }

    /**
     * Get <code>Integer</code> value of digital input
     * @return integer value of digital input
     */
    public int getValue() {
        return value;
    }
}
