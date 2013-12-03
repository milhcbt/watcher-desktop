/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.esb.message;


/**
 * This class help database value and others code consistent.
 * @author ImanLHakim@gmail.com
 */
public interface MessageLabel {
    String RAW_MESSAGE = "msg";
    String MESSAGE_SOURCE = "msg-source";
    String MESSAGE_VALUE = "msg-value";
    String DIGITAL1 = "digit1";
    String DIGITAL2 = "digit2";
    String DIGITAL3 = "digit3";
    String DIGITAL4 = "digit4";
    String ANALOG1 = "analog1";
    String ANALOG2 = "analog2";
    String ANALOG3 = "analog3";
    String ANALOG4 = "analog4";
    String URGENT = "urgent";
    int VALUE_LOW = -1;
    int VALUE_HIGH = 1;
    String ID = "id";
    int MESSAGE_IN = 1;
    int MESSAGE_OUT = 2;
}
