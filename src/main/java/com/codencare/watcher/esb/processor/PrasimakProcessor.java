package com.codencare.watcher.esb.processor;

import com.codencare.esb.message.IMessage;
import com.codencare.esb.message.Prasimax;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 *
 * @author abah
 */
public class PrasimakProcessor implements Processor {

    public void process(Exchange exchng) throws Exception {
        exchng.getIn().setBody(new Prasimax(exchng.getIn()));
    }

}
