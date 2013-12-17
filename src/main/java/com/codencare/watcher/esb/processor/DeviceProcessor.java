/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.esb.processor;

import com.codencare.esb.message.IMessage;
import com.codencare.watcher.controller.DeviceJpaController;
import com.codencare.watcher.controller.exceptions.IllegalOrphanException;
import com.codencare.watcher.controller.exceptions.NonexistentEntityException;
import com.codencare.watcher.entity.Device;
import com.codencare.watcher.util.DataConverter;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

/**
 * Camel Processor for mapping message to database device
 *
 * @author Iman L Hakim <imanlhakim at gmail.com>
 */
public class DeviceProcessor implements Processor {

    private static final Logger LOGGER = Logger.getLogger(DeviceProcessor.class.getName());
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("watcherDB");

    /**
     * Map message to Device
     *
     * @param exchng
     * @throws Exception
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        DeviceJpaController dc
                = new DeviceJpaController(emf);
        IMessage msg = exchange.getIn()
                .getBody(IMessage.class);
        /*FIXME: loading device first is unnecessary
         and may decrease performance, use native query
         for better performance */
        Device d = dc.findDevice(DataConverter.
                bytesToLong(msg.getLocalAddress().
                        getAddress()));
        if (d != null) {
            fillDevice(d, msg);
            try {
                d.setMode(Device.MODE_ALARMED);
                dc.edit(d);
                exchange.getIn().setBody(d, Device.class);
            } catch (IllegalOrphanException |
                    NonexistentEntityException ex) {
                LOGGER.error(ex.toString());
            }
        } else {
            dc.newDevice(DataConverter.
                    bytesToLong(msg.getLocalAddress().
                            getAddress()));
        }
    }

    private static void fillDevice(Device d, IMessage msg) {
        d.setAnalog1(msg.getAnalog1());
        d.setAnalog2(msg.getAnalog2());
        d.setAnalog3(msg.getAnalog3());
        d.setAnalog4(msg.getAnalog4());
        d.setDigit1(msg.getDigit1().getValue());
        d.setDigit2(msg.getDigit2().getValue());
        d.setDigit3(msg.getDigit3().getValue());
        d.setDigit4(msg.getDigit4().getValue());
        d.setLastTime(new Date());
    }
}
