/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.desktop;

import com.codencare.esb.message.IMessage;
import com.codencare.esb.message.Metajasa01;
import com.codencare.esb.message.Prasimax;
import com.codencare.watcher.controller.DeviceJpaController;
import com.codencare.watcher.controller.exceptions.IllegalOrphanException;
import com.codencare.watcher.controller.exceptions.NonexistentEntityException;
import static com.codencare.watcher.desktop.TraditionalMainController.MEDIA_URL;
import com.codencare.watcher.dialog.AlertDialog;
import com.codencare.watcher.entity.Device;
import com.codencare.watcher.util.DataConverter;
import java.util.Date;
import java.util.Stack;
import javafx.application.Platform;
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

    private static final Logger LOGGER = Logger.getLogger(MainFXMLController.class.getName());
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("watcherDB");
    static final Media media = new Media(MEDIA_URL.toString());
    static final MediaPlayer mediaPlayer = new MediaPlayer(media);
    static final Stack<AlertDialog> alerts = new Stack<>();
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
                            /*save message to database 
                             and complete it with meta-data from database*/
                            .process(new Processor() {

                                @Override
                                public void process(Exchange exchange)
                                throws Exception {
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
                            })
                            /*turn sound if needed*/
                            .process(new Processor() {

                                @Override
                                public void process(Exchange exchange) throws Exception {
                                    Platform.runLater(new Runnable() {

                                        @Override
                                        public void run() {
                                            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
//                                          mediaPlayer.setAutoPlay(true);//FIXME: delete if not needed
                                            mediaPlayer.play();
                                        }
                                    });
                                }

                            })
                            /*show dialog box with address if needed*/
                            .process(new Processor() {
                                @Override
                                public void process(final Exchange exchng) throws Exception {
                                    Platform.runLater(new Runnable() {

                                        @Override
                                        public void run() {
                                            Object body = exchng.getIn().getBody();
                                            if (body instanceof Device) {
                                                AlertDialog alert = new AlertDialog(target,
                                                        "alarm at " + ((Device) body).getAddress(),
                                                        AlertDialog.ICON_INFO,
                                                        new EventHandler() {
                                                            @Override
                                                            public void handle(Event event) {
                                                                alerts.pop();
                                                                ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
                                                                if (alerts.size() == 0) {
                                                                    mediaPlayer.stop();
                                                                }
                                                            }
                                                        }
                                                );
                                                alerts.push(alert);
                                                alert.showAndWait();
                                            }
                                        }
                                    });
                                }

                            })
                            /*TODO:sent SMS notification if needed, use Camel or not?*/
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

    /**
     * Stop camel engine
     *
     * @throws Exception
     */
    public void stopContext() throws Exception {
        context.stop();
    }

    public static void fillDevice(Device d, IMessage msg) {
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
