package main;

import utils.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class: Manages the application launch
 *
 * @author Hussein Coulibaly
 */


public class Main extends Application{


    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
//      stage.setTitle("Appointment Manager");
        stage.show();
    }

    public static void main(String[] args) {

        DBConnection.startConnection();
        launch(args);
        DBConnection.closedConnection();
    }

}