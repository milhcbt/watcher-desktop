/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codencare.watcher.desktop;

import com.codencare.watcher.dialog.NewAlarmDialog;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

/**
 *
 * @author abah
 */
public class SocketListener extends Thread{
    private static final Logger LOGGER = Logger.getLogger(SocketListener.class.getName());
    protected int DEFAULT_PORT = 7000;
    private static ServerSocket serverSocket;
    private static Socket sock;
    private Scene sceen;
    
    public SocketListener(Scene sceen){
        this.sceen = sceen;
    }
    @Override
    public void run() {
                 

            while (true) {
                if (serverSocket == null) { ////akibat bug di glassfish yang manggil contextInit 2 kali
                    try {
                        LOGGER.info("opening socket at port:" + DEFAULT_PORT);
                        serverSocket = new ServerSocket(DEFAULT_PORT);
                        LOGGER.info("listening...");
                        Thread.sleep(2000);
                    } catch (IOException | InterruptedException ex) {
                        LOGGER.error(ex.toString());
                        return;//////akibat bug di glassfish yang manggil contextInit 2 kali
                    }
                }
                try {
                    sock = serverSocket.accept();
                    if (sock != null && sock.isConnected() && (!sock.isClosed())) { ////akibat bug di glassfish yang manggil contextInit 2 kali
                        StringBuilder data = new StringBuilder();

                        do {
                            data.append((char) sock.getInputStream().read());
                        } while (sock.getInputStream().available() > 0);

                        Stage dialog = new NewAlarmDialog((Stage) sceen.getWindow(), true, null,data.toString());
                        dialog.show();

                    } else {
                        if (sock != null) {////akibat bug di glassfish yang manggil contextInit 2 kali
                            sock.close();
                        }
                    }
                } catch (IOException ex) {
                    LOGGER.error(ex.toString());
                }
            }
    }
    
}
