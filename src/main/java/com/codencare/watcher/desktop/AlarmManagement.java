package com.codencare.watcher.desktop;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author reegan
 */
public class AlarmManagement extends Application {

    private void init(Stage primaryStage) {

        Group root = new Group();

        primaryStage.setScene(new Scene(root));

        final ObservableList<Person> data = FXCollections.observableArrayList(
                new Person("Alr1","Panic Button",false,false,false,false,false),
                new Person("Alr2","Network Connection Down",false,false,false,false,false),
                new Person("Alr3","Operate On Battray",false,false,false,false,false)
                );

        //"Alarm_log" column

        TableColumn Alarm_log = new TableColumn<Person, Boolean>();

        Alarm_log.setText("Alarm_log");

        Alarm_log.setMinWidth(100);

        Alarm_log.setCellValueFactory(new PropertyValueFactory("Alarm_log"));

        Alarm_log.setCellFactory(new Callback<TableColumn<Person, Boolean>, TableCell<Person, Boolean>>() {
            public TableCell<Person, Boolean> call(TableColumn<Person, Boolean> p) {

                return new CheckBoxTableCell<Person, Boolean>();

            }
        });
        
         //"Alarm_window" column

        TableColumn Alarm_window = new TableColumn<Person, Boolean>();

        Alarm_window.setText("Alarm_window");

        Alarm_window.setMinWidth(100);

        Alarm_window.setCellValueFactory(new PropertyValueFactory("Alarm_window"));

        Alarm_window.setCellFactory(new Callback<TableColumn<Person, Boolean>, TableCell<Person, Boolean>>() {
            public TableCell<Person, Boolean> call(TableColumn<Person, Boolean> p) {

                return new CheckBoxTableCell<Person, Boolean>();

            }
        });
        
        //"SMS_customer" column

        TableColumn SMS_customer = new TableColumn<Person, Boolean>();

        SMS_customer.setText("SMS_customer");

        SMS_customer.setMinWidth(100);

        SMS_customer.setCellValueFactory(new PropertyValueFactory("SMS_customer"));

        SMS_customer.setCellFactory(new Callback<TableColumn<Person, Boolean>, TableCell<Person, Boolean>>() {
            public TableCell<Person, Boolean> call(TableColumn<Person, Boolean> p) {

                return new CheckBoxTableCell<Person, Boolean>();

            }
        });
        
        //"SMS_group1" column

        TableColumn SMS_group1 = new TableColumn<Person, Boolean>();

        SMS_group1.setText("SMS_group1");

        SMS_group1.setMinWidth(100);

        SMS_group1.setCellValueFactory(new PropertyValueFactory("SMS_group1"));

        SMS_group1.setCellFactory(new Callback<TableColumn<Person, Boolean>, TableCell<Person, Boolean>>() {
            public TableCell<Person, Boolean> call(TableColumn<Person, Boolean> p) {

                return new CheckBoxTableCell<Person, Boolean>();

            }
        });
        
        //"SMS_group2" column

        TableColumn SMS_group2 = new TableColumn<Person, Boolean>();

        SMS_group2.setText("SMS_group2");

        SMS_group2.setMinWidth(100);

        SMS_group2.setCellValueFactory(new PropertyValueFactory("SMS_group2"));

        SMS_group2.setCellFactory(new Callback<TableColumn<Person, Boolean>, TableCell<Person, Boolean>>() {
            public TableCell<Person, Boolean> call(TableColumn<Person, Boolean> p) {

                return new CheckBoxTableCell<Person, Boolean>();

            }
        });

        //"alarm_id Name" column

        TableColumn alarm_id = new TableColumn();

        alarm_id.setText("alarm_id");
        alarm_id.setMinWidth(100);

        alarm_id.setCellValueFactory(new PropertyValueFactory("alarm_id"));

        //"alarm_des Name" column

        TableColumn alarm_des = new TableColumn();

        alarm_des.setText("alarm_des");
        alarm_des.setMinWidth(100);

        alarm_des.setCellValueFactory(new PropertyValueFactory("alarm_des"));

        

        //Set cell factory for cells that allow editing

        Callback<TableColumn, TableCell> cellFactory =
                new Callback<TableColumn, TableCell>() {
            public TableCell call(TableColumn p) {

                return new EditingCell();

            }
        };

        alarm_id.setCellFactory(cellFactory);

        alarm_des.setCellFactory(cellFactory);



        //Set handler to update ObservableList properties. Applicable if cell is edited

        //updateObservableListProperties(alarm_id, alarm_des);



        TableView tableView = new TableView();

        tableView.setItems(data);

        //Enabling editing

        VBox vb = new VBox();

        tableView.setEditable(false);

        tableView.getColumns().addAll(alarm_id, alarm_des, Alarm_log, Alarm_window,SMS_customer,SMS_group1,SMS_group2);

        Button bt1 = new Button("Update");
        Button bt2 = new Button("Close");
        
        CheckBox cb = new CheckBox("Select all");
        cb.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {
                if (new_val) {
                    for (Person p : data) {
                        p.Alarm_log.set(true);
                        p.Alarm_window.set(true);
                        p.SMS_customer.set(true);
                        p.SMS_group1.set(true);
                        p.SMS_group2.set(true);
                    }

                }


            }
        });
        

        vb.getChildren().addAll(cb, tableView);
        root.getChildren().addAll(vb);

    }


    //Person object
    public static class Person {
        private StringProperty alarm_id;
        private StringProperty alarm_des;
        private BooleanProperty Alarm_log;
        private BooleanProperty Alarm_window;
        private BooleanProperty SMS_customer;
        private BooleanProperty SMS_group1;
        private BooleanProperty SMS_group2;

        private Person(String alarm_id, String alarm_des, boolean Alarm_log,boolean Alarm_window,boolean SMS_customer,boolean SMS_group1,boolean SMS_group2 ) {

            this.alarm_id = new SimpleStringProperty(alarm_id);
            this.alarm_des = new SimpleStringProperty(alarm_des);
            this.Alarm_log = new SimpleBooleanProperty(Alarm_log);  
            this.Alarm_window = new SimpleBooleanProperty(Alarm_window); 
            this.SMS_customer = new SimpleBooleanProperty(SMS_customer);
            this.SMS_group1 = new SimpleBooleanProperty(SMS_group1);
            this.SMS_group2 = new SimpleBooleanProperty(SMS_group2);



            /*this.invited.addListener(new ChangeListener<Boolean>() {
                public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {

                    System.out.println(firstNameProperty().get() + " invited: " + t1);

                }
            });*/

        }
        public BooleanProperty Alarm_logProperty() {
            return Alarm_log;
        }
        public BooleanProperty Alarm_windowProperty() {
            return Alarm_window;
        }
        public BooleanProperty SMS_customerProperty() {
            return SMS_customer;
        }
        public BooleanProperty SMS_group1Property() {
            return SMS_group1;
        }
        public BooleanProperty SMS_group2Property() {
            return SMS_group2;
        }

        public StringProperty alarm_idProperty() {
            return alarm_id;
        }

        public StringProperty alarm_desProperty() {
            return alarm_des;
        }

        

        /*public void setLastName(String lastName) {
            this.lastName.set(lastName);
        }

        public void setFirstName(String firstName) {
            this.firstName.set(firstName);
        }

        public void setEmail(String email) {
            this.email.set(email);
        }*/
    }

    //CheckBoxTableCell for creating a CheckBox in a table cell
    public static class CheckBoxTableCell<S, T> extends TableCell<S, T> {

        private final CheckBox checkBox;
        private ObservableValue<T> ov;

        public CheckBoxTableCell() {

            this.checkBox = new CheckBox();

            this.checkBox.setAlignment(Pos.CENTER);



            setAlignment(Pos.CENTER);

            setGraphic(checkBox);

        }

        @Override
        public void updateItem(T item, boolean empty) {

            super.updateItem(item, empty);

            if (empty) {

                setText(null);

                setGraphic(null);

            } else {

                setGraphic(checkBox);

                if (ov instanceof BooleanProperty) {

                    checkBox.selectedProperty().unbindBidirectional((BooleanProperty) ov);

                }

                ov = getTableColumn().getCellObservableValue(getIndex());

                if (ov instanceof BooleanProperty) {

                    checkBox.selectedProperty().bindBidirectional((BooleanProperty) ov);

                }

            }

        }
    }

    // EditingCell - for editing capability in a TableCell
    public static class EditingCell extends TableCell<Person, String> {

        private TextField textField;

        public EditingCell() {
        }

        @Override
        public void startEdit() {

            super.startEdit();



            if (textField == null) {

                createTextField();

            }

            setText(null);

            setGraphic(textField);

            textField.selectAll();

        }

        @Override
        public void cancelEdit() {

            super.cancelEdit();

            setText((String) getItem());

            setGraphic(null);

        }

        @Override
        public void updateItem(String item, boolean empty) {

            super.updateItem(item, empty);

            if (empty) {

                setText(null);

                setGraphic(null);

            } else {

                if (isEditing()) {

                    if (textField != null) {

                        textField.setText(getString());

                    }

                    setText(null);

                    setGraphic(textField);

                } else {

                    setText(getString());

                    setGraphic(null);

                }

            }

        }

        private void createTextField() {

            textField = new TextField(getString());

            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

            textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent t) {

                    if (t.getCode() == KeyCode.ENTER) {

                        commitEdit(textField.getText());

                    } else if (t.getCode() == KeyCode.ESCAPE) {

                        cancelEdit();

                    }

                }
            });

        }

        private String getString() {

            return getItem() == null ? "" : getItem().toString();

        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        init(primaryStage);

        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}