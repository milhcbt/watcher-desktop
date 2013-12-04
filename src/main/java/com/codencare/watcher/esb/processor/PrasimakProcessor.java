/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.esb.processor;

import com.codencare.esb.message.Prasimax;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Camel's Processor for Prasimax
 * @author Iman L Hakim <imanlhakim at gmail.com>
 */
public class PrasimakProcessor implements Processor {

    /**
     * Creating Prasimax Message
     * @param exchng
     * @throws Exception 
     */
    @Override
    public void process(Exchange exchng) throws Exception {
        exchng.getIn().setBody(new Prasimax(exchng.getIn()));
    }

}
