package com.codencare.watcher.esb.processor;

import com.codencare.esb.message.IMessage;
import com.codencare.esb.message.Metajasa01;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 *
 * @author abah
 */
public class Metajasa01Processor implements Processor {

    public void process(Exchange exchng) throws Exception {
        exchng.getIn().setBody(new Metajasa01(exchng.getIn()));
    }

}
