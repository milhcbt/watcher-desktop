/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codencare.watcher.desktop;

import com.codencare.esb.message.IMessage;
import com.codencare.esb.message.Metajasa01;
import com.codencare.esb.message.Prasimax;
import static com.codencare.watcher.desktop.TraditionalMainController.MEDIA_URL;
import java.net.UnknownHostException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Window;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.log4j.Logger;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author Iman L Hakim
 */
public class AlarmListener extends Task<Void> {

    private static final Logger LOGGER = Logger.getLogger(MainFXMLController.class.getName());
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("watcherDB");
    private static CamelContext context;
    private static Window target;
    
    public AlarmListener(Window target){
        AlarmListener.target = target;
    }

    /**
     * Receiving alarm/message from device
     * @return 
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
                            /*parse message base on device */
                            .process(new Processor() {

                                @Override
                                public void process(Exchange exchng) throws Exception {
                                    final String body = exchng.getIn().getBody(String.class);
                                    IMessage msg = null;
                                    if (body.matches("[ijklIJKL]|M\\d{1,4}|N\\d{1,4}|O\\d{1,4}|P\\d{1,4}")) {
                                        msg = new Prasimax(exchng.getIn());
                                    }
                                    if (body.matches("IO[^IORST]*\\*|RST[^IORST]*\\*")) {
                                        msg = new Metajasa01(exchng.getIn());
                                    }
                                    LOGGER.debug(body);
                                    exchng.getIn().setBody(msg, IMessage.class);
                                }

                            })
                            /*save message to database*/
                            /* and complete it with meta-data from database*/
                            /*turn sound if needed*/
                            /*show dialog box with address if needed*/
                            /*sent SMS notification if needed*/
                            .process(new Processor() {
                                @Override
                                public void process(final Exchange exchng) throws Exception {
                                    Platform.runLater(new Runnable() {

                                        @Override
                                        public void run() {
                                            try {
                                                IMessage msg = exchng.getIn().getBody(IMessage.class);
                                                Media media = new Media(MEDIA_URL.toString());
                                                MediaPlayer mediaPlayer = new MediaPlayer(media);
                                                mediaPlayer.setAutoPlay(true);
                                                Action response = Dialogs.create()
                                                .title("alarm")
                                                .owner(target)
                                                .message(msg.getLocalAddress().toString())
                                                //.lightweight()
                                                .showWarning();

                                                if (response == Dialog.Actions.OK) {
                                                    // ... submit user input
                                                } else {
                                                    // ... user cancelled, reset form to default
                                                }
                                            } catch (UnknownHostException ex) {
                                                LOGGER.error(ex);
                                            }
                                        }
                                    });
                                }

                            })
                            /*save to log*/
                            .to("log:com.codencare.watcher");
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
    
    public void stopContext() throws Exception{
        context.stop();
    }

}
