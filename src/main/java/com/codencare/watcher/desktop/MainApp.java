package com.codencare.watcher.desktop;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.Logger;
import org.controlsfx.control.ButtonBar;
import org.controlsfx.control.ButtonBar.ButtonType;
import org.controlsfx.control.action.AbstractAction;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;

public class MainApp extends Application {

    private static final Logger LOGGER = Logger.getLogger(MainApp.class.getName());
    public static final Properties defaultProps = new Properties();

    private static final double W_GAP = 10;
    private static final double H_GAP = 20;

     // This dialog will consist of two input fields (username and password),
        // and have two buttons: Login and Cancel.
    final TextField txUserName = new TextField();
    final PasswordField txPassword = new PasswordField();
    final Action actionLogin = new AbstractAction("Login") {
        {
            ButtonBar.setType(this, ButtonType.OK_DONE);
        }

        // This method is called when the login button is clicked...
        public void execute(ActionEvent ae) {
            Dialog dlg = (Dialog) ae.getSource();
            // real login code here
            dlg.hide();
        }
    };

   //TODO:make properties has default value, in case properties has error or typo
    static {
        
//            try (InputStream is = MainApp.class.getResourceAsStream("/watcher.properties")) {
            try (InputStream is = new FileInputStream("./watcher.properties")) {
                defaultProps.load(is);
            }catch (IOException ex) {
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

       
        showLoginDialog();
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
        ScrollPane mainPane = (ScrollPane) rootPane.lookup("#mainPane");
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

    // This method is called when the user types into the username / password fields  
    private void validate() {
        actionLogin.disabledProperty().set(
                txUserName.getText().trim().isEmpty() || txPassword.getText().trim().isEmpty());
    }

    // Imagine that this method is called somewhere in your codebase
    private void showLoginDialog() {
        Dialog dlg = new Dialog(null, "Login Dialog");

        // listen to user input on dialog (to enable / disable the login button)
        ChangeListener<String> changeListener = new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                validate();
            }
        };
        txUserName.textProperty().addListener(changeListener);
        txPassword.textProperty().addListener(changeListener);

        // layout a custom GridPane containing the input fields and labels
        final GridPane content = new GridPane();
        content.setHgap(10);
        content.setVgap(10);

        content.add(new Label("User name"), 0, 0);
        content.add(txUserName, 1, 0);
        GridPane.setHgrow(txUserName, Priority.ALWAYS);
        content.add(new Label("Password"), 0, 1);
        content.add(txPassword, 1, 1);
        GridPane.setHgrow(txPassword, Priority.ALWAYS);

        // create the dialog with a custom graphic and the gridpane above as the
        // main content region
        dlg.setResizable(false);
        dlg.setIconifiable(false);
        dlg.setGraphic(new ImageView(getClass().getResource("/styles/img/logo.jpg").toString()));
        dlg.setContent(content);
        dlg.getActions().addAll(actionLogin, Dialog.Actions.CANCEL);
        validate();

        // request focus on the username field by default (so the user can
        // type immediately without having to click first)
        Platform.runLater(new Runnable() {
            public void run() {
                txUserName.requestFocus();
            }
        });

        dlg.show();
    }
}
