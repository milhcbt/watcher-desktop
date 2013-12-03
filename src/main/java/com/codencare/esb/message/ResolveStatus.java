/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.esb.message;

/**
 * Valid value for message respond status
 * @author ImanLHakim@gmail.com
 */
public enum ResolveStatus {

    RESOLVED(1), NORMAL(0), UNRESOLVED(-1);

    private final int value;

    private ResolveStatus(final int newValue) {
        value = newValue;
    }

    /**
     * Integer value of status
     * @return status in Integer
     */
    public int getValue() {
        return value;
    }
}
