/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.desktop;

import com.codencare.esb.message.DigitalInput;
import com.codencare.esb.message.IMessage;
import com.codencare.watcher.component.DeviceMode;
import com.codencare.watcher.component.MapView;
import com.codencare.watcher.controller.DeviceJpaController;
import com.codencare.watcher.controller.exceptions.IllegalOrphanException;
import com.codencare.watcher.controller.exceptions.NonexistentEntityException;
import com.codencare.watcher.dialog.AlertDialog;
import com.codencare.watcher.entity.Device;
import com.codencare.watcher.esb.processor.DeviceProcessor;
import com.codencare.watcher.esb.processor.MessageProcessor;
import com.codencare.watcher.esb.processor.Metajasa01Processor;
import com.codencare.watcher.esb.processor.PrasimakProcessor;
import com.codencare.watcher.util.DataConverter;
import java.net.URL;
import java.util.Date;
import java.util.Stack;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.log4j.Logger;

/**
 * Camel-netty listener, listen to socket message from devices
 *
 * @author Iman L Hakim <imanlhakim at gmail.com>
 */
public class AlarmListener extends Task<Void> {

    private static final Logger LOGGER = Logger.getLogger(AlarmListener.class.getName());
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("watcherDB");


    /**
     * Context of camel engine.
     */
    private static CamelContext context;
    /**
     * owner for warning dialog of alarm. where warning should be shown.
     */
    private static Window target;

    /**
     * Contructor
     *
     * @param target owner for warning dialog of alarm.where warning should be
     * shown.
     */
    public AlarmListener(Window target) {
        AlarmListener.target = target;
    }

    /**
     * Receiving alarm/message from device
     *
     * @return nothing Void just a stamp.
     */
    @Override
    protected Void call() {
        try {
            if (context == null) {
                context = new DefaultCamelContext();
            }
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    //get message from devices
                    from("netty:tcp://"
                            + MainApp.defaultProps.getProperty("localhost")
                            + ":"
                            + MainApp.defaultProps.getProperty("localport")
                            + "?sync=false"
                            + "&backlog=128"
                            + "&allowDefaultCodec=false"
                            + "&textline=false"
                            + "&delimiter=NULL")
                            .choice()
                            .when(body().convertToString().regex("[ijklIJKL]|M\\d{1,4}|N\\d{1,4}|O\\d{1,4}|P\\d{1,4}"))
                            .process(new PrasimakProcessor())
                            .to("direct:message")
                            .when(body().convertToString().regex("IO[^IORST]*\\*|RST[^IORST]*\\*"))
                            .process(new Metajasa01Processor())
                            .to("direct:message")
                            .otherwise()
                            .to("direct:ping");
                    from("direct:message")
                            .process(new DeviceProcessor())
                            .process(new MessageProcessor(target))
                            .to("log:message");
                    from("direct:ping").to("log:ping");
                }
            });
            context.start();
        } catch (Exception ex) {
            LOGGER.error(ex);
        }
        // while (true) {
        //}
        return null;
    }

    /**
     * Stop camel engine
     *
     * @throws Exception
     */
    public void stopContext() throws Exception {
        context.stop();
    }
}
