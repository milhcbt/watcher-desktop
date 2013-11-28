/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codencare.esb.message;

import com.codencare.watcher.util.DataConverter;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import org.apache.camel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author abah
 */
public abstract class MessageBase implements IMessage, Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageBase.class);
    private final Message raw;//TODO:bug, it's make non-sense Message raw always changed
    private final String strRaw;//TODO: store raw massage because raw always changed.
    private final InetAddress localAddress;
    private final int localPort;
    private final InetAddress remoteAddress;
    private final int remotePort;

    protected MessageBase(final Message camelMessage) throws UnknownHostException {

        this.raw = camelMessage;
        this.strRaw = raw.getBody(String.class);
        String localStr = raw.getHeader("CamelNettyLocalAddress", String.class);
        String remoteStr = raw.getHeader("CamelNettyRemoteAddress", String.class);

        if (localStr == null || remoteStr == null) {
            throw new UnsupportedOperationException("Protocol Not supported yet. "
                    + "currently only netty socket supported");
        }

        String src[] = localStr.substring(1).split(":");
        String dest[] = remoteStr.substring(1).split(":");

        this.localAddress = InetAddress.getByName(src[0]);
        this.remoteAddress = InetAddress.getByName(dest[0]);

        localPort = Integer.parseInt(src[1]);
        remotePort = Integer.parseInt(dest[1]);
    }

    public InetAddress getLocalAddress() throws UnknownHostException {
        return localAddress;
    }

    public InetAddress getRemoteAddress() throws UnknownHostException {
        return remoteAddress;
    }

    public int getLocalPort() {
        return localPort;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public Map<String, Object> getHeaders() {
        return raw.getHeaders();
    }

    public String getJsonMessage() {
        ByteArrayOutputStream jsonResult = new ByteArrayOutputStream();
        JsonGenerator jg = Json.createGenerator(jsonResult);
        try {
            jg.writeStartObject()
                    .write(IMessage.REMOTE_IP,DataConverter.bytesToLong( getRemoteAddress().getAddress()))
                    .write(IMessage.DIGITAL1, getDigit1().getValue())
                    .write(IMessage.DIGITAL2, getDigit2().getValue())
                    .write(IMessage.DIGITAL3, getDigit3().getValue())
                    .write(IMessage.DIGITAL4, getDigit4().getValue())
                    .write(IMessage.ANALOG1, getAnalog1())
                    .write(IMessage.ANALOG2, getAnalog2())
                    .write(IMessage.ANALOG3, getAnalog3())
                    .write(IMessage.ANALOG4, getAnalog4())
                    .writeEnd().flush();
        } catch (UnknownHostException ex) {
            LOGGER.error(ex.toString());
        }
        jg.close();
        LOGGER.info(jsonResult.toString());
        return jsonResult.toString();
    }

    public Message getRawMessage() {
        return raw;
    }

    @Override
    public String toString() {
       return  getJsonMessage();
    }
    
    
}
