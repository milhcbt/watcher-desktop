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
 * IMessage implementation of Prasimax device.
 * @author ImanLHakim@gmail.com
 */
public class Prasimax extends MessageBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(Prasimax.class);
    private final String strBody;

    /**
     * A Constructor as mandated by class BaseMessage
     * @param raw Message in Camel Netty Socket format.
     * @throws UnknownHostException 
     */
    public Prasimax(Message raw) throws UnknownHostException {
        super(raw);
        this.strBody = raw.getBody(String.class).trim();
        LOGGER.debug("processing:" + strBody);
    }

    
    @Override
    public DigitalInput getDigit1() {
        if (strBody.matches("i")) {
            return DigitalInput.LOW;
        } else if (strBody.matches("I")) {
            return DigitalInput.HIGH;
        } else {
            return DigitalInput.UNKNOW;
        }
    }

    @Override
    public DigitalInput getDigit2() {
        if (strBody.matches("j")) {
            return DigitalInput.LOW;
        } else if (strBody.matches("J")) {
            return DigitalInput.HIGH;
        } else {
            return DigitalInput.UNKNOW;
        }
    }

    @Override
    public DigitalInput getDigit3() {
        if (strBody.matches("k")) {
            return DigitalInput.LOW;
        } else if (strBody.matches("K")) {
            return DigitalInput.HIGH;
        } else {
            return DigitalInput.UNKNOW;
        }
    }

    @Override
    public DigitalInput getDigit4() {
        if (strBody.matches("l")) {
            return DigitalInput.LOW;
        } else if (strBody.matches("L")) {
            return DigitalInput.HIGH;
        } else {
            return DigitalInput.UNKNOW;
        }
    }

    @Override
    public int getAnalog1() {
        Pattern pattern = Pattern.compile("M\\d{1,4}");
        Matcher matcher = pattern.matcher(strBody);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(0).substring(1));
        }
        return ANALOG_UNKNOW;
    }

    @Override
    public int getAnalog2() {
        Pattern pattern = Pattern.compile("N\\d{1,4}");
        Matcher matcher = pattern.matcher(strBody);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(0).substring(1));
        }
        return ANALOG_UNKNOW;
    }

    @Override
    public int getAnalog3() {
        Pattern pattern = Pattern.compile("O\\d{1,4}");
        Matcher matcher = pattern.matcher(strBody);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(0).substring(1));
        }
        return ANALOG_UNKNOW;
    }

    @Override
    public int getAnalog4() {
        Pattern pattern = Pattern.compile("P\\d{1,4}");
        Matcher matcher = pattern.matcher(strBody);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(0).substring(1));
        }
        return ANALOG_UNKNOW;
    }

    @Override
    public boolean getUrgent() {
        return getDigit1() == DigitalInput.HIGH || getDigit1() == DigitalInput.LOW;
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
