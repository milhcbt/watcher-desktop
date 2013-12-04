/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */

package com.codencare.esb.message;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import org.apache.camel.Message;

/**
 * this interface defines uniform data format for all kinds device message
 * implementation of this interface will need capability to parse and enrich
 * raw message for certain device.;
 * @author ImanLHakim@gmail.com
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

    /**
     *  set -1 if analog value un-know, 
     * it's assume that valid analog value never negative
     */
    int ANALOG_UNKNOW = -1;
    /**
     * 1 = in (from device to app), 2 = out (from app to device)
     */
    int MESSAGE_IN = 1;
    /**
     * 1 = in (from device to app), 2 = out (from app to device)
     */
    int MESSAGE_OUT = 2;
    

    /**
     * Get value of digital input number 1
     * @return return Digital Input
     */
    public DigitalInput getDigit1();
    /**
     * Get value of digital input number 2
     * @return return Digital Input
     */
    public DigitalInput getDigit2();
    /**
     * Get value of digital input number 3
     * @return return Digital Input
     */
    public DigitalInput getDigit3();
    /**
     * Get value of digital input number 4
     * @return return Digital Input
     */
    public DigitalInput getDigit4();
    /**
     * Get value of analog input number 1
     * @return return positive Integer off analog input
     */
    public short getAnalog1();
    /**
     * Get value of analog input number 2
     * @return return positive Integer off analog input
     */
    public short getAnalog2();
    /**
     * Get value of analog input number 3
     * @return return positive Integer off analog input
     */
    public short getAnalog3();
    /**
     * Get value of analog input number 4
     * @return return positive Integer off analog input
     */
    public short getAnalog4();

    /**
     * Get raw message in Camel's Message format. 
     * get into body to see real massage.
     * some common header may be gathered using other methods. 
     * @return raw massage and it's properties/headers
     */
    public Message getRawMessage();

    /**
     * Get address of machine that received the message from device
     * @return machine IP  or host name
     * @throws UnknownHostException 
     */
    public InetAddress getLocalAddress() throws UnknownHostException;
    /**
     * Get address of device which sent the message.
     * @return IP or Host of device
     * @throws UnknownHostException 
     */
    public InetAddress getRemoteAddress() throws UnknownHostException;

    /**
     * Get port that received message
     * @return port number
     */
    public int getLocalPort();
    /**
     * Get port that sent message
     * @return port number
     */
    public int getRemotePort();

    /**
     * Get a map of message's headers
     * @return a map of headers
     */
    public Map<String, Object> getHeaders();

    /**
     * Get enriched message in JSON format.
     * @return enriched message in JSON format.
     */
    public String getJsonMessage();
    
    /**
     * is the message urgent or not
     * by default only digital input no 1 considered as urgent message
     * this method's value may override by system setting
     * @return true if urgent and false if not urgent
     */
    public boolean getUrgent();
    
    /**
     * is the message has been resolved or not.
     * this method's value may override by system setting
     * @return return status of message
     */
    public ResolveStatus getResolve();
}
