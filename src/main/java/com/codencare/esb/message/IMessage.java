/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codencare.esb.message;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import org.apache.camel.Message;

/**
 *
 * @author abah
 */
public interface IMessage {
    String REMOTE_IP ="remoteIp"; 
    String DIGITAL1 = "digit1";
    String DIGITAL2 = "digit2";
    String DIGITAL3 = "digit3";
    String DIGITAL4 = "digit4";
    String ANALOG1 = "analog1";
    String ANALOG2 = "analog2";
    String ANALOG3 = "analog3";
    String ANALOG4 = "analog4";

    int ANALOG_UNKNOW = -1;
    int MESSAGE_IN = 1;//1 = in (from device to app), 2 = out (from app to device)
    int MESSAGE_OUT = 2;//1 = in (from device to app), 2 = out (from app to device)
    

    public DigitalInput getDigit1();

    public DigitalInput getDigit2();

    public DigitalInput getDigit3();

    public DigitalInput getDigit4();

    public int getAnalog1();

    public int getAnalog2();

    public int getAnalog3();

    public int getAnalog4();

    public Message getRawMessage();

    public InetAddress getLocalAddress() throws UnknownHostException;

    public InetAddress getRemoteAddress() throws UnknownHostException;

    public int getLocalPort();

    public int getRemotePort();

    public Map<String, Object> getHeaders();

    public String getJsonMessage();
}
