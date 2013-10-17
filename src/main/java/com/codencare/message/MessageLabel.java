package com.codencare.message;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author abah
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
