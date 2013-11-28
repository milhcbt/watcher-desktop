/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codencare.esb.message;

import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.camel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author abah
 */
public class Metajasa01 extends MessageBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(Metajasa01.class);
    private static final int BODY_HEAD = 0;
    private static final int BODY_SOURCE = 1;
    private static final int BODY_DIGITAL = 2;
    private static final int BODY_ANALOG = 3;
    private final String [] strBody ;
    

    public Metajasa01(final Message raw) throws UnknownHostException {
        super(raw);
        Pattern pattern = Pattern.compile("IO[^IORST]*\\*$|RST[^IORST]*\\*$");
        Matcher matcher = pattern.matcher(raw.getBody(String.class).trim());
        
        if (matcher.find()) {
            this.strBody = matcher.group(0).split("\\.");
            LOGGER.debug("processing:"+matcher.group(0)+":"+strBody.length);
        }else{
            this.strBody="....".split(".");
        }
      
    }

    public DigitalInput getDigit1() {
        if (strBody[BODY_DIGITAL].charAt(0) == '0') {
            return DigitalInput.LOW;
        } else if (strBody[BODY_DIGITAL].charAt(0) == '1') {
            return DigitalInput.HIGH;
        } else {
            return DigitalInput.UNKNOW;
        }
    }

    public DigitalInput getDigit2() {
        if (strBody[BODY_DIGITAL].charAt(1) == '0') {
            return DigitalInput.LOW;
        } else if (strBody[BODY_DIGITAL].charAt(1) == '1') {
            return DigitalInput.HIGH;
        } else {
            return DigitalInput.UNKNOW;
        }
    }

    public DigitalInput getDigit3() {
        if (strBody[BODY_DIGITAL].charAt(2) == '0') {
            return DigitalInput.LOW;
        } else if (strBody[BODY_DIGITAL].charAt(2) == '1') {
            return DigitalInput.HIGH;
        } else {
            return DigitalInput.UNKNOW;
        }
    }

    public DigitalInput getDigit4() {
        if (strBody[BODY_DIGITAL].charAt(3) == '0') {
            return DigitalInput.LOW;
        } else if (strBody[BODY_DIGITAL].charAt(3) == '1') {
            return DigitalInput.HIGH;
        } else {
            return DigitalInput.UNKNOW;
        }
    }

    public int getAnalog1() {
       if (strBody[BODY_SOURCE].charAt(strBody[BODY_SOURCE].length()-1) == '5'){
           return Integer.parseInt(strBody[BODY_ANALOG]);
       }
       else return ANALOG_UNKNOW;
    }

    public int getAnalog2() {
       if (strBody[BODY_SOURCE].charAt(strBody[BODY_SOURCE].length()-1) == '6'){
           return Integer.parseInt(strBody[BODY_ANALOG]);
       }
       else return ANALOG_UNKNOW;
    }

    public int getAnalog3() {
       if (strBody[BODY_SOURCE].charAt(strBody[BODY_SOURCE].length()-1) == '7'){
           return Integer.parseInt(strBody[BODY_ANALOG]);
       }
      else return ANALOG_UNKNOW;
    }

    public int getAnalog4() {
      if (strBody[BODY_SOURCE].charAt(strBody[BODY_SOURCE].length()-1) == '8'){
           return Integer.parseInt(strBody[BODY_ANALOG]);
       }
       else return ANALOG_UNKNOW;
    }

    public boolean getUrgent() {
        return strBody[BODY_SOURCE].charAt(strBody[BODY_SOURCE].length()-1) == '1';
    }

    public ResolveStatus getResolve() {
        if (getDigit1() == DigitalInput.HIGH) {
            return ResolveStatus.UNRESOLVED;
        } else if (getDigit1() == DigitalInput.LOW) {
            return ResolveStatus.RESOLVED;
        }
        return ResolveStatus.NORMAL;
    }
       
}
