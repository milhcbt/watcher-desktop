/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.dialog;

import static com.codencare.watcher.desktop.MainApp.defaultProps;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 *
 * @author Iman L Hakim <imanlhakim at gmail.com>
 */
public class AlertDialog extends Stage {
    private static final double  GAP = 20;
    private static double lastX = 0;
    private static double lastY = 0;
    
    private final int WIDTH_DEFAULT = 300;
    

    public static final int ICON_INFO = 0;
    public static final int ICON_ERROR = 1;
    
    public AlertDialog(Window owner, String msg, int type,EventHandler eventHandler) {
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.TRANSPARENT);

        Label label = new Label(msg);
        label.setWrapText(true);
        label.setGraphicTextGap(20);
        ImageView imgView = new ImageView(getImage(type));
        imgView.setFitHeight(40);
        imgView.setFitWidth(40);
        label.setGraphic(imgView);

        Button button = new Button("OK");
        button.setOnAction(eventHandler);

        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(getClass().getResource(defaultProps.getProperty("CSS")).toExternalForm());
        borderPane.setTop(label);

        HBox hbox2 = new HBox();
        hbox2.setAlignment(Pos.CENTER);
        hbox2.getChildren().add(button);
        borderPane.setBottom(hbox2);

        // calculate width of string
        final Text text = new Text(msg);
        text.snapshot(null, null);
        // + 20 because there is padding 10 left and right
        int width = (int) text.getLayoutBounds().getWidth() + 40;

        if (width < WIDTH_DEFAULT) {
            width = WIDTH_DEFAULT;
        }

        int height = 100;

        final Scene scene = new Scene(borderPane, width, height);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);

        // make sure this stage is centered on top of its owner
        if (lastX == 0){
            lastX = owner.getX() + (owner.getWidth() / 2 - width / 2);
        }else{
            lastX += GAP;
        }
        setX(lastX);
        if (lastY == 0){
            lastY = owner.getY() + (owner.getHeight() / 2 - height / 2);
        }else{
            lastY +=GAP; 
        }
        setY(lastY);
    }

    private Image getImage(int type) {
        if (type == ICON_ERROR) {
            return new Image(getClass().getResourceAsStream("/styles/mine/error.png"));
        } else {
            return new Image(getClass().getResourceAsStream("/styles/mine/info.png"));
        }
    }

    @Override
    public void close(){
        super.close();
        lastX -= GAP;
        lastY -= GAP;
    }
}
