package com.codencare.watcher.desktop;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.Logger;

public class MainApp extends Application {

    private static final Logger LOGGER = Logger.getLogger(MainApp.class.getName());
    public static final Properties defaultProps = new Properties();

    private static final double W_GAP = 10;
    private static final double H_GAP = 20;

    static {
        try {
            InputStream is = MainApp.class.getResourceAsStream("/watcher.properties");
            defaultProps.load(is);
        } catch (IOException ex) {
            LOGGER.error(ex);
        }
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        Region contentRootRegion = FXMLLoader.load(getClass().getResource("/fxml/TraditionalMain.fxml"));

        //Set a default "standard" or "100%" resolution
        double origW = primScreenBounds.getWidth() - W_GAP;
        double origH = primScreenBounds.getHeight() - H_GAP;

        //If the Region containing the GUI does not already have a preferred width and height, set it.
        //But, if it does, we can use that setting as the "standard" resolution.
        if (contentRootRegion.getPrefWidth() == Region.USE_COMPUTED_SIZE) {
            contentRootRegion.setPrefWidth(origW);
        } else {
            origW = contentRootRegion.getPrefWidth();
        }

        if (contentRootRegion.getPrefHeight() == Region.USE_COMPUTED_SIZE) {
            contentRootRegion.setPrefHeight(origH);
        } else {
            origH = contentRootRegion.getPrefHeight();
        }

        //Wrap the resizable content in a non-resizable container (Group)
        Group group = new Group(contentRootRegion);
        //Place the Group in a StackPane, which will keep it centered
        StackPane rootPane = new StackPane();
        rootPane.getChildren().add(group);
        stage.setTitle(defaultProps.getProperty("title"));
        //Create the scene initally at the "100%" size
        Scene scene = new Scene(rootPane, origW, origH);
        scene.getStylesheets().add(defaultProps.getProperty("CSS"));
        //Bind the scene's width and height to the scaling parameters on the group
        rootPane.prefWidthProperty().bind(scene.widthProperty());
        rootPane.prefHeightProperty().bind(scene.heightProperty());
        ScrollPane mainPane = (ScrollPane)rootPane.lookup("#mainPane");
        mainPane.prefWidthProperty().bind(rootPane.widthProperty());
        mainPane.prefHeightProperty().bind(rootPane.heightProperty().subtract(48));
        
        //Set the scene to the window (stage) and show it
        stage.setScene(scene);
        stage.setX(W_GAP / 2);
        stage.setY(0);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.show();
    }

}
