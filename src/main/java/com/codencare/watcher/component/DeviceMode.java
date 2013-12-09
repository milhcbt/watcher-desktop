/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */

package com.codencare.watcher.component;

/**
 *
 * @author Iman L Hakim <imanlhakim at gmail.com>
 */
public enum DeviceMode {
    ACTIVE_ON_AC(0),ALARMED(1),ACTIVE_ON_BATTERY(2),INACTIVE(2);
    
    private final int value;

    private DeviceMode(final int newValue) {
        value = newValue;
    }

    /**
     * Get <code>Integer</code> value of Device Mode
     * @return integer value of device mode
     */
    public short getValue() {
        return (short) value;
    }
}
