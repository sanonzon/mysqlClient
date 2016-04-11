/**
 * Created by kaffefe on 2016-01-30.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.ArrayList;

public class GUI extends Application {


    public static void main(String[] args) {
        launch(args);
    }


    /**
     * MAIN WINDOW
     */
    @Override
    public void start(Stage primaryStage) {
        SetInfo setInfo = new SetInfo();
        Label errors = new Label("");

        VBox vbox = new VBox(10);

        /**
         * SQL info
         */
        Button info = new Button("SQL info");
        info.setOnAction(e -> pre_req_Get_tables(setInfo));
        vbox.getChildren().add(info);

        /**
         * List Databases
         */
        HBox hBox = new HBox(10);
        Button listDB = new Button("List Databases");
        listDB.setOnAction(e -> {
            try {
                errors.setText("");
                listDatabases(setInfo);
            } catch (SQLException e1) {
                errors.setText(e1.getMessage());
            }
        });
        Label databaseLabel = new Label("Stuff for later");
        hBox.getChildren().addAll(listDB, databaseLabel);
        vbox.getChildren().add(hBox);

        /**
         * List Tables
         */
        HBox hBox2 = new HBox(10);
        Button tableButton = new Button("GET TABLES");
        tableButton.setOnAction(e -> {
            try {
                errors.setText("");
                fixUrl_and_Connect(setInfo);
            } catch (SQLException e1) {
                errors.setText(e1.getMessage());
            }
        });
        Label tableLabel = new Label("Stuff for later");
        hBox2.getChildren().addAll(tableButton, tableLabel);
        vbox.getChildren().add(hBox2);


        /**
         * Lastly add error label och set window size and stuff.
         */
        vbox.getChildren().add(errors);

        Scene scene = new Scene(vbox, 800, 600);
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("mysql");
        primaryStage.show();
    }

    /**
     * Create URL from instance
     * And get tables.
     */
    void fixUrl_and_Connect(SetInfo setInfo) throws SQLException {
        MySQL mysql = new MySQL();
        ArrayList<Object> arrayList = new ArrayList<>();
        String url = "jdbc:mysql://" + setInfo.getMySQL_ip() + ":" + setInfo.getMySQL_port() + "/" + setInfo.getMySQL_database();

        arrayList = mysql.getTables(url, setInfo.getMySQL_username(), setInfo.getMySQL_password());
        System.out.println("---- TABLES ----");
        for (int i = 0; i < arrayList.size(); i++) {
            System.out.println(arrayList.get(i));
        }

    }

    void listDatabases(SetInfo setInfo) throws SQLException {
        MySQL mySQL = new MySQL();
        ArrayList<Object> arrayList = new ArrayList<>();
        String url = "jdbc:mysql://" + setInfo.getMySQL_ip() + ":" + setInfo.getMySQL_port();
        arrayList = mySQL.getDatabaseList(url, setInfo.getMySQL_username(), setInfo.getMySQL_password());
        System.out.println("---- Databases ----");
        for (int i = 0; i < arrayList.size(); i++) {
            System.out.println(arrayList.get(i));
        }
    }


    /**
     *
     */
    void pre_req_Get_tables(SetInfo setInfo) {

        Stage window = new Stage();
        window.setTitle("Input MySQL information");
        VBox vBox = new VBox(5);

        HBox user = new HBox();
        Label l1 = new Label("Username: ");
        l1.setPrefWidth(70);
        TextField username = new TextField(setInfo.getMySQL_username());
        username.setPrefWidth(200);
        user.getChildren().addAll(l1, username);

        HBox pwd = new HBox();
        Label l2 = new Label("Password: ");
        l2.setPrefWidth(70);
        PasswordField password = new PasswordField();
        password.setPrefWidth(200);
        pwd.getChildren().addAll(l2, password);

        HBox url = new HBox();
        Label l3 = new Label("IP: ");
        l3.setPrefWidth(70);
        TextField ip = new TextField(setInfo.getMySQL_ip());
        ip.setPrefWidth(200);
        Label l4 = new Label(" Port: ");
        TextField port = new TextField(setInfo.getMySQL_port());
        port.setMaxWidth(70);
        url.getChildren().addAll(l3, ip, l4, port);

        HBox db = new HBox();
        Label l5 = new Label("Database: ");
        l5.setPrefWidth(70);
        TextField database = new TextField(setInfo.getMySQL_database());
        database.setPrefWidth(200);
        db.getChildren().addAll(l5, database);

        HBox buttons = new HBox(70);

        Label saveLabel = new Label("");
        saveLabel.setPrefWidth(200);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            setInfo.setMySQL_database(database.getText());
            setInfo.setMySQL_ip(ip.getText());
            setInfo.setMySQL_port(port.getText());
            setInfo.setMySQL_username(username.getText());
            setInfo.setMySQL_password(password.getText());


        /*    saveInfo(username.getText(), password.getText(), ip.getText(), port.getText(), database.getText(), table.getText()); */
            saveLabel.setText("Information saved, press Close.");
        });
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> window.close());
        buttons.getChildren().addAll(saveButton, saveLabel, closeButton);


        //vBox.getChildren().addAll(user, pwd, url, db, tableStuff, buttons);
        vBox.getChildren().addAll(user, pwd, url, db, buttons);
        Scene scene = new Scene(vBox, 500, 300);


        window.setResizable(false);

        window.setScene(scene);
        window.show();


    }


}
