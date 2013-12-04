/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.esb.processor;

import com.codencare.esb.message.Metajasa01;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Camel Processor for Metajasa device type 1
 * @author Iman L Hakim <imanlhakim at gmail.com>
 */
public class Metajasa01Processor implements Processor {

    /**
     * Create metajasa message
     * @param exchng
     * @throws Exception 
     */
    @Override
    public void process(Exchange exchng) throws Exception {
        exchng.getIn().setBody(new Metajasa01(exchng.getIn()));
    }

}
