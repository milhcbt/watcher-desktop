/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.esb.message;

import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.camel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IMessage implementation for Metajasa type 1.
 * @author ImanLHakim@gmail.com
 */
public class Metajasa01 extends MessageBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(Metajasa01.class);
    private static final int BODY_HEAD = 0;
    private static final int BODY_SOURCE = 1;
    private static final int BODY_DIGITAL = 2;
    private static final int BODY_ANALOG = 3;
    private final String [] strBody ;
    

    /**
     * A Constructor as mandated by class BaseMessage
     * @param raw Message in Camel Netty Socket format.
     * @throws UnknownHostException 
     */
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

    @Override
    public DigitalInput getDigit1() {
        if (strBody[BODY_DIGITAL].charAt(0) == '0') {
            return DigitalInput.LOW;
        } else if (strBody[BODY_DIGITAL].charAt(0) == '1') {
            return DigitalInput.HIGH;
        } else {
            return DigitalInput.UNKNOW;
        }
    }

    @Override
    public DigitalInput getDigit2() {
        if (strBody[BODY_DIGITAL].charAt(1) == '0') {
            return DigitalInput.LOW;
        } else if (strBody[BODY_DIGITAL].charAt(1) == '1') {
            return DigitalInput.HIGH;
        } else {
            return DigitalInput.UNKNOW;
        }
    }

    @Override
    public DigitalInput getDigit3() {
        if (strBody[BODY_DIGITAL].charAt(2) == '0') {
            return DigitalInput.LOW;
        } else if (strBody[BODY_DIGITAL].charAt(2) == '1') {
            return DigitalInput.HIGH;
        } else {
            return DigitalInput.UNKNOW;
        }
    }

    @Override
    public DigitalInput getDigit4() {
        if (strBody[BODY_DIGITAL].charAt(3) == '0') {
            return DigitalInput.LOW;
        } else if (strBody[BODY_DIGITAL].charAt(3) == '1') {
            return DigitalInput.HIGH;
        } else {
            return DigitalInput.UNKNOW;
        }
    }

    @Override
    public int getAnalog1() {
       if (strBody[BODY_SOURCE].charAt(strBody[BODY_SOURCE].length()-1) == '5'){
           return Integer.parseInt(strBody[BODY_ANALOG]);
       }
       else return ANALOG_UNKNOW;
    }

    @Override
    public int getAnalog2() {
       if (strBody[BODY_SOURCE].charAt(strBody[BODY_SOURCE].length()-1) == '6'){
           return Integer.parseInt(strBody[BODY_ANALOG]);
       }
       else return ANALOG_UNKNOW;
    }

    @Override
    public int getAnalog3() {
       if (strBody[BODY_SOURCE].charAt(strBody[BODY_SOURCE].length()-1) == '7'){
           return Integer.parseInt(strBody[BODY_ANALOG]);
       }
      else return ANALOG_UNKNOW;
    }

    @Override
    public int getAnalog4() {
      if (strBody[BODY_SOURCE].charAt(strBody[BODY_SOURCE].length()-1) == '8'){
           return Integer.parseInt(strBody[BODY_ANALOG]);
       }
       else return ANALOG_UNKNOW;
    }

    @Override
    public boolean getUrgent() {
        return strBody[BODY_SOURCE].charAt(strBody[BODY_SOURCE].length()-1) == '1';
    }

    @Override
    public ResolveStatus getResolve() {
        if (getDigit1() == DigitalInput.HIGH) {
            return ResolveStatus.UNRESOLVED;
        } else if (getDigit1() == DigitalInput.LOW) {
            return ResolveStatus.RESOLVED;
        }
        return ResolveStatus.NORMAL;
    }
       
}
